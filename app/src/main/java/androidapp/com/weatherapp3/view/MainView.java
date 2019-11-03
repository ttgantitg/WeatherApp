package androidapp.com.weatherapp3.view;

import android.graphics.drawable.Drawable;

interface MainView {

	void setTemperatureView(String str);
	void setCityView(String str);
	void setWindSpeedView(String str);
	void setHumidityView(String str);
	void setPressureView(String str);
	void setSunRiseView(String str);
	void setSunSetView(String str);
	void setLastUpdateView(String str);
	void setWeatherIcon(Drawable icon);
}
