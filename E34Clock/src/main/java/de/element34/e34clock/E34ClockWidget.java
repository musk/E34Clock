package de.element34.e34clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

import static de.element34.e34clock.E34Clock.E34Clock_Widget;

/**
 * Copyrigfht 2013
 * Created by slanger on 18.10.13.
 */
public class E34ClockWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(E34Clock_Widget.name(), "E34ClockWidget created!");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(E34Clock_Widget.name(), "E34ClockWidget completely removed!");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(E34Clock_Widget.name(), "Deleting an instance of widget!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(E34Clock_Widget.name(), "Updating widgets!");
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        views.setImageViewResource(R.id.battery, getBatteryImg(batteryStatus));
        views.setTextViewText(R.id.battery_txt, getBatteryLevel(batteryStatus));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private CharSequence getBatteryLevel(Intent batteryStatus) {
        String retVal = "---%";
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (scale > 0) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            int extra = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            Log.d(E34Clock_Widget.name(), "Retrieved EXTRA_STATUS " + extra);
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
                    Log.w(E34Clock_Widget.name(), String.format("Unable to determine level of battery scale %d, level %d!", scale, level));
            }
        }
        Log.d(E34Clock_Widget.name(), "Battery status " + retVal + " determined!");
        return retVal;
    }

    private int getBatteryImg(Intent batteryStatus) {
        int extra = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(E34Clock_Widget.name(), "Retrieved EXTRA_PLUGGED " + extra);
        switch (extra) {
            case BatteryManager.BATTERY_PLUGGED_AC:
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
            case BatteryManager.BATTERY_PLUGGED_USB:
                Log.d(E34Clock_Widget.name(), "Using plugged battery image!");
                return R.drawable.battery_load;
            case -1:
            default:
                Log.d(E34Clock_Widget.name(), "Using battery image!");
                return R.drawable.battery;
        }
    }

}
