package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RestoreData extends AppCompatActivity {
    String TAG="!@#DeviceInfoActivity";
    TextView serialNumber;
    TextView time;
    Button save,restore;
    ProgressBar progressBar;
    DataValue serializedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_data);
        serialNumber=findViewById(R.id.serialNumberRestoreData);
        time=findViewById(R.id.timeRestoreData);
        restore=findViewById(R.id.restore);
        Intent intent=getIntent();
        if(intent!=null){
            DataValue dataValue=(DataValue) intent.getSerializableExtra("selectedData");
            if(dataValue!=null){
                serialNumber.setText(dataValue.getSerialNumber());
                time.setText(dataValue.getTime());
            }
        }

    }
}