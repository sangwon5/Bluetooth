package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class bluetooth_search extends AppCompatActivity {


    // 기기 검색 때 사용 예정
    ListView listpaired;

    SimpleAdapter adapterDevice;

    // 페어링 목록 출력
    TextView pairedlist;
    // 어댑터
    BluetoothAdapter mBlueAdapter;

    // 페어링된 목록 출력
    Button paired_list;

    private static final int REQUEST_ENABLE_BT = 1;

    List<Map<String,String>> dataPaired;
    List<BluetoothDevice> bluetoothDevices;

    int selectDevice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search);

        bluetoothDevices = new ArrayList<>();
        //선택된 디바이스 없음
        selectDevice = -1;
        //어댑터
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // 출력 바뀔 (예정)
        paired_list = findViewById(R.id.paired_list);

        // 상태 텍스트
        pairedlist = findViewById(R.id.pairedlist);

        listpaired = findViewById(R.id.listpaired);

        // 페어링된 기기 어댑터
        dataPaired = new ArrayList<>();
        adapterDevice = new SimpleAdapter(this, dataPaired, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listpaired.setAdapter(adapterDevice);

        if (!mBlueAdapter.isEnabled()) {

            Toast.makeText(getApplicationContext(), "Bluetooth is off", Toast.LENGTH_SHORT).show();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }


        // 페어링된 기기 목록
        paired_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedlist.setText("페어링된 기기");
                Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();

                dataPaired.clear();
                for(BluetoothDevice device: devices){
                    Map map = new HashMap();
                    map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                    map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                    dataPaired.add(map);

                    // 리스트 갱신
                    adapterDevice.notifyDataSetChanged();
                }
            }
        });


        // 등록된 기기 페어링 ?
        listpaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bluetoothDevices.get(position);
                try {
                    //선택한 디바이스 페어링 요청
                    Method method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                    selectDevice = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }



}