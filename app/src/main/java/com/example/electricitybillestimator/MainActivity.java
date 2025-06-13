package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    Spinner spinnerMonth;
    EditText editTextUnits;
    SeekBar seekBarRebate;
    TextView textViewRebateValue, textViewTotalCharges, textViewFinalCost, textViewError;
    Button buttonCalculate;
    int rebatePercent = 0; // initial rebate %

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Link variables to layout
        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        textViewRebateValue = findViewById(R.id.textViewRebateValue);
        textViewTotalCharges = findViewById(R.id.textViewTotalCharges);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);
        textViewError = findViewById(R.id.textViewError);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        TextView tvMonthLabel = findViewById(R.id.tvMonthLabel);
        TextView tvUnitsLabel = findViewById(R.id.tvUnitsLabel);
        TextView tvRebateLabel = findViewById(R.id.tvRebateLabel);

        tvMonthLabel.setText(android.text.Html.fromHtml("Month: <font color='#D32F2F'>*</font>"));
        tvUnitsLabel.setText(android.text.Html.fromHtml("Electricity Units Used (kWh): <font color='#D32F2F'>*</font>"));
        tvRebateLabel.setText(android.text.Html.fromHtml("Rebate Percentage: <font color='#D32F2F'>*</font>"));

        // Spinner - Set month values
        String[] months = {"Select Month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // SeekBar - Show value
        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rebatePercent = progress;
                textViewRebateValue.setText(progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // Button - Calculate logic (to be added next)
        buttonCalculate.setOnClickListener(view -> {
            // Clear previous error message
            textViewError.setText("");
            textViewError.setVisibility(TextView.GONE);

            // 1. Get and validate user input
            String month = spinnerMonth.getSelectedItem().toString();
            String unitsStr = editTextUnits.getText().toString().trim();

            // Spinner validation
            if (month.equals("Select Month")) {
                textViewError.setText("Please select a month.");
                textViewError.setVisibility(TextView.VISIBLE);
                return;
            }

            // Unit validation
            if (unitsStr.isEmpty()) {
                textViewError.setText("Please enter the number of units used.");
                textViewError.setVisibility(TextView.VISIBLE);
                return;
            }

            int units;
            try {
                units = Integer.parseInt(unitsStr);
                if (units <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                textViewError.setText("Invalid input for units. Enter a positive number.");
                textViewError.setVisibility(TextView.VISIBLE);
                return;
            }

            // 2. Calculate total charges using block tariff
            double total = 0;
            int remaining = units;

            if (remaining > 0) {
                int block = Math.min(remaining, 200);
                total += block * 0.218;
                remaining -= block;
            }
            if (remaining > 0) {
                int block = Math.min(remaining, 100);
                total += block * 0.334;
                remaining -= block;
            }
            if (remaining > 0) {
                int block = Math.min(remaining, 300);
                total += block * 0.516;
                remaining -= block;
            }
            if (remaining > 0) {
                total += remaining * 0.546;
            }

            // 3. Calculate final cost after rebate
            double finalCost = total - (total * rebatePercent / 100.0);

            // 4. Display output (format to 2 decimal places)
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);

            textViewTotalCharges.setText("Total Charges: RM " + nf.format(total));
            textViewFinalCost.setText("Final Cost after Rebate: RM " + nf.format(finalCost));

            // Save to database
            AppDatabase db = AppDatabase.getInstance(MainActivity.this);
            BillRecord record = new BillRecord();
            record.month = month;
            record.units = units;
            record.rebatePercent = rebatePercent;
            record.totalCharges = total;
            record.finalCost = finalCost;
            db.billRecordDao().insert(record);

            // Show feedback to user
            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

        });

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
