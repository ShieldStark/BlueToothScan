package com.example.bluetoothscan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView deviceListView;
    private LoadingDialog loadingDialog;
    private Handler mLoadingHandler;
    private boolean allDevicesDiscovered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceListView = findViewById(R.id.deviceListView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        deviceListView.setAdapter(arrayAdapter);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle("Custom Loading");
        loadingDialog.setMessage("Please Wait...");

        mLoadingHandler = new Handler();

        // Check if Bluetooth LE is supported on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Bluetooth LE is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check if Bluetooth permissions are granted
        checkBluetoothPermissions();
    }

    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.NEARBY_WIFI_DEVICES,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.BLUETOOTH},
                        REQUEST_PERMISSIONS);
            } else {
                initializeBluetooth();
            }
        } else {
            initializeBluetooth();
        }
    }

    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            Toast.makeText(this, "Bluetooth LE Scanner is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Start BLE scanning
        startBleScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean permissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }
            if (grantResults.length>6) {
                initializeBluetooth();
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startBleScan() {
        if (bluetoothLeScanner != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingDialog();
                }
            });
            bluetoothLeScanner.startScan(scanCallback);
            mLoadingHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopBleScanAndShowNoDeviceFound();
                }
            },30000);
        }
    }
    private void stopBleScanAndShowNoDeviceFound() {
        stopBleScan();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                if (deviceList.isEmpty()) {
                    showNoDeviceFoundMessage();
                }
            }
        });
    }
    private void showNoDeviceFoundMessage() {
        // Show a message indicating no devices found
        Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
    }
    private void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }
    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void stopBleScan() {
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            String deviceInfo = deviceName + "\n" + deviceAddress;
            if (!deviceList.contains(deviceInfo)) {
                deviceList.add(deviceInfo);
                arrayAdapter.notifyDataSetChanged();
            }
            //checkAllDevicesDiscovered();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            // Handle scan failure
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            // Handle batch scan results
        }
    };
    private void stopBleScanAndDismissLoading() {
        // Stop BLE scanning
        stopBleScan();

        // Dismiss the loading dialog if all devices have been discovered
        if (allDevicesDiscovered && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    void showProgressBar() {
        loadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        // Stop BLE scanning when the activity is destroyed
        stopBleScan();
    }
}
