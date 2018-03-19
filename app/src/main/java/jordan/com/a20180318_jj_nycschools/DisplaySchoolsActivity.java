package jordan.com.a20180318_jj_nycschools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplaySchoolsActivity extends AppCompatActivity implements SchoolDataManager.OnDataPopulatedCallback {
    private RecyclerView mRecyclerView;
    private SchoolDataAdapter mSchoolDataAdapter;
    private final String LOG_TAG = "DisplaySchoolsActivity";
    private LinearLayout mEmptyLayout;
    private Button mRetryGetDataButton;
    private LinearLayout mRecyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schools);
        mRecyclerView = (RecyclerView) findViewById(R.id.school_data_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEmptyLayout = (LinearLayout) findViewById(R.id.school_data_empty_linear_layout);
        mRecyclerLayout = (LinearLayout) findViewById(R.id.school_data_recycler_view_linear_layout);

        SchoolDataManager.getInstance().GetSchoolDataFromServer(this);
        UpdateUI();
        SchoolDataManager.getInstance().GetSATDataFromServer(new SchoolDataManager.OnSATDataPopulatedCallback() {
            @Override
            public void OnSATDataPopulated() {
                Toast.makeText(getApplicationContext(),"Loaded SAT Data for " + Integer.toString(SchoolDataManager.getInstance().GetNumSATDataKeys()) + " schools",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnSATDataFailedToPopulate() {
                Toast.makeText(getApplicationContext(),"Failed to load SAT Data for Schools",Toast.LENGTH_LONG).show();
            }
        });

        final DisplaySchoolsActivity dsActivityReference = this;
        mRetryGetDataButton = (Button) findViewById(R.id.retry_get_school_info_button);
        mRetryGetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRetryGetDataButton.setEnabled(false);
                SchoolDataManager.getInstance().GetSchoolDataFromServer(dsActivityReference);
            }
        });
        mRetryGetDataButton.setEnabled(false);
    }

    private void UpdateUI(){
        if(mSchoolDataAdapter == null || mSchoolDataAdapter.getItemCount() == 0){
            mRecyclerLayout.setVisibility(View.INVISIBLE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.INVISIBLE);
        }
    }


    //******************
    //RecyclerView ViewHolder Code
    //******************

    //Create a viewholder to hold our views
    //CrimeHolder manages the Ui and sets up the views for individual crime list items for the CrimeAdapter
    private class SchoolDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mSchoolNameTextView;
        private TextView mBoroughTextView;
        private SchoolData mData;
        private int mIndexInRecyclerView;

        public SchoolDataViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mSchoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_textview);
            mBoroughTextView = (TextView) itemView.findViewById(R.id.list_item_school_bourough);
        }

        public void bindData(SchoolData data, int positionInRecyclerView){
            mData = data;
            mSchoolNameTextView.setText(data.getSchool_name());
            mBoroughTextView.setText(data.getBoro());
            mIndexInRecyclerView = positionInRecyclerView;
        }

        @Override
        public void onClick(View v){
            //TODO:Spawn the schoolDetails Activity

            //mLastClickedCrimeIndex = mIndexInRecyclerView;
            Intent intent = SchoolInfoActivity.newIntent(getApplicationContext(),mIndexInRecyclerView);
            startActivity(intent);
        }
    }

    //******************
    //Recycler View Adapter Code
    //******************

    //Create an adapter to manager our viewholders
    private class SchoolDataAdapter extends RecyclerView.Adapter<SchoolDataViewHolder> {
        private List<SchoolData> mSchoolDataList;
        public SchoolDataAdapter(List<SchoolData> schoolData){
            mSchoolDataList = schoolData;
        }

        @Override
        public SchoolDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_school_data,parent,false);
            return new SchoolDataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SchoolDataViewHolder holder, int position){
            SchoolData data = mSchoolDataList.get(position);
            holder.bindData(data,position);
        }

        @Override
        public int getItemCount(){
            return mSchoolDataList.size();
        }

        public void setSchoolData(List<SchoolData> schoolData){
            mSchoolDataList = schoolData;
        }
    }

    //******************
    //Data Populated Interface Functions
    //******************

    @Override
    public void OnDataPopulated(ArrayList<SchoolData> newData) {
        //If our adapter hasn't been set up, do so now
        if(mSchoolDataAdapter == null){
            mSchoolDataAdapter = new SchoolDataAdapter(newData);
            mRecyclerView.setAdapter(mSchoolDataAdapter);
            UpdateUI();
            Toast.makeText(this,"Found " + newData.size() + " Schools", Toast.LENGTH_SHORT).show();
        } else {
            UpdateUI();
            mSchoolDataAdapter.setSchoolData(newData);
            mSchoolDataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnDataFailedToPopulate() {
        Toast.makeText(this, "Failed to get School Data, please retry",Toast.LENGTH_SHORT).show();
        mRetryGetDataButton.setEnabled(true);
    }
}
