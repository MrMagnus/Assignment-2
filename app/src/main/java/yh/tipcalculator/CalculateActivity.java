package yh.tipcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.Spinner;


public class CalculateActivity extends Activity implements AdapterView.OnItemSelectedListener, Chronometer.OnChronometerTickListener{

    //Constants
    public static final int GOOD = 2;
    public static final int ALRIGHT = 0;
    public static final int BAD = -2;
    public static final int SECONDS_THRESHOLD = 30;

    //Class Variables
    private Spinner problemSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private Chronometer waitTimeChronometer;
    private long elapsedMillis = 0;
    private long elapsedSeconds = 0;

    //Waitress point variables
    private int waitressIntroductionPoints = 0;
    private int waitressAvailabilityPoints = 0;
    private int waitressProblemSolvingPoints = 0;
    private int waitressTimePoints = 0;
    private int waitressTotalPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        problemSpinner = (Spinner) findViewById(R.id.problemSpinner);
        waitTimeChronometer = (Chronometer) findViewById(R.id.chronometer);

        problemSpinner.setOnItemSelectedListener(this);

        waitTimeChronometer.setOnChronometerTickListener(this);

        //Create an adapter with the array i created in strings resources.
        adapter = ArrayAdapter.createFromResource(this,
                R.array.problemSolvingArray, android.R.layout.simple_spinner_item);

        //Set the layout of the spinner.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter to the spinner
        problemSpinner.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calculate, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.actionStartChronometer:
                //Set start time and start chronometer
                waitTimeChronometer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                waitTimeChronometer.start();
            break;

            case R.id.actionPauseChronometer:
                 //Stops the time
                 waitTimeChronometer.stop();

            break;

            case R.id.actionResetChronometer:
                //Stops the time an resets it.
                waitTimeChronometer.stop();
                elapsedMillis = 0;
                elapsedSeconds = 0;
                waitTimeChronometer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
            break;

            case R.id.actionResultActivity:
                waitTimeChronometer.stop();
                calculateWaitressTotalPoints();
                //create an intent and send the waitress points with it to the next activity
                Intent resultActivityIntent = new Intent(this, ResultActivity.class);
                resultActivityIntent.putExtra("TOTAL_WAITRESS_POINTS", waitressTotalPoints);
                startActivity(resultActivityIntent);

            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Give the waitress 2 points for every alternative that is checked. If the user unchecks an
     * alternative, take away 2 points.
     * @param view
     */
    public void onCheckClick(View view){

        //Looks if anything is checked.
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()){
            case R.id.checkBoxFriendly:
               if(checked) {
                   waitressIntroductionPoints += 2;
               } else{
                   waitressIntroductionPoints -= 2;
               }
            break;

            case R.id.checkBoxSpecials:
                if(checked) {
                    waitressIntroductionPoints += 2;
                } else{
                    waitressIntroductionPoints -= 2;
                }
            break;

            case R.id.checkBoxOpinion:
                if(checked) {
                    waitressIntroductionPoints += 2;
                } else{
                    waitressIntroductionPoints -= 2;
                }
            break;
        }
    }

    /**
     * Gives the waitress points based on what radio button the user selects
     * @param view
     */
    public void onRadioClick(View view){

        switch (view.getId()){
            case R.id.radioButtonGood:
                waitressAvailabilityPoints = GOOD;
            break;

            case R.id.radioButtonAlright:
                waitressAvailabilityPoints = ALRIGHT;
            break;

            case R.id.radioButtonBad:
                waitressAvailabilityPoints = BAD;
            break;
        }
    }

    /**
     * Gives the waitress points based on what spinner option the user selects.
     * @param view
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String choice = parent.getItemAtPosition(position).toString();

        if (choice.equals("Good")){
            waitressProblemSolvingPoints = GOOD;
        }

        if (choice.equals("Alright")){
            waitressProblemSolvingPoints = ALRIGHT;
        }

        if (choice.equals("Bad")){
            waitressProblemSolvingPoints = BAD;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Reduces a point from waitressTimePoint every 30 seconds.
     * @param chronometer
     */
    @Override
    public void onChronometerTick(Chronometer chronometer) {

        //Check the elapsed time
        elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();

        //Converts the elapsed milliseconds to seconds
        elapsedSeconds = elapsedMillis / 1000;
        if(elapsedSeconds % SECONDS_THRESHOLD == 0 && elapsedSeconds != 0){
            System.out.println("Point Reduced");
            //Reduce a point.
            waitressTimePoints --;
        }
    }

    /**
     * Calculate the total of the waitresses points.
     */
    public void calculateWaitressTotalPoints(){
        waitressTotalPoints = waitressIntroductionPoints +
                              waitressAvailabilityPoints +
                              waitressProblemSolvingPoints +
                              waitressTimePoints;
    }
}
