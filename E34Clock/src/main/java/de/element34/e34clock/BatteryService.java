package de.element34.e34clock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * (c)Copyright 2013 Stefan Langer
 */
public class BatteryService extends Service {
    private static final String TAG = "e34clock.BatteryService";
    private BroadcastReceiver batteryReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating battery service!");
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                broadcastBatteryStatus(intent);
            }
        };

        Intent batteryChanged = registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        broadcastBatteryStatus(batteryChanged);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying battery service!");
        unregisterReceiver(batteryReceiver);
    }

    private void broadcastBatteryStatus(Intent batteryChanged) {
        Intent batteryIntent = new Intent(Constants.BATTERY_ACTION);
        batteryIntent.putExtra(Constants.BATTERY_LEVEL, info.getBatteryLevel(batteryChanged));
        batteryIntent.putExtra(Constants.BATTERY_IMG, info.getBatteryImg(batteryChanged));
        Log.d(TAG, "Broadcasting intent " + batteryIntent);
        sendBroadcast(batteryIntent);
    }

    private BatteryInfo info = new BatteryInfo();
}
