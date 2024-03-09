package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceInfoActivity extends AppCompatActivity {
    String TAG="!@#DeviceInfoActivity";
    TextView serialNumber;
    TextView time;
    Button save,restore;
    ProgressBar progressBar;
    String serializedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        serialNumber=findViewById(R.id.serialNumberData);
        time=findViewById(R.id.timeData);
        save=findViewById(R.id.save);
        restore=findViewById(R.id.restore);
        progressBar=findViewById(R.id.progressBar);
        serialNumber.setText("123");
        time.setText(getCurrentTime());
        String data="Hello Guys";

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick Called");
                JSONObject jsonObject = new JSONObject();
                try {
                    Log.d(TAG,"TRY Called");
                    jsonObject.put("serialNumber", "123");
                    jsonObject.put("data", data);
                    jsonObject.put("time", getCurrentTime());
                } catch (JSONException e) {
                    Log.d(TAG,"Exception Called"+e.getMessage());
                    e.printStackTrace();
                }
                // Serialize data object
                serializedData = jsonObject.toString();
                Log.d(TAG,"onClick Called: "+serializedData);
                // Write serialized data to file
                //saveDataToFile(serializedData);
                new SaveDataTask().execute();
            }
        });
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeviceInfoActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void saveDataToFile(String data) {
        try {
            Log.d(TAG,"saveDataToFile TRY Called");
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d(TAG,"SaveDataToFile exception Called"+e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }

    private String fetchDataFromDeviceOrServer() {
        // Implement data fetching logic from device or server
        return "Sample data from device or server";
    }
    private class SaveDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... params) {
            // Simulate a time delay (in milliseconds) during saving
            try {
                Thread.sleep(2000); // 2 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try (FileOutputStream fos = openFileOutput("saved_data.txt", Context.MODE_PRIVATE)) {
                Log.d(TAG, "saveDataToFile TRY Called");
                fos.write(serializedData.getBytes());
                fos.write("\n".getBytes());
                fos.close();
                //Toast.makeText(DeviceInfoActivity.this, "Data saved to file", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.d(TAG, "SaveDataToFile exception Called" + e.getMessage());
                e.printStackTrace();
                //Toast.makeText(DeviceInfoActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
        }
    }

}