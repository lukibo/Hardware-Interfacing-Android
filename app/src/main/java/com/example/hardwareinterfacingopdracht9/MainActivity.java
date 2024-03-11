//Bron https://www.youtube.com/watch?v=TLXpDY1pItQ en Viktor
package com.example.hardwareinterfacingopdracht9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //String uit tutorial
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mac-adres van bleutooth apparaat
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
        }

        //Bluetooth apparaat
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice("98:D3:61:FD:35:1E");

        //Button op het scherm
        Button buttonStart = findViewById(R.id.button);
        //Functie voor wat er gebeurt als er op de knop wordt gedrukt
        buttonStart.setOnClickListener(ex -> {
            //Aanmaken socket
            BluetoothSocket bluetoothSocket = null;
            int counter = 0;
            do {
                try {
                    bluetoothSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!bluetoothSocket.isConnected() && counter < 3);

            try (OutputStream outputStream = bluetoothSocket.getOutputStream()) {
                //Stuur waarde 49 naar arduino, in tutorial video staat 48 maar die werkt niet.
                outputStream.write(49);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Sluit connectie
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}