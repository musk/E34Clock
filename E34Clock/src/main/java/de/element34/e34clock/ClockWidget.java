package de.element34.e34clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Copyrigfht 2013
 * Created by slanger on 18.10.13.
 */
public class ClockWidget extends AppWidgetProvider {
    private static String TAG = "e34clock.ClockWidget";
    public final BatteryMonitor batteryMonitor = new BatteryMonitor();
    private int[] widgetIds = new int[0];

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            handleBatteryChanged(intent, this);
        } else if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            handleBatteryLow(intent, this);
        }
    }

    private void handleBatteryLow(Intent intent, ClockWidget clockWidget) {
    }

    private void handleBatteryChanged(Intent intent, ClockWidget clockWidget) {
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "ClockWidget created!");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "ClockWidget completely removed!");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG, "Deleting an instance of widget!");
        this.widgetIds = appWidgetIds;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Updating widgets!");
        this.widgetIds = appWidgetIds;
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        views = batteryMonitor.updateView(views, batteryStatus);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
