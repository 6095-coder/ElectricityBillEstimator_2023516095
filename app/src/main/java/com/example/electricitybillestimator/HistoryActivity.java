package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;


public class HistoryActivity extends AppCompatActivity {

    ListView listViewHistory;
    List<BillRecord> billList;
    List<String> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.listViewHistory);

        // Get data from Room database
        AppDatabase db = AppDatabase.getInstance(this);
        billList = db.billRecordDao().getAll();

        // Prepare data for ListView (show month and final cost)
        displayList = new ArrayList<>();
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        for (BillRecord record : billList) {
            String formattedCost = formatter.format(record.finalCost);
            displayList.add(record.month + " : RM " + formattedCost);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);

        listViewHistory.setAdapter(adapter);

        // Click to view detail
        listViewHistory.setOnItemClickListener((parent, view, position, id) -> {
            BillRecord selectedRecord = billList.get(position);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("month", selectedRecord.month);
            intent.putExtra("units", selectedRecord.units);
            intent.putExtra("rebate", selectedRecord.rebatePercent);
            intent.putExtra("total", selectedRecord.totalCharges);
            intent.putExtra("finalCost", selectedRecord.finalCost);
            startActivity(intent);
        });
    }
}
