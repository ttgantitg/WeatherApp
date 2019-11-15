package androidapp.com.weatherapp.data;

import androidapp.com.weatherapp.data.models.WeatherRequestRestModel;
import io.reactivex.Single;

public interface IWeatherRepository {
	Single<WeatherRequestRestModel> requestRetrofit(String city);
	Single<WeatherRequestRestModel> requestRetrofitLocation(String lon, String lat);
}
