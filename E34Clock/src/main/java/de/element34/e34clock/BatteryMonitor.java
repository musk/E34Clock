package de.element34.e34clock;

import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by slanger on 21.10.13.
 */
public class BatteryMonitor {
    private static String TAG = "e34clock.BatteryMonitor";

    public RemoteViews updateView(RemoteViews views, Intent intent) {
        views.setImageViewResource(R.id.battery, getBatteryImg(intent));
        views.setTextViewText(R.id.battery_txt, getBatteryLevel(intent));
        return views;
    }

    private CharSequence getBatteryLevel(Intent batteryStatus) {
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
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                default:
                    Log.w(TAG, String.format("Unable to determine level of battery scale %d, level %d!", scale, level));
            }
        }
        Log.d(TAG, "Battery status " + retVal + " determined!");
        return retVal;
    }

    private int getBatteryImg(Intent batteryStatus) {
        int extra = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(TAG, "Retrieved EXTRA_PLUGGED " + extra);
        switch (extra) {
            case BatteryManager.BATTERY_PLUGGED_AC:
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
            case BatteryManager.BATTERY_PLUGGED_USB:
                Log.d(TAG, "Using plugged battery image!");
                return R.drawable.battery_load;
            case -1:
            default:
                Log.d(TAG, "Using battery image!");
                return R.drawable.battery;
        }
    }
}
