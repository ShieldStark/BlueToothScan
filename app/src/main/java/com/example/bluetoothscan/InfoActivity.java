package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class InfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    List<DataValue> recyclerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerList=new ArrayList<>();
        recyclerList.add(new DataValue("1","Server","123"));
        recyclerList.add(new DataValue("2","Device","456"));
        recyclerList.add(new DataValue("3","Server","789"));
        dataAdapter=new DataAdapter(recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
    }
}