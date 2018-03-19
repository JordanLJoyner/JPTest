package jordan.com.a20180318_jj_nycschools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by Jordan on 3/18/2018.
 */

public class SchoolInfoActivity extends AppCompatActivity {
    private static final String EXTRA_SCHOOL_DATA_ID = "com.jordan.school_id";
    private TextView mNumSATTestTakersTextView, mSATMathAverageTextView,
            mSATReadingAverageTextView, mSATWritingAverageTextView;
    private SchoolData mSchoolData;

    public static Intent newIntent(Context packageContext, int dataId){
        Intent intent = new Intent(packageContext, SchoolInfoActivity.class);
        intent.putExtra(EXTRA_SCHOOL_DATA_ID, dataId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_school_info);

        int id = (int)getIntent().getSerializableExtra(EXTRA_SCHOOL_DATA_ID);
        mSchoolData = SchoolDataManager.getInstance().GetSchoolData(id);

        TextView nameTextView = (TextView) findViewById(R.id.school_info_school_name_textview);
        nameTextView.setText(mSchoolData.getSchool_name());

        TextView paragraphTextView = (TextView) findViewById(R.id.school_info_school_overview_textview);
        paragraphTextView.setText("Overview\n"+mSchoolData.getOverview_paragraph());

        TextView subwayTextView = (TextView) findViewById(R.id.school_info_school_subway_textview);
        subwayTextView.setText("Nearby Subway Stations:\n" + mSchoolData.getSubway());

        TextView bouroughTextView = (TextView) findViewById(R.id.school_info_school_bourough_textview);
        bouroughTextView.setText(mSchoolData.getBorough());

        TextView locationTextView = (TextView) findViewById(R.id.school_info_school_location_textview);
        locationTextView.setText(mSchoolData.getLocation());


        //******************
        //Display SATData
        //******************
        mNumSATTestTakersTextView = (TextView) findViewById(R.id.school_info_num_sat_test_takers);
        mSATMathAverageTextView = (TextView) findViewById(R.id.school_info_sat_math_average);
        mSATReadingAverageTextView = (TextView) findViewById(R.id.school_info_sat_reading_average);
        mSATWritingAverageTextView = (TextView) findViewById(R.id.school_info_sat_writing_average);
        UpdateSATFields();

    }

    private void UpdateSATFields(){
        SATData satInfo = SchoolDataManager.getInstance().GetSATDataForSchool(mSchoolData.getDbn());
        String numTakersText = "Number of SAT Test Takers: ";
        String mathAverageText = "Average SAT Math Score: ";
        String readingAverageText = "Average SAT Critical Reading Score: ";
        String writingAverageText = "Average SAT Writing Score: ";
        if(satInfo != null) {
            numTakersText += satInfo.getNum_of_sat_test_takers();
            mathAverageText += satInfo.getSat_math_avg_score();
            readingAverageText += satInfo.getSat_critical_reading_avg_score();
           writingAverageText+= satInfo.getSat_writing_avg_score();
        } else {
            numTakersText += "None found";
            mathAverageText += "--";
            readingAverageText += "--";
            writingAverageText+= "--";
        }
        mNumSATTestTakersTextView.setText(numTakersText);
        mSATMathAverageTextView.setText(mathAverageText);
        mSATReadingAverageTextView.setText(readingAverageText);
        mSATWritingAverageTextView.setText(writingAverageText);
    }
}
