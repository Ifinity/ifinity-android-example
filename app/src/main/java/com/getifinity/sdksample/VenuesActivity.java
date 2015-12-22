package com.getifinity.sdksample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.getifinity.sdk.generated.IFArea;
import com.getifinity.sdk.generated.IFBeacon;
import com.getifinity.sdk.generated.IFFloorPlan;
import com.getifinity.sdk.generated.IFVenue;
import com.getifinity.sdk.managers.DataManager;

import java.util.List;

public class VenuesActivity extends AppCompatActivity {

    ListView listView;
    List<IFVenue> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);
        listView = (ListView) findViewById(R.id.listView);
        ((Button)findViewById(R.id.btIndoor)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VenuesActivity.this, IndoorActivity.class));
            }
        });
        list = DataManager.getInstance().fetchVenuesFromCache(getBaseContext());
        listView.setAdapter(new MyAdapter(getBaseContext()));

    }

    class MyAdapter extends BaseAdapter{

        LayoutInflater layoutInflator;

        public MyAdapter(Context context){
            layoutInflator  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflator.inflate(R.layout.venue_item, null);
            IFVenue venue = list.get(position);
            ((TextView)view.findViewById(R.id.venueName)).setText(venue.getName());
            view.setOnClickListener(new OnClick(position));
            return view;
        }

        class OnClick implements View.OnClickListener{
            final int mPosition;
            public OnClick(int position){
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                List<IFArea> areaList = DataManager.getInstance().fetchAreasFromCacheForVenueId(VenuesActivity.this, list.get(mPosition).getVenueId());
                List<IFFloorPlan> floorPlanList = DataManager.getInstance().fetchFloorsFromCacheForVenueId(VenuesActivity.this, list.get(mPosition).getVenueId());

                for(IFArea a : areaList){
                    Log.e("area", a.getName());
                }

                for(IFFloorPlan f :floorPlanList){
                    Log.e("floor", f.getFloorId() + "");
                    List<IFBeacon> beaconList = DataManager.getInstance().fetchBeaconsForFloor(VenuesActivity.this,f.getFloorId());
                    for(IFBeacon b : beaconList){
                        Log.e("beacon", b.getName());
                    }
                }
            }
        }
    }
}
