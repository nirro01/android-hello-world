package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_DEVICE_SELECTED = "android.bluetooth.devicepicker.action.DEVICE_SELECTED";
    public static final String ACTION_LAUNCH = "android.bluetooth.devicepicker.action.LAUNCH";
    private BluetoothDevice device;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                TextView deviceNameTextView = findViewById(R.id.deviceNameText);
                deviceNameTextView.setText(device.getName());
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_DEVICE_SELECTED));
        startActivity(new Intent(ACTION_LAUNCH)); // launch bluetooth device picker
    }

}
