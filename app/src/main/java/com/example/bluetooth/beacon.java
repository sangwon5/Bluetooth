package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Manifest;

import com.estimote.sdk.SystemRequirementsChecker;

import static org.altbeacon.beacon.BeaconParser.ALTBEACON_LAYOUT; // m:2-3=beac, i:4-19,i:20-21,i:22-23,p:24-24,d:25-25

public class beacon extends AppCompatActivity implements BeaconConsumer {

    private String TAG = MainActivity.class.getSimpleName();
    private BeaconManager beaconManager;
    private Region beaconRegion = null;
    // 감지된 비콘들을 임시로 담을 리스트
    private List<Beacon> beaconList = new ArrayList<>();
    TextView textView;

    // 탐색 버튼
    private Button beacon_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        textView = findViewById(R.id.tvId);


        // 비콘 탐지를 위한 비콘 매니저 객체 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // setBeaconLayout 설정 ALTBEACON_LAYOUT
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(ALTBEACON_LAYOUT));

        // 비콘 탐지 시작 (where actual service starts)
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect starts");

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size() > 0){
                    beaconList.clear();
                    for(Beacon beacon : beacons){
                        beaconList.add(beacon);
                    }
                }
            }
        });

        try{
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch(RemoteException e){ }
    }

    public void onClicked(View view){
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        public void handleMessage(Message msg){
            textView.setText("");

        for(Beacon beacon : beaconList){
            textView.append("ID : " + beacon.getId2() + "// Distance" + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
        }
        handler.sendEmptyMessageDelayed(0, 1000);
        }


    };
}
