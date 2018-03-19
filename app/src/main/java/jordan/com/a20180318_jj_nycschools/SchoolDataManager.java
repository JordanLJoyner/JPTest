package jordan.com.a20180318_jj_nycschools;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jordan on 3/18/2018.
 */

public class SchoolDataManager {
    private final String LOG_TAG = "SchoolDataManager";
    private ArrayList<SchoolData> mSchoolData = new ArrayList<SchoolData>();
    private HashMap<String, SATData> mSATDataDictionary = new HashMap<String, SATData>();

    //******************
    //Singleton Boilerplate
    //******************
    private static SchoolDataManager instance = null;

    protected SchoolDataManager() {
        // Exists only to defeat instantiation.
    }

    public static SchoolDataManager getInstance() {
        if (instance == null) {
            instance = new SchoolDataManager();
        }
        return instance;
    }

    public ArrayList<SchoolData> GetSchoolDataList() {
        return mSchoolData;
    }


    //******************
    //Getting SchoolData
    //******************
    public interface OnDataPopulatedCallback {
        public void OnDataPopulated(ArrayList<SchoolData> newData);
        public void OnDataFailedToPopulate();
    }

    public void GetSchoolDataFromServer(final OnDataPopulatedCallback dataReadyCallback) {
        //Make the network call to get the school information
        NetworkController controller = new NetworkController();

        if(mSchoolData.size() == 0){
            controller.getSchoolData(new Callback<List<SchoolData>>() {
                @Override
                public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                    if(response.isSuccessful()){
                        Log.d(LOG_TAG,"Response recieved");
                        List<SchoolData> schoolDataList = response.body();
                        for (SchoolData school:schoolDataList) {
                            Log.d("School from change list",school.getSchool_name());
                        }
                        mSchoolData.clear();
                        mSchoolData.addAll(schoolDataList);
                        if(dataReadyCallback != null) {
                            dataReadyCallback.OnDataPopulated(mSchoolData);
                        }
                    } else {
                        System.out.println(response.errorBody());
                        if(dataReadyCallback != null) {
                            dataReadyCallback.OnDataFailedToPopulate();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SchoolData>> call, Throwable t) {
                    t.printStackTrace();
                    if(dataReadyCallback != null) {
                        dataReadyCallback.OnDataFailedToPopulate();
                    }
                }
            });
        } else {
            if(dataReadyCallback != null) {
                dataReadyCallback.OnDataPopulated(mSchoolData);
            }
        }
    }

    public SchoolData GetSchoolData(int id) {
        if(id > -1 && id < mSchoolData.size()) {
            return mSchoolData.get(id);
        }
        return null;
    }

    //******************
    //Getting SATData
    //******************
    public interface OnSATDataPopulatedCallback {
        public void OnSATDataPopulated();
        public void OnSATDataFailedToPopulate();
    }

    public void GetSATDataFromServer(final OnSATDataPopulatedCallback dataReadyCallback){
        //Make the network call to get the school information
        NetworkController controller = new NetworkController();
        if(mSATDataDictionary.isEmpty()) {
            controller.GetSATDataFromServer(new Callback<List<SATData>>() {
                @Override
                public void onResponse(Call<List<SATData>> call, Response<List<SATData>> response) {
                    if(response.isSuccessful()) {
                        mSATDataDictionary.clear();
                        //Convert the list to a dictionary keyed on the school name
                        List<SATData> satDataList = response.body();
                        for (SATData sat : satDataList) {
                            mSATDataDictionary.put(sat.getDbn(), sat);
                            Log.d(LOG_TAG, "Found SAT Data for school: " + sat.getSchool_name());
                        }
                        if(dataReadyCallback != null) {
                            dataReadyCallback.OnSATDataPopulated();
                        }
                    } else {
                        if(dataReadyCallback != null) {
                            dataReadyCallback.OnSATDataFailedToPopulate();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SATData>> call, Throwable t) {
                    t.printStackTrace();
                    if(dataReadyCallback != null){
                        dataReadyCallback.OnSATDataFailedToPopulate();
                    }
                }
            });
        }
    }

    public SATData GetSATDataForSchool(String schoolDatabaseNumber){
        if(mSATDataDictionary.containsKey(schoolDatabaseNumber)) {
            return mSATDataDictionary.get(schoolDatabaseNumber);
        } else {
            return null;
        }
    }

    public int GetNumSATDataKeys(){
        return mSATDataDictionary.size();
    }
}
