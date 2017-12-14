package ca.bcit.comp2052.a00993518.tipcalculatorweek5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    //define variables
    private final int ROUND_NONE = 0;
    private final int ROUND_TIP = 1;
    private final int ROUND_TOTAL = 2;

    //define default values
    private String billAmountString = "";
    private float tipPercent = .15f;
    private int rounding = ROUND_NONE;
    private int split = 1;

    private EditText billAmountEditText;
    private TextView percentTextView;
    private SeekBar percentSeekBar;
    private TextView tipTextView;
    private TextView totalTextView;
    private RadioGroup roundingRadioGroup;
    private RadioButton roundNoneRadioButton;
    private RadioButton roundTipRadioButton;
    private RadioButton roundTotalRadioButton;
    private TextView perPersonLabel;
    private TextView perPersonTextView;
    //private Spinner splitSpinner;
    //private int rbIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);

        //get references to the widgets and set listeners
        billAmountEditText = (EditText) findViewById(R.id.editTextBillAmount);
        billAmountEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                setTitle(text);
                calculateAndDisplay();
            }
        });

        percentTextView = (TextView) findViewById(R.id.textViewPercent);

        percentSeekBar = (SeekBar) findViewById(R.id.seekBarPercent);
        percentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar1)  { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) { }
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                // Implement your logic here
                //setTitle("Tip percent: " + progress);
                String progressString =  Integer.toString( progress );
                percentTextView.setText( progressString.concat("%") );
                calculateAndDisplay();
            }
        });

        tipTextView = (TextView) findViewById(R.id.textViewTip);
        totalTextView = (TextView) findViewById(R.id.textViewTotal);


        roundingRadioGroup = (RadioGroup) findViewById(R.id.radioGroupRounding);
        roundNoneRadioButton = (RadioButton) findViewById(R.id.radioButtonNone);
        roundTipRadioButton = (RadioButton) findViewById(R.id.radioButtonTip);
        roundTotalRadioButton = (RadioButton) findViewById(R.id.radioButtonTotal);

        /*roundNoneRadioButton.setText("Radio Button 1");
        roundTipRadioButton.setText("Radio Button 2");
        roundTotalRadioButton.setText("Radio Button 3");*/

        /*
        roundNoneRadioButton.setOnClickListener(radioListener);
        roundTipRadioButton.setOnClickListener(radioListener);
        roundTotalRadioButton.setOnClickListener(radioListener);
        */

        //roundingRadioGroup.setOnCheckedChangeListener(this);

        perPersonLabel = (TextView) findViewById(R.id.textViewLabelPerPerson); //need to be able to hide this
        perPersonTextView = (TextView) findViewById(R.id.textViewPerPerson);

        Spinner splitSpinner = (Spinner) findViewById(R.id.spinnerSplitBill);
        splitSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // Implement your logic here
                setTitle("Selected: " + parent.getItemAtPosition(pos).toString());
                split = pos + 1;
                calculateAndDisplay();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //set array adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.split_bill_array, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        splitSpinner.setAdapter( adapter );




    }//end of protected void onCreate()


    public void calculateAndDisplay(){
        //get the bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;

        if( billAmountString.equals("") ){
            billAmount = 0;
        }
        else {
            billAmount = Float.parseFloat( billAmountString );
        }

        //get tip percent
        int progress = percentSeekBar.getProgress();
        tipPercent = (float) progress / 100;

        //calc tip and total
        float tipAmount = 0;
        float totalAmount = 0;


        if( rounding == ROUND_NONE ){
            tipAmount = billAmount * tipPercent;
            totalAmount = billAmount + tipAmount;
        }
        else if( rounding == ROUND_TIP ){
            tipAmount = StrictMath.round( billAmount * tipPercent );
            totalAmount = billAmount + tipAmount;
        }
        else if( rounding == ROUND_TOTAL ){
            float tipNotRounded = billAmount * tipPercent;
            totalAmount = StrictMath.round( billAmount + tipNotRounded );
            tipAmount = totalAmount - billAmount;
        }

        tipAmount = billAmount * tipPercent;
        totalAmount = billAmount + tipAmount;


        //calculate split amount and when to show/hide widget
        float splitAmount = 0;
        if( split == 1 ){
            perPersonLabel.setVisibility(View.GONE);
            perPersonTextView.setVisibility(View.GONE);
        }
        else {
            splitAmount = totalAmount / split;
            perPersonLabel.setVisibility(View.VISIBLE);
            perPersonTextView.setVisibility(View.VISIBLE);
        }

        //display results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText( currency.format(tipAmount) );
        totalTextView.setText( currency.format(totalAmount) );
        perPersonTextView.setText( currency.format(splitAmount) );

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText( percent.format(tipPercent) );
    }


    /* Event handler for RadioGroup */
    /*
    @Override
    public void onCheckedChanged( RadioGroup group, int checkedId ){
        switch( checkedId ){
            case R.id.radioButtonNone:
                rounding = ROUND_NONE;
                break;
            case R.id.radioButtonTip:
                rounding = ROUND_TIP;
                break;
            case R.id.radioButtonTotal:
                rounding = ROUND_TOTAL;
                break;
        }

        calculateAndDisplay();
    }
    */


    //listener function for radio button click
    private android.view.View.OnClickListener radioListener =
        new android.view.View.OnClickListener() {
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                // Implement your logic here
                setTitle("Selected: " + rb.getText());

                int rbIndex = roundingRadioGroup.indexOfChild(
                        findViewById(roundingRadioGroup.getCheckedRadioButtonId())
                );

                switch( rbIndex ){
                    case R.id.radioButtonNone:
                        rounding = ROUND_NONE;
                        break;
                    case R.id.radioButtonTip:
                        rounding = ROUND_TIP;
                        break;
                    case R.id.radioButtonTotal:
                        rounding = ROUND_TOTAL;
                        break;
                }

                calculateAndDisplay();
            }
    };
}
