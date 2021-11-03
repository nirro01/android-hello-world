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

import com.jackandphantom.joystickview.JoyStickView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_DEVICE_SELECTED = "android.bluetooth.devicepicker.action.DEVICE_SELECTED";
    public static final String ACTION_LAUNCH = "android.bluetooth.devicepicker.action.LAUNCH";
    private BluetoothDevice device;
    private BluetoothSocket bluetoothSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JoyStickView joyStickView = findViewById(R.id.joystick);
        joyStickView.setOnMoveListener((angle, strength) -> {
            Log.i("Nir", "Joystick angle + " + angle + ", strength: " + strength);
            if(bluetoothSocket != null && bluetoothSocket.isConnected()) {
                try {
                    bluetoothSocket.getOutputStream().write("Hello".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    Log.e("Nir", e.getMessage());
                }
            }
        });
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                TextView deviceNameTextView = findViewById(R.id.deviceNameText);
                deviceNameTextView.setText(device.getName());
                try {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    Log.e("Nir", e.getMessage());
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_DEVICE_SELECTED));
        startActivity(new Intent(ACTION_LAUNCH)); // launch bluetooth device picker
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e("Nir", e.getMessage());
        }
    }
}
