package androidapp.com.weatherapp.data;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitInit {

	private static volatile RetrofitInit instance;

	static RetrofitInit getInstance() {
		RetrofitInit localInstance = instance;
		if (localInstance == null) {
			synchronized (RetrofitInit.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new RetrofitInit();
				}
			}
		}
		return localInstance;
	}

	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(ApiConstants.API_BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build();
}
