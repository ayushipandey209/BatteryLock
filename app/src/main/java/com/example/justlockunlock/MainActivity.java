package com.example.justlockunlock;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "dpc_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "started");
        super.onCreate(savedInstanceState);
        makeForeground();
    }
    private void makeForeground() {
        Log.d(TAG, "makeForeground: ############### creating");
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        startForegroundService(serviceIntent);
    }
}
