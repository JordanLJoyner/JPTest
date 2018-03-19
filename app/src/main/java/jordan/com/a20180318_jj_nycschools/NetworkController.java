package jordan.com.a20180318_jj_nycschools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jordan on 3/18/2018.
 */

public class NetworkController  {
    static final String BASE_URL = "https://data.cityofnewyork.us/resource/";
    private final String LOG_TAG = "NetworkController";

    private Retrofit GetRetrofit(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    public void getSchoolData(Callback<List<SchoolData>> dataRetrievedCallback) {
        Retrofit retrofit = GetRetrofit();
        SchoolDataAPI schooldataAPI = retrofit.create(SchoolDataAPI.class);
        Call<List<SchoolData>> call = schooldataAPI.getSchoolData("application/json");
        call.enqueue(dataRetrievedCallback);
    }

    public void GetSATDataFromServer(Callback<List<SATData>> SATDataRetrievedCallback) {
        Retrofit retrofit = GetRetrofit();
        SchoolDataAPI schooldataAPI = retrofit.create(SchoolDataAPI.class);
        Call<List<SATData>> call = schooldataAPI.getSATData("application/json");
        call.enqueue(SATDataRetrievedCallback);
    }
}
