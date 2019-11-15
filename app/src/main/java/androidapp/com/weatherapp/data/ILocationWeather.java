package androidapp.com.weatherapp.data;

import androidapp.com.weatherapp.data.models.WeatherRequestRestModel;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ILocationWeather {
    @GET("weather")
	Single<WeatherRequestRestModel> loadWeather(@Query("lon") String lon,
												@Query("lat") String lat,
												@Query("appid") String keyApi,
												@Query("units") String units);
}
