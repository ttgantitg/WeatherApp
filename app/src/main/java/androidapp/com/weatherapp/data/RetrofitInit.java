package androidapp.com.weatherapp.data;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitInit {

	private static RetrofitInit singleton = null;

	static RetrofitInit getSingleton() {
		if(singleton == null) {
			singleton = new RetrofitInit();
		}
		return singleton;
	}

	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(ApiConstants.API_BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build();
}
