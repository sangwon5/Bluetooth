package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter mBlueAdapter;
    Button onBtn;
    // 페어링 목록 출력
    TextView pairedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 걍 모름 텍스트 보여줌
        pairedlist = findViewById(R.id.pairedlist);
        onBtn = findViewById(R.id.onBtn);
        // 어댑터
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();





        // 버튼 누를시 bluetooth_searcah 로 이동
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  블루투스 꺼져있을시 활성화

                Intent bluetooth_search = new Intent(getApplicationContext(), bluetooth_search.class);
                startActivity(bluetooth_search);
            }
        });



    }
}