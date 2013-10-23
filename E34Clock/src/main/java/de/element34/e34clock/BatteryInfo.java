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

import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryInfo {
    private static final String TAG = "e34clock.BatteryInfo";

    public String getBatteryLevel(Intent batteryStatus) {
        String retVal = "---%";
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (scale > 0) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            int extra = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            Log.d(TAG, "Retrieved EXTRA_STATUS " + extra);
            switch (extra) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    retVal = String.valueOf((int) Math.ceil(level / (float) scale * 100)) + "%";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    retVal = "Full";
                    break;
                default:
                    Log.w(TAG, String.format("Unable to determine level of battery scale %d, level %d!", scale, level));
            }
        }
        Log.d(TAG, "Battery status " + retVal + " determined!");
        return retVal;
    }

    public int getBatteryImg(Intent batteryStatus) {
        int extra = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(TAG, "Retrieved EXTRA_PLUGGED " + extra);
        switch (extra) {
            case BatteryManager.BATTERY_PLUGGED_AC:
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
            case BatteryManager.BATTERY_PLUGGED_USB:
                Log.d(TAG, "Using plugged battery image!");
                return R.drawable.battery_load;
            default:
                Log.d(TAG, "Using battery image!");
                return R.drawable.battery;
        }
    }
}
