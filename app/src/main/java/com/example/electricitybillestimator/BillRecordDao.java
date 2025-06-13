package com.example.electricitybillestimator;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BillRecordDao {
    @Insert
    void insert(BillRecord record);

    @Query("SELECT * FROM BillRecord ORDER BY id DESC")
    List<BillRecord> getAll();
}
