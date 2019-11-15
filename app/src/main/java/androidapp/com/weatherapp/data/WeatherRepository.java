package androidapp.com.weatherapp.data;

import androidapp.com.weatherapp.data.models.WeatherRequestRestModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepository implements IWeatherRepository {

    private ICityWeather cityAPI;
	private ILocationWeather locationAPI;

    public WeatherRepository() {
        cityAPI = createCityAdapter();
        locationAPI = createLocationAdapter();
    }

	private ICityWeather getCityAPI() {
        return cityAPI;
    }
	private ILocationWeather getLocationAPI() {
		return locationAPI;
	}

    private ICityWeather createCityAdapter() {
        return RetrofitInit.getSingleton().retrofit.create(ICityWeather.class);
    }

	private ILocationWeather createLocationAdapter() {
		return RetrofitInit.getSingleton().retrofit.create(ILocationWeather.class);
	}

	@Override
	public Single<WeatherRequestRestModel> requestRetrofit(String city) {
		return getCityAPI().loadWeather(city, ApiConstants.API_KEY, ApiConstants.DEFAULT_UNITS)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<WeatherRequestRestModel> requestRetrofitLocation(String lon, String lat) {
		return getLocationAPI().loadWeather(lon, lat, ApiConstants.API_KEY, ApiConstants.DEFAULT_UNITS)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
