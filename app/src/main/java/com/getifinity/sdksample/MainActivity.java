package com.getifinity.sdksample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.getifinity.sdk.listeners.OnAuthenticateUserFinished;
import com.getifinity.sdk.listeners.OnQuerySuccess;
import com.getifinity.sdk.managers.DataManager;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private static String sClientId = "XXXXX";
    private static String sClientSecret = "YYYYY";

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.btAuth)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizeAndLoad();
            }
        });

    }

    private void authorizeAndLoad(){

        DataManager.getInstance().authenticateWithClientID(sClientId, sClientSecret, new OnAuthenticateUserFinished() {
            @Override
            public void onOAuthSuccess() {
                Log.e("TAG", "successfull auth");

                // Load data on successfull authentication

                DataManager.getInstance().loadDataForLocationWithSuccess(MainActivity.this, new LatLng(52.0f, 24.0f), new OnQuerySuccess() {
                    @Override
                    public void onQuerySuccess() {
                        Log.e("TAG", "successfull load");

                        // Loading venues successfull

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MainActivity.this, VenuesActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onQueryError(String s, int i) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TAG", "error with parsing data");
                            }
                        });
                    }
                });
            }

            @Override
            public void onOAuthFailure(String s) {
                Log.e("TAG", "Authorization failed");
            }
        });
    }
}
