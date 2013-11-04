/*
 * (c) Copyright 2013 Stefan Langer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.element34.e34clock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ClockService extends Service {
    private static final String TAG = "e34clock.ClockService";
    private BroadcastReceiver clockReceiver;
    private BatteryInfo info = new BatteryInfo();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Starting clock service!");
        clockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context outerCtx, Intent intent) {
                Log.d(TAG, "Received intent " + intent);
                String intentAction = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(intentAction)) {
                    Log.v(TAG, "Screen off! Unregistering clock receiver.");
                    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(final Context innerCtx, Intent intent) {
                            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                                Log.v(TAG, "Screen on! Registering clock receiver.");
                                outerCtx.unregisterReceiver(this);
                                Intent batteryChanged = registerServiceReceiver();
                                broadcastStatus(batteryChanged);
                            }
                        }
                    };
                    outerCtx.registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
                    outerCtx.unregisterReceiver(clockReceiver);
                } else if (Intent.ACTION_BATTERY_CHANGED.equals(intentAction)) {
                    broadcastStatus(intent);
                } else {
                    Intent batteryChanged = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    broadcastStatus(batteryChanged);
                }
            }
        };

        Intent batteryChanged = registerServiceReceiver();
        broadcastStatus(batteryChanged);
    }

    private Intent registerServiceReceiver() {
        Log.d(TAG, "Registering service receiver!");
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction("android.intent.action.ALARM_CHANGED");
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        return registerReceiver(clockReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping clock service!");
        unregisterReceiver(clockReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Received onStartCommand(intent=[" + intent + "],flags=[" + flags + "], startId=[" + startId + "]");
        return super.onStartCommand(intent, flags, startId);
    }

    private void broadcastStatus(Intent batteryChanged) {
        final String batteryLevel = info.getBatteryLevel(batteryChanged);
        final int batteryImg = info.getBatteryImg(batteryChanged);
        final Intent statusIntent = new Intent(Constants.ACTION_CLOCK_CHANGED);
        statusIntent.putExtra(Constants.BATTERY_LEVEL, batteryLevel);
        statusIntent.putExtra(Constants.BATTERY_IMG, batteryImg);
        Log.d(TAG, "Broadcasting intent " + statusIntent + "(level=" + batteryLevel + ", img=" + batteryImg + ")");
        sendBroadcast(statusIntent);
    }
}
