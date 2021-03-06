package androidapp.com.weatherapp.data;

import androidapp.com.weatherapp.data.models.WeatherRequestRestModel;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICityWeather {
    @GET("weather")
	Single<WeatherRequestRestModel> loadWeather(@Query("q") String city,
												@Query("appid") String keyApi,
												@Query("units") String units);
}
