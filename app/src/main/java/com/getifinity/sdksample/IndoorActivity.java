package com.getifinity.sdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.getifinity.sdk.generated.IFArea;
import com.getifinity.sdk.generated.IFContent;
import com.getifinity.sdk.generated.IFFloorPlan;
import com.getifinity.sdk.generated.IFTimePush;
import com.getifinity.sdk.generated.IFVenue;
import com.getifinity.sdk.listeners.OnEnterAreaListener;
import com.getifinity.sdk.listeners.OnFloorChangedListener;
import com.getifinity.sdk.listeners.OnUserPositionChangedListener;
import com.getifinity.sdk.listeners.OnVenueContentListener;
import com.getifinity.sdk.managers.DataManager;
import com.getifinity.sdk.managers.IfinityBluetoothManager;
import com.getifinity.sdk.managers.IndoorLocationManager;
import com.google.android.gms.maps.model.LatLng;

public class IndoorActivity extends AppCompatActivity {

    IfinityBluetoothManager mBluetoothManager;
    IndoorLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothManager = IfinityBluetoothManager.getInstance();
        mBluetoothManager.startManager(getBaseContext());
        mBluetoothManager.addOnVenueContentListener(new OnVenueContentListener() {
            @Override
            public void venueContent(IFVenue ifVenue, IFContent ifContent, IFTimePush ifTimePush) {
                Log.e("TAG", "venue content " + ifVenue.getName() + " " + ifContent.getName());
            }
        }, DataManager.getInstance().fetchVenuesFromCache(getBaseContext()));

        mLocationManager = IndoorLocationManager.getInstance();
        mLocationManager.startManager(getBaseContext());

        mLocationManager.addOnEnterAreaListener(new OnEnterAreaListener() {
            @Override
            public void areaEntered(IFArea ifArea, IFContent ifContent) {
                Log.e("TAG", "area entered " + ifArea.getName());
                if (ifArea.content != null) {
                    Log.e("TAG", "area content " + ifArea.content.getName());
                }
            }

            @Override
            public void areaLeft(IFArea ifArea, IFContent ifContent) {
                Log.e("TAG", "area left");
            }
        });

        mLocationManager.addOnUserPositionChanged(new OnUserPositionChangedListener() {
            @Override
            public void positionChanged(LatLng latLng, double v) {
                Log.e("TAG", "position " + latLng.toString());
            }
        });

        mBluetoothManager.addOnFloorChangedListener(new OnFloorChangedListener() {
            @Override
            public void floorChanged(IFVenue ifVenue, IFFloorPlan ifFloorPlan, String s) {
                Log.e("TAG", "venue " + ifVenue.getVenueId() + " floor " + ifFloorPlan.getFloorId());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothManager.stopManager();
        mLocationManager.stopManager();
    }
}
