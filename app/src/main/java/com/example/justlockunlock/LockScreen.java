package com.example.justlockunlock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreen extends AppCompatActivity {
    private static final String PREFS_NAME = "LockScreenPrefs";
    private static final String KEY_IS_RUNNING = "isRunning";
    private boolean isCharging;
    private final String TAG = "dpc_tag";
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private byte[] token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLockScreenRunning(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        TextView chargingStatusTextView = findViewById(R.id.lockStatusTextView);

        isCharging = getIntent().getBooleanExtra("isCharging", false);

        if (isCharging) {
            chargingStatusTextView.setText("Device is Locked.");
        } else {
            chargingStatusTextView.setText("Device is unlocked.");
        }

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);

        lockDevice(isCharging);


    }

    private void lockDevice(boolean status) {
        Log.d(TAG, "device locked");

        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setLockTaskPackages(
                    componentName,new String[]{componentName.getPackageName()});
            devicePolicyManager.addUserRestriction(componentName, UserManager.DISALLOW_CREATE_WINDOWS);
            if (status) {
                Log.d(TAG, "lockDevice: #######status########   "  + status);
                startLockTask();
                Log.d(TAG, "run: lock cmd will run");
                Toast.makeText(this, "Device Locked", Toast.LENGTH_SHORT).show();


            }
            else{
                Log.d(TAG, "lockDevice: #######status########   "  + status);
                stopLockTask();  // Unlock the device after 5 seconds
                Log.d(TAG, "device unlocked");
                Toast.makeText(getApplicationContext(), "Device Unlocked", Toast.LENGTH_SHORT).show();
            }
              // Delay for 5000 milliseconds or 5 seconds
        } else {
            Toast.makeText(this, "Admin permission required", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Disable volume buttons
            return true;  // Consume the event
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Disable volume buttons
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Mark the activity as not running when destroyed
        setLockScreenRunning(false);
    }

    private void setLockScreenRunning(boolean isRunning) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_RUNNING, isRunning);
        editor.apply();
    }

    // SharedPreferences helper method to retrieve the isRunning state
    public static boolean isLockScreenRunning(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_RUNNING, false);
    }


}

