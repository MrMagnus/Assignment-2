package yh.tipcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ResultActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    //Constants
    static final int SEEK_BAR_DEFAULT_VALUE = 10;

    //class variables
    private EditText billEt;
    private SeekBar tipSb;
    private TextView displayBillTv;
    private TextView tipAmountTv;
    private String billString;

    private int waitressTotalPoints;
    private double billDouble;
    private double seekBarProgress;
    private double seekBarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Create a intent to get an intent
        Intent intent = getIntent();

        //Get the waitress points from the Calculate activity.
        waitressTotalPoints = intent.getIntExtra("TOTAL_WAITRESS_POINTS", -1);

        billEt = (EditText) findViewById(R.id.billEt);
        tipSb = (SeekBar) findViewById(R.id.tipSb);
        displayBillTv = (TextView) findViewById(R.id.displayBillTv);
        tipAmountTv = (TextView) findViewById(R.id.tipAmountTv);

        tipSb.setOnSeekBarChangeListener(this);

        //Set the seek bar value to the default tip value + the points the user gave him/her.
        tipSb.setProgress(SEEK_BAR_DEFAULT_VALUE + waitressTotalPoints);

        //Display the amount of tip percentage the user will give the waitress.
        tipAmountTv.setText(String.format("%.0f", seekBarProgress) + "%");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){

            case R.id.actionCalculateFinalBill:
                billString = billEt.getText().toString();
                //Display a error message if the user forgets to enter a bill.
                if(billString.isEmpty()){
                    Toast.makeText(this, "Enter your bill!", Toast.LENGTH_SHORT).show();
                } else {
                    billDouble = Double.parseDouble(billString);
                    tipCalculator();
                }
            break;

            //Sends the user to the first activity
            case R.id.actionResetAll:
                Intent intent = new Intent(this, CalculateActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When called, this method will calculate your total bill with the cost of what the user have
     * bought and the percentage of tip he/she decided to give. The method
     * then prints out the total cost.
     */
    public void tipCalculator(){
        seekBarValue = seekBarProgress / 100;
        double totalCost = seekBarValue * billDouble + billDouble;

        String scaledTotalCost = String.format("%.2f", totalCost);
        displayBillTv.setText(scaledTotalCost);

    }

    /**
     * Every time the change the value of the seek bar the program will calculate the amount of your
     * total bill.
     * @param seekBar
     * @param i
     * @param b
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        seekBarProgress = tipSb.getProgress();
        //Display the amount of tip percentage the user will give the waitress.
        tipAmountTv.setText(String.format("%.0f", seekBarProgress) + "%");

        tipCalculator();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
