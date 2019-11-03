package androidapp.com.weatherapp3.presenter;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

	private SharedPreferences prefs;

	public CityPreference(Activity activity){
		prefs = activity.getPreferences(Activity.MODE_PRIVATE);
	}

	public String getCity(){
		return prefs.getString("city", "Moscow, RU");
	}

	public void setCity(String city){
		prefs.edit().putString("city", city).apply();
	}
}
