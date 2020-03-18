package com.example.androidbluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView btStatusTv, devicesTv;
    ImageView btIv;
    Button onBtn, offBtn, discBtn, pairBtn;

    BluetoothAdapter btAdap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btStatusTv = findViewById(R.id.btStatusTv);
        devicesTv = findViewById(R.id.devicesTv);

        btIv = findViewById(R.id.btIv);

        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        discBtn = findViewById(R.id.discBtn);
        pairBtn = findViewById(R.id.pairBtn);

        btAdap = BluetoothAdapter.getDefaultAdapter();

        if(btAdap == null) {
            btStatusTv.setText("Bluetooth Driver Unavailable.");
        }
        else {
            btStatusTv.setText("Bluetooth Driver Available.");
        }

        if(btAdap.isEnabled()) {
            btIv.setImageResource(R.drawable.ic_action_on);
        }
        else {
            btIv.setImageResource(R.drawable.ic_action_off);
        }

        // SOMETHING IS WRONG
        onBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!btAdap.isEnabled()) {
                    showToast("Turning On...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
                else {
                    showToast("Bluetooth enabled already!");
                    btIv.setImageResource(R.drawable.ic_action_on);
                }
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(btAdap.isEnabled()) {
                    btAdap.disable();
                    showToast("Turning off...");
                    btIv.setImageResource(R.drawable.ic_action_off);
                    devicesTv.setText(null);
                }
                else {
                    showToast("Bluetooth disabled already!");
                    btIv.setImageResource(R.drawable.ic_action_off);
                }
            }
        });

        discBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(btAdap.isEnabled()) {
                    if(!btAdap.cancelDiscovery()) {
                        showToast("Making device discoverable...");
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(intent,REQUEST_DISCOVER_BT);
                    }
                    else {
                        showToast("Device discoverable already!");
                    }
                }
                else {
                    showToast("Enable bluetooth first!");
                }

            }
        });

        pairBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(btAdap.isEnabled()) {
                    devicesTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = btAdap.getBondedDevices();
                    for(BluetoothDevice device:devices) {
                        devicesTv.append("\nDevice: " + device.getName() + ", Address- " + device);
                    }
                }
                else {
                    showToast("Enable bluetooth first!");
                }
            }
        });

    }

    // ISSUES IN THIS FUNCTION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    btIv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth enabled.");
                }
                else {
                    showToast("Couldn't enable bluetooth.");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
