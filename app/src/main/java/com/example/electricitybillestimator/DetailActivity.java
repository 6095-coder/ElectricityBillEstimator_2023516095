package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    TextView textViewMonth, textViewUnits, textViewRebate, textViewTotal, textViewFinal;
    Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewMonth = findViewById(R.id.textViewMonth);
        textViewUnits = findViewById(R.id.textViewUnits);
        textViewRebate = findViewById(R.id.textViewRebate);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewFinal = findViewById(R.id.textViewFinal);
        buttonBack = findViewById(R.id.buttonBack);

        // Get data from Intent
        String month = getIntent().getStringExtra("month");
        int units = getIntent().getIntExtra("units", 0);
        int rebate = getIntent().getIntExtra("rebate", 0);
        double total = getIntent().getDoubleExtra("total", 0);
        double finalCost = getIntent().getDoubleExtra("finalCost", 0);

        // Use NumberFormat to add commas
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String formattedTotal = formatter.format(total);
        String formattedFinalCost = formatter.format(finalCost);

        // Set data to TextViews
        textViewMonth.setText("Month: " + month);
        textViewUnits.setText("Units Used: " + units + " kWh");
        textViewRebate.setText("Rebate: " + rebate + "%");
        textViewTotal.setText("Total Charges: RM " + formattedTotal);
        textViewFinal.setText("Final Cost after Rebate: RM " + formattedFinalCost);

        buttonBack.setOnClickListener(view -> finish());
    }
}
