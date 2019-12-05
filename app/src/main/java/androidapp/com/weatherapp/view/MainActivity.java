package androidapp.com.weatherapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import java.text.DecimalFormat;
import java.util.Objects;
import androidapp.com.weatherapp.R;
import androidapp.com.weatherapp.presenter.CityPreference;
import androidapp.com.weatherapp.presenter.Presenter;
import androidapp.com.weatherapp.view.dialogs.CloseDialogFragment;
import androidapp.com.weatherapp.view.dialogs.SearchDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity
		implements SearchDialogFragment.SearchDialogListener, MainView {

	@BindView(R.id.weather_icon)
	ImageView weatherIcon;
	@BindView(R.id.city_view)
	TextView cityView;
	@BindView(R.id.temperature_view)
	TextView temperatureView;
	@BindView(R.id.wind_speed_view)
	TextView windSpeedView;
	@BindView(R.id.humidity_view)
	TextView humidityView;
	@BindView(R.id.pressure_view)
	TextView pressureView;
	@BindView(R.id.last_update_view)
	TextView lastUpdateView;
	@BindView(R.id.sunrise_view)
	TextView sunRiseView;
	@BindView(R.id.sunset_view)
	TextView sunSetView;
	@BindView(R.id.progress_bar_container)
	FrameLayout progressBar;
	@BindView(R.id.nav_view)
	BottomNavigationView navView;
	@BindView(R.id.refreshLayout)
	SwipeRefreshLayout mSwipeRefreshLayout;

	private String city;
	private Presenter myPresenter;
	private String lon;
	private String lat;
	private static final int PERMISSION_REQUEST_CODE = 10;
	private LocationManager locationManager;
	private boolean isGrantedPermission = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		myPresenter = new Presenter(this);
		navView.setOnNavigationItemSelectedListener(mOnNavigationItemListener);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (checkNetworkConnection()) {
			if (checkForPermissions()) {
				requestLocationPermissions();
			}
			startInit();
		} else {
			showNoNetConnectionToast();
		}
		initSwipeRefreshLayout();
	}

	private void initSwipeRefreshLayout() {
		mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			if (navView.getSelectedItemId() == R.id.nav_city_search) {
				if (checkNetworkConnection()) {
					startInit();
				} else {
					showNoNetConnectionToast();
				}
			} else if (navView.getSelectedItemId() == R.id.nav_location) {
				if (checkNetworkConnection() & checkGPSProvider()) {
					requestLocation();
				} else {
					showNoNetConnectionOrGPSToast();
				}
			}
			mSwipeRefreshLayout.setRefreshing(false);
		});
	}

	private void showNoNetConnectionOrGPSToast() {
		makeText(getApplicationContext(), getString(R.string.noNetOrGPSConn), Toast.LENGTH_LONG).show();
	}

	private void showNoNetConnectionToast() {
		makeText(getApplicationContext(), getString(R.string.noNetConn), Toast.LENGTH_LONG).show();
	}

	private void startInit() {
		city = new CityPreference(this).getCity();
		myPresenter.requestWeather(city);
	}

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemListener
		= menuItem -> {
			switch (menuItem.getItemId()) {
				case R.id.nav_city_search:
					if (checkNetworkConnection()) {
						showDialog();
					} else {
						showNoNetConnectionToast();
					}
					return true;
				case R.id.nav_location:
					if (checkNetworkConnection() & checkGPSProvider()) {
						if (isGrantedPermission) {
							requestLocation();
						} else {
							requestLocationPermissions();
							return false;
						}
					} else {
						showNoNetConnectionOrGPSToast();
					}
					return true;
				case R.id.nav_report:
					if (checkNetworkConnection()) {
						sendReport();
					} else {
						showNoNetConnectionToast();
					}
					return true;
			}
			return false;
		};

	private void sendReport() {
		Intent email = new Intent(Intent.ACTION_SENDTO);
		email.setType("text/plain");
		email.setData(Uri.parse("mailto:" + "ttgantitg@gmail.com"));
		email.putExtra(Intent.EXTRA_SUBJECT, "Report from App");
		email.putExtra(Intent.EXTRA_TEXT, "Place your email message here ...");
		startActivity(Intent.createChooser(email, "Send Email"));
	}

	public void showDialog() {
		SearchDialogFragment searchFragment = new SearchDialogFragment();
		searchFragment.show(getSupportFragmentManager(), "TAG");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		TextInputEditText inputEditText = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.search_city_text);
		city = Objects.requireNonNull(inputEditText.getText()).toString().trim();
		myPresenter.requestWeather(city);
		new CityPreference(MainActivity.this).setCity(city);
	}

	@Override
	public void onBackPressed() {
		CloseDialogFragment closeFragment = new CloseDialogFragment();
		closeFragment.show(getSupportFragmentManager(), "TAG");
	}

	private boolean checkForPermissions() {
		return (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
	}

	private void requestLocationPermissions() {
		if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
			ActivityCompat.requestPermissions(this,
				new String[]{
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION
				}, PERMISSION_REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults.length == 2 &&
					(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
					grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
				isGrantedPermission = true;
//				requestLocation();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void requestLocation() {
		if (ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			return;
		myPresenter.refreshAllViewsToZero();
		progressBar.setVisibility(View.VISIBLE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locationManager.getBestProvider(criteria, true);
		if (provider != null) {
			locationManager.requestSingleUpdate(provider, new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					DecimalFormat df = new DecimalFormat("#.##");
					lat = Double.valueOf(df.format(location.getLatitude())
							.replaceAll(",", ".")).toString();  // Широта
					lon = Double.valueOf(df.format(location.getLongitude())
							.replaceAll(",", ".")).toString();// Долгота
					progressBar.setVisibility(View.GONE);
					myPresenter.requestWeatherLocation(lon, lat);
				}

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}
			}, null);
		}
	}

	private boolean checkGPSProvider() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean checkNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = Objects.requireNonNull(cm).getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@Override
	public void setTemperatureView(String str) {
		temperatureView.setText(str);
	}

	@Override
	public void setCityView(String str) {
		cityView.setText(str);
	}

	@Override
	public void setWindSpeedView(String str) {
		windSpeedView.setText(str);
	}

	@Override
	public void setHumidityView(String str) {
		humidityView.setText(str);
	}

	@Override
	public void setPressureView(String str) {
		pressureView.setText(str);
	}

	@Override
	public void setSunRiseView(String str) {
		sunRiseView.setText(str);
	}

	@Override
	public void setSunSetView(String str) {
		sunSetView.setText(str);
	}

	@Override
	public void setLastUpdateView(String str) {
		lastUpdateView.setText(str);
	}

	@Override
	public void setWeatherIcon(Drawable icon) {
		weatherIcon.setImageDrawable(icon);
	}
}
