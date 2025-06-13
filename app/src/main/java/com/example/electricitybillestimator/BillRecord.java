package com.example.electricitybillestimator;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BillRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String month;
    public int units;
    public int rebatePercent;
    public double totalCharges;
    public double finalCost;
}

