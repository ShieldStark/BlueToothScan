package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceInfoActivity extends AppCompatActivity {
    TextView serialNumber;
    TextView time;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        serialNumber=findViewById(R.id.serialNumberData);
        time=findViewById(R.id.timeData);
        save=findViewById(R.id.save);
        serialNumber.setText("123");
        time.setText(getCurrentTime());
        String data="Hello Guys";
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serialNumber", serialNumber);
                    jsonObject.put("data", data);
                    jsonObject.put("time", getCurrentTime());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Serialize data object
                String serializedData = jsonObject.toString();
                // Write serialized data to file
                saveDataToFile(serializedData);

            }
        });
    }
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void saveDataToFile(String data) {
        try {
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }

    private String fetchDataFromDeviceOrServer() {
        // Implement data fetching logic from device or server
        return "Sample data from device or server";
    }
}