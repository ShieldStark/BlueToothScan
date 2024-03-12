package com.example.bluetoothscan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity implements DataAdapter.OnItemLongClickListener,DataAdapter.OnItemClickListener {
    String TAG="!@# Info Activity";


    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    List<DataValue> originalDataList;
    List<DataValue> filteredDataList;
    private int selectedItemIndex = -1;
    private List<Integer> selectedItems;
    Button deleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        recyclerView = findViewById(R.id.recyclerView);
        originalDataList = loadDataFromFile();
        filteredDataList = new ArrayList<>(originalDataList);

        //dataAdapter = new DataAdapter(filteredDataList);
        dataAdapter = new DataAdapter(filteredDataList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
        selectedItems=new ArrayList<>();
        deleteAll=findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedItems();
            }
        });
        dataAdapter.setOnSelectionChangedListener(new DataAdapter.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(boolean hasSelection) {
                // Show or hide the delete all button based on whether there are selected items
                deleteAll.setVisibility(hasSelection ? View.VISIBLE : View.GONE);
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
            //query = query.toLowerCase().trim();
            Log.d(TAG,"Query: "+query);
            for (DataValue data : originalDataList) {
                if (data.getSerialNumber().startsWith(query)) {
                    Log.d(TAG,"Data updated");
                    filteredDataList.add(data);
                }
            }
        }
        //dataAdapter.clear();
        //dataAdapter.addAll(filteredDataList);
        dataAdapter.notifyDataSetChanged();
    }
    private void deleteData(DataValue data) {
        try {
            FileInputStream fis = openFileInput("saved_data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                String serialNumber = jsonObject.getString("serialNumber");
                if (!(serialNumber.equals(data.getSerialNumber())&& data.getTime().equals(jsonObject.getString("time")))) {
                    stringBuilder.append(line).append("\n");
                }
            }
            fis.close();

            // Write the updated data back to the file
            FileOutputStream fos = openFileOutput("saved_data.txt", Context.MODE_PRIVATE);
            fos.write(stringBuilder.toString().getBytes());
            fos.close();

            // Remove the data from the original and filtered lists
            originalDataList.remove(data);
            filteredDataList.remove(data);

            // Notify the adapter of the change
            dataAdapter.notifyDataSetChanged();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "on onItemLongClick called");
        dataAdapter.toggleSelection(position);

        // Update the selected items in the adapter
        dataAdapter.updateSelectedItems(dataAdapter.getSelectedItems());
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "on item click called  "+selectedItems.size());
        if (!selectedItems.isEmpty()) {
            Log.d(TAG, "on item click called not empty");
            if (selectedItems.contains(position)) {
                // Deselect the item
                selectedItems.remove(Integer.valueOf(position));
                Log.d(TAG, "on item click called not empty: GONE "+selectedItems.size());
                if (selectedItems.size()==0){
                    deleteAll.setVisibility(View.GONE);
                }
            } else {
                // Select the item
                Log.d(TAG, "on item click called not empty: VISIBLE "+selectedItems.size());
                selectedItems.add(position);
                if (selectedItems.size()>0){
                    deleteAll.setVisibility(View.VISIBLE);
                }

            }
            dataAdapter.setSelectedItem(selectedItems);
        }else {
            Log.d(TAG, "on item click called");
            DataValue selectedData = filteredDataList.get(position);
            Intent intent = new Intent(InfoActivity.this, RestoreData.class);
            intent.putExtra("selectedData", selectedData);
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteClick(int position) {
        Log.d(TAG,"on Delete click called");
        deleteData(filteredDataList.get(position));

    }
    private void deleteSelectedItems() {
        List<DataValue> selectedData = new ArrayList<>();
        for (int position : dataAdapter.getSelectedItems()) {
            selectedData.add(filteredDataList.get(position));
        }

        for (DataValue data : selectedData) {
            deleteData(data);
        }
        dataAdapter.clearSelectedItems();
    }
}
