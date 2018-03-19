package jordan.com.a20180318_jj_nycschools;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jordan on 3/18/2018.
 */

public interface SchoolDataAPI {
    @GET("97mf-9njv.json")
    Call<List<SchoolData>> getSchoolData(@Header("Content-Type") String contentType);

    @GET("734v-jeq5.json")
    Call<List<SATData>> getSATData(@Header("Content-Type") String contentType);
}
