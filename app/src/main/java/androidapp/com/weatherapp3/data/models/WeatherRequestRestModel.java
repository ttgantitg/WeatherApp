package androidapp.com.weatherapp3.data.models;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("base") public String base;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("visibility") public int visibility;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("clouds") public CloudsRestModel clouds;
    @SerializedName("dt") public long dt;
    @SerializedName("sys") public  SysRestModel sys;
    @SerializedName("id") public  long id;
	@SerializedName("timezone") public String timezone;
    @SerializedName("name") public String name;
    @SerializedName("cod") public int cod;
}
