package com.example.nawoo.secretaryproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by dbsal on 2017-12-18.
 */

public class SetWeatherActivity extends AppCompatActivity implements LocationListener, View.OnClickListener{

    LocationManager locationManager;
    double latitude; // 위도
    double longitude; // 경도
    String city = "용인";
    String county = "기흥구";
    String village = "서천동";
    TextView latText, lonText, weather, temperature, mint;
    ImageView w_icon;

    public static Context mContext;

    long dday;

    SimpleDateFormat dateFormat;
    Date dt;

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antivity_set_weather);
        initView();
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        mContext = this;

        try{
            dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
            dt = dateFormat.parse(CurrentSchedule.Date);

            Calendar today = Calendar.getInstance();
            Calendar schcal = Calendar.getInstance();
            schcal.setTime(dt);

            long td = today.getTimeInMillis()/86400000;
            long sd = schcal.getTimeInMillis()/86400000;

            dday = sd - td;

        } catch(ParseException e){
            Log.d("error " , "message : ", e);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);


        if(dday > 14)
        {
            alertBuilder
                    .setTitle("알림")
                    .setMessage("날씨 정보가 없습니다!")
                    .setCancelable(true)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog dialog = alertBuilder.create();
            dialog.show();
            finish();
        }
        else
        {
            if (locationManager != null) {
                requestLocation();
            }
        }


    }
    private void initView() {
        //뷰세팅
        latText = (TextView) findViewById(R.id.latitude);
        lonText = (TextView) findViewById(R.id.longitude);
        weather = (TextView) findViewById(R.id.weather);
        temperature =(TextView) findViewById(R.id.temperature);
        mint = (TextView)findViewById(R.id.temperaturemin);
        w_icon = (ImageView)findViewById(R.id.weather_icon);
    }

    @Override
    public void onLocationChanged(Location location){
        //latitude = location.getLatitude();
        //longitude = location.getLongitude();
        latitude = 37.249554;
        longitude = 127.077864;

        //날씨 가져오기 통신
        getWeather(latitude, longitude);
        //위치정보 모니터링 제거
        locationManager.removeUpdates(SetWeatherActivity.this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
    }

    private void requestLocation() {
        //사용자로 부터 위치정보 권한체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        }
    }

    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getForecast(ApiService.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                String cur_county = null;
                String cur_village = null;

                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    JsonObject object = response.body();
                    if (object != null) {
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        JsonParser parser = new JsonParser();
                        JsonElement root = parser.parse(object.toString())
                                .getAsJsonObject().get("weather");

                        String WEATER_DATA = root.toString();

                        JsonObject weatherObject = new JsonParser().parse(WEATER_DATA).getAsJsonObject();
                        JsonArray weatherNode = weatherObject.get("forecast6days").getAsJsonArray();

                        JsonObject forecastOnject = weatherNode.get(0).getAsJsonObject();
                        //JsonObject forecastObject = forecast.getAsJsonObject();

                        String grid = forecastOnject.get("grid").toString();
                        String sky = forecastOnject.get("sky").toString();
                        String temper = forecastOnject.get("temperature").toString();

                        JsonObject gridObject = new JsonParser().parse(grid).getAsJsonObject();
                        JsonObject skyObject = new JsonParser().parse(sky).getAsJsonObject();
                        JsonObject temperObject = new JsonParser().parse(temper).getAsJsonObject();

                        cur_county = gridObject.get("county").toString();
                        cur_village = gridObject.get("village").toString();

                        String Code;
                        String Name;
                        String maxtmp;
                        String mintmp;

                        String strdday = Long.toString(dday);

                        if(dday >= 0 && dday < 3)
                        {
                            Code = skyObject.get("pmCode3day").toString();
                            Name = skyObject.get("pmName3day").toString();
                            maxtmp = temperObject.get("tmax3day").toString();
                            mintmp = temperObject.get("tmin3day").toString();
                        }
                        else
                        {
                            Code = skyObject.get("pmCode" + strdday +"day").toString();
                            Name = skyObject.get("pmName" +strdday +"day").toString();
                            maxtmp = temperObject.get("tmax" + strdday +"day").toString();
                            mintmp = temperObject.get("tmin" + strdday +"day").toString();
                        }

                        cur_county = cur_county.substring(1, cur_county.length()-1);
                        cur_village = cur_village.substring(1, cur_village.length()-1);
                        Code = Code.substring(1, Code.length()-1);
                        maxtmp = maxtmp.substring(1, maxtmp.length()-1);
                        mintmp = mintmp.substring(1, mintmp.length()-1);
                        maxtmp = "최고 기온 : " + maxtmp + "'";
                        mintmp = "최저 기온 : " + mintmp + "'";

                        switch(Code){
                            case "SKY_W00":
                                w_icon.setImageResource(R.drawable.w38);
                            case "SKY_W01":
                                w_icon.setImageResource(R.drawable.w01);
                            case "SKY_W02":
                                w_icon.setImageResource(R.drawable.w02);
                            case "SKY_W03":
                                w_icon.setImageResource(R.drawable.w03);
                            case "SKY_W04":
                                w_icon.setImageResource(R.drawable.w18);
                            case "SKY_W07":
                                w_icon.setImageResource(R.drawable.w21);
                            case "SKY_W09":
                                w_icon.setImageResource(R.drawable.w12);
                            case "SKY_W10":
                                w_icon.setImageResource(R.drawable.w21);
                            case "SKY_W11":
                                w_icon.setImageResource(R.drawable.w04);
                            case "SKY_W12":
                                w_icon.setImageResource(R.drawable.w13);
                            case "SKY_W13":
                                w_icon.setImageResource(R.drawable.w32);
                        }

                        latText.setText(cur_county);
                        lonText.setText(cur_village);

                        weather.setText(Name);
                        temperature.setText(maxtmp);
                        mint.setText(mintmp);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }

        });
    }

    private interface ApiService {
        //베이스 Url
        String BASEURL = "http://apis.skplanetx.com/";
        String APPKEY = "57b66fe0-eecf-3dec-b3a8-c5af5d84cbc9";

        //get 메소드를 통한 http rest api 통신
        @GET("weather/forecast/6days")
        Call<JsonObject> getForecast(@Header("appkey") String appKey, @Query("version") int version,
                                   @Query("lat") double lat, @Query("lon") double lon);

        /*@GET("weather/forecast/6days")
        Call<JsonObject> getForecast(@Header("appkey") String appKey, @Query("version") int version,
                                     @Query("city") String city, @Query("county") String county,
                                     @Query("village") String village);*/

    }
}
