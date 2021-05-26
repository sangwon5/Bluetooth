package com.example.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.estimote.sdk.SystemRequirementsChecker;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

public class beacon extends AppCompatActivity implements BeaconConsumer {

    private final String TAG = MainActivity.class.getSimpleName();
    private BeaconManager beaconManager;


    ListView list_beacon;
    TextView textView;
    SimpleAdapter beacon_adapter;
    // 감지된 비콘들을 임시로 담을 리스트
    private final List<Beacon> beaconList = new ArrayList<>();
    List<Map<String,String>> beaconlist;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        list_beacon= findViewById(R.id.list_beacon);
        textView = findViewById(R.id.textView);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconlist = new ArrayList<>();
        beacon_adapter = new SimpleAdapter(this, beaconlist, android.R.layout.simple_list_item_2, new String[]{"UUID","distance"}, new int[]{android.R.id.text1, android.R.id.text2});
        list_beacon.setAdapter(beacon_adapter);

        // 비콘 탐지를 위한 비콘 매니저 객체 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // setBeaconLayout 설정 Alt beacon_LAYOUT
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지 시작 (where actual service starts)
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect starts");

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addRangeNotifier((beacons, region) -> {
            textView.setText("비콘 찾음!");
            if(beacons.size() > 0){
                beaconList.clear();
                beaconList.addAll(beacons);
            }
        });

        try{
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch(RemoteException ignored){ }
    }

    // 버튼 누를시 탐색된 비콘의 UUID, Distance 출력
    public void onClicked(View view){

        beaconlist.clear();
        for (Beacon beacon : beaconList) {
            Map map = new HashMap();
            map.put("UUID", ("UUID : " + beacon.getId1())); // beacon UUID
            map.put("distance",("Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + " m"));
            beaconlist.add(map);
            beacon_adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

}
