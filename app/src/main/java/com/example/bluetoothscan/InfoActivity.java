package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        recyclerList=loadDataFromFile();
//        recyclerList.add(new DataValue("1","Server","123"));
//        recyclerList.add(new DataValue("2","Device","456"));
//        recyclerList.add(new DataValue("3","Server","789"));
        dataAdapter=new DataAdapter(recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
    }
    private List<DataValue> loadDataFromFile() {
        List<DataValue> dataList = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("saved_data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                DataValue data = new DataValue();
                data.setSerialNumber(jsonObject.getString("serialNumber"));
                data.setDataType(jsonObject.getString("data"));
                data.setTime(jsonObject.getString("time"));
                // Set other attributes accordingly
                dataList.add(data);
            }
            fis.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

}