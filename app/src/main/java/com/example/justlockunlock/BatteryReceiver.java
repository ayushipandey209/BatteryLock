package com.example.justlockunlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryReceiver extends BroadcastReceiver {

    private static final String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "Device is charging");
            launchLockScreen(context, true);  // Charging is true
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Device is not charging");
            launchLockScreen(context, false);  // Charging is false
        }
    }

    private void launchLockScreen(Context context, boolean isCharging) {
        boolean isRunning = LockScreen.isLockScreenRunning(context);
        Log.d(TAG, "launchLockScreen: LockScreen isRunning: " + isRunning);
        if (isRunning) {
            Log.d(TAG, "launchLockScreen: lockscreen bool");
            Log.d(TAG, "LockScreen is already running, closing existing one");
            Intent closeIntent = new Intent(context, LockScreen.class);
            closeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(closeIntent);  // This will close the running activity
        }
        Log.d(TAG, "launchLockScreen: starting lock");
        Intent activityIntent = new Intent(context, LockScreen.class);
        activityIntent.putExtra("isCharging", isCharging);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}

