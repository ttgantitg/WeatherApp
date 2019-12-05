package androidapp.com.weatherapp.presenter;

import android.graphics.drawable.Drawable;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import androidapp.com.weatherapp.R;
import androidapp.com.weatherapp.data.WeatherRepository;
import androidapp.com.weatherapp.data.models.WeatherRequestRestModel;
import androidapp.com.weatherapp.view.MainActivity;
import io.reactivex.observers.DisposableSingleObserver;

public class Presenter implements IPresenter {

	private MainActivity view;
	private HashMap<String, String> presenterMap = new HashMap<>();
	private WeatherRequestRestModel model = new WeatherRequestRestModel();
	private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
	private DateFormat dateFormat = DateFormat.getDateTimeInstance();
	private WeatherRepository repo = new WeatherRepository();

	public Presenter(MainActivity view) {
		this.view = view;
	}

	@Override
	public void requestWeather(String city) {
		repo.requestRetrofit(city)
			.subscribe(new DisposableSingleObserver<WeatherRequestRestModel>() {
			@Override
			public void onSuccess(WeatherRequestRestModel weatherRequestRestModel) {
				model = weatherRequestRestModel;
				addDataToMap();
				setViewsData();
			}
			@Override
			public void onError(Throwable e) {
				model = null;
			}
		});
	}

	@Override
	public void requestWeatherLocation(String lon, String lat) {
		repo.requestRetrofitLocation(lon, lat)
			.subscribe(new DisposableSingleObserver<WeatherRequestRestModel>() {
			@Override
			public void onSuccess(WeatherRequestRestModel weatherRequestRestModel) {
				model = weatherRequestRestModel;
				addDataToMap();
				setViewsData();
			}
			@Override
			public void onError(Throwable e) {
				model = null;
			}
		});
	}

	private void addDataToMap() {
		presenterMap.put("currentCity", model.name + ", " + model.sys.country);
		presenterMap.put("currentTemp", Math.round(model.main.temp) + " \u2103");
		presenterMap.put("currentWindSpeed", Math.round(model.wind.speed) + " m/s");
		presenterMap.put("currentHumidity", Math.round(model.main.humidity) + " %");
		presenterMap.put("currentPressure", Math.round((int) model.main.pressure / 1.333) + " mm");
		presenterMap.put("sunRise", timeFormat.format(new Date((model.sys.sunrise + (Integer.valueOf(model.timezone) - 10800)) * 1000)));
		presenterMap.put("sunSet", timeFormat.format(new Date((model.sys.sunset + (Integer.valueOf(model.timezone) - 10800))* 1000)));
		presenterMap.put("lastUpdate", "Last update: \n"  + dateFormat.format(new Date(model.dt * 1000)));
		presenterMap.put("actualId", String.valueOf(model.weather[0].id));
		presenterMap.put("sunRiseTime", String.valueOf(model.sys.sunrise));
		presenterMap.put("sunSetTime", String.valueOf(model.sys.sunset));
	}

	@Override
	public void refreshAllViewsToZero() {
		view.setCityView("");
		view.setTemperatureView("");
		view.setHumidityView("");
		view.setPressureView("");
		view.setLastUpdateView("");
		view.setSunRiseView("");
		view.setSunSetView("");
		view.setWindSpeedView("");
		view.setWeatherIcon(null);
	}

	private void setViewsData() {
		setCity();
		setCurrentTemp();
		setHumidity();
		setPressure();
		setSunrise();
		setSunset();
		setWindSpeed();
		setLastUpdate();
		setWeatherIcon();
	}

	private void setCity() {
		view.setCityView(presenterMap.get("currentCity"));
		new CityPreference(view).setCity(presenterMap.get("currentCity"));
	}

	private void setCurrentTemp() {
		view.setTemperatureView(presenterMap.get("currentTemp"));
	}

	private void setWindSpeed() {
		view.setWindSpeedView(presenterMap.get("currentWindSpeed"));
	}

	private void setHumidity() {
		view.setHumidityView(presenterMap.get("currentHumidity"));
	}

	private void setPressure() {
		view.setPressureView(presenterMap.get("currentPressure"));
	}

	private void setSunrise() {
		view.setSunRiseView(presenterMap.get("sunRise"));
	}

	private void setSunset() {
		view.setSunSetView(presenterMap.get("sunSet"));
	}

	private void setLastUpdate() {
		view.setLastUpdateView(presenterMap.get("lastUpdate"));
	}

	private void setWeatherIcon() {
		int actualId = Integer.parseInt(presenterMap.get("actualId"));
		long sunrise = Long.parseLong(presenterMap.get("sunRiseTime"));
		long sunset = Long.parseLong(presenterMap.get("sunSetTime"));
		Drawable icon = null;
		long currentTime = new Date().getTime() / 1000;
		if(currentTime >= sunrise && currentTime < sunset) { //day
			if (actualId == 800) {
				icon = view.getDrawable(R.drawable.clearsky_day_icon);
			} else if (actualId == 801) {
				icon = view.getDrawable(R.drawable.cloud_day_icon);
			} else if (actualId >= 802 && actualId < 900) {
				icon = view.getDrawable(R.drawable.cloudy_day_icon);
			} else if (actualId >= 200 && actualId < 300) {
				icon = view.getDrawable(R.drawable.thunderstrom_day_icon);
			} else if (actualId >= 300 && actualId < 500) {
				icon = view.getDrawable(R.drawable.drizzle_day_icon);
			} else if (actualId == 500) {
				icon = view.getDrawable(R.drawable.lightrain_day_icon);
			}else if (actualId > 500 && actualId < 600) {
				icon = view.getDrawable(R.drawable.rain_day_icon);
			} else if (actualId == 600) {
				icon = view.getDrawable(R.drawable.lightsnow_day_icon);
			} else if (actualId > 600 && actualId < 700) {
				icon = view.getDrawable(R.drawable.snow_day_icon);
			} else if (actualId >= 700 && actualId < 800) {
				icon = view.getDrawable(R.drawable.fog_icon);
			}
		} else {	//night
			if (actualId == 800) {
				icon = view.getDrawable(R.drawable.clearsky_night_icon);
			} else if (actualId == 801) {
				icon = view.getDrawable(R.drawable.cloud_night_icon);
			} else if (actualId >= 802 && actualId < 900) {
				icon = view.getDrawable(R.drawable.cloudy_night_icon);
			} else if (actualId >= 200 && actualId < 300) {
				icon = view.getDrawable(R.drawable.thunderstrom_night_icon);
			} else if (actualId >= 300 && actualId < 500) {
				icon = view.getDrawable(R.drawable.drizzle_night_icon);
			} else if (actualId >= 500 && actualId < 600) {
				icon = view.getDrawable(R.drawable.rain_night_icon);
			} else if (actualId >= 600 && actualId < 700) {
				icon = view.getDrawable(R.drawable.snow_night_icon);
			} else if (actualId >= 700 && actualId < 800) {
				icon = view.getDrawable(R.drawable.fog_icon);
			}
		}
		view.setWeatherIcon(icon);
	}
}
