package androidapp.com.weatherapp3.presenter;

public interface IPresenter {
	void requestWeather(String city);
	void requestWeatherLocation(String lon, String lat);
	void refreshAllViewsToZero();
}
