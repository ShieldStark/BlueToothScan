package com.example.bluetoothscan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothscan.DataAdapter;
import com.example.bluetoothscan.DataValue;
import com.example.bluetoothscan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    String TAG="!@# Info Activity";

    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    List<DataValue> originalDataList;
    List<DataValue> filteredDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        recyclerView = findViewById(R.id.recyclerView);
        originalDataList = loadDataFromFile();
        filteredDataList = new ArrayList<>(originalDataList);

        dataAdapter = new DataAdapter(filteredDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG,"on item click called");
                DataValue selectedData = filteredDataList.get(position);
                Intent intent = new Intent(InfoActivity.this, RestoreData.class);
                intent.putExtra("selectedData", selectedData);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    private void filterData(String query) {
        Log.d(TAG,"Filter Data");
        filteredDataList.clear();
        if (TextUtils.isEmpty(query)) {
            Log.d(TAG,"Filter Data Empty query");
            filteredDataList.addAll(originalDataList);
        } else {
            query = query.toLowerCase().trim();
            Log.d(TAG,"Query: "+query);
            for (DataValue data : originalDataList) {
                if (data.getSerialNumber().toLowerCase().equalsIgnoreCase(query)) {
                    Log.d(TAG,"Data updated");
                    filteredDataList.add(data);
                }
            }
        }
        //dataAdapter.clear();
        //dataAdapter.addAll(filteredDataList);
        dataAdapter.notifyDataSetChanged();
    }

}
