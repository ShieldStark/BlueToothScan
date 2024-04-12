package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private ProgressBar timerProgressBar;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private final int MAX_PROGRESS = 100;
    private final int TOTAL_DURATION = 180000; // 3 minutes
    private final int INTERVAL = 1000; // 9 seconds
    private final int INCREMENT_AMOUNT = 1;
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
        timerProgressBar=findViewById(R.id.rectangular_progress_bar);
        timerTextView=findViewById(R.id.progressText);
        startTimer();
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
            try (FileOutputStream fos = openFileOutput("saved_data.txt",Context.MODE_APPEND| Context.MODE_PRIVATE)) {
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
//    private void startTimer() {
//        countDownTimer = new CountDownTimer(TOTAL_DURATION, INTERVAL) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                int currentProgress = timerProgressBar.getProgress();
//                if (currentProgress < MAX_PROGRESS) {
//                    int newProgress = currentProgress + INCREMENT_AMOUNT;
//                    timerProgressBar.setProgress(newProgress);
//                }
//                updateTimerText(millisUntilFinished);
//            }
//
//            @Override
//            public void onFinish() {
//                timerProgressBar.setProgress(MAX_PROGRESS);
//                timerTextView.setText("00:00");
//            }
//        };
//        countDownTimer.start();
//    }
private void startTimer() {
    countDownTimer = new CountDownTimer(TOTAL_DURATION, INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            // Calculate remaining seconds
            long remainingSeconds = millisUntilFinished / 1000;

            // Update time every second
            updateTimerText(remainingSeconds * 1000); // Pass milliseconds

            // Update progress every 9 seconds by 5
            if (remainingSeconds % 9 == 0) {
                int currentProgress = timerProgressBar.getProgress();
                int newProgress = currentProgress + 5;
                if (newProgress <= MAX_PROGRESS) {
                    timerProgressBar.setProgress(newProgress);
                }
            }
        }

        @Override
        public void onFinish() {
            // Ensure progress reaches maximum at the end
            timerProgressBar.setProgress(MAX_PROGRESS);
            timerTextView.setText("00:00");
        }
    };
    countDownTimer.start();
}



    private void updateTimerText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}