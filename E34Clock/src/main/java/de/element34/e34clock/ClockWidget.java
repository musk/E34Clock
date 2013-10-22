package de.element34.e34clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;

import static de.element34.e34clock.Constants.BATTERY_ACTION;
import static de.element34.e34clock.Constants.BATTERY_IMG;
import static de.element34.e34clock.Constants.BATTERY_LEVEL;

/**
 * Copyrigfht 2013
 * Created by slanger on 18.10.13.
 */
public class ClockWidget extends AppWidgetProvider {
    private static String TAG = "e34clock.ClockWidget";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (Constants.BATTERY_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "Receiving battery action intent " + intent);
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateViews(intent, context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, ClockWidget.class)));
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "ClockWidget created!");
        Intent service = new Intent(context, BatteryService.class);
        context.startService(service);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "ClockWidget completely removed!");
        Intent service = new Intent(context, BatteryService.class);
        context.stopService(service);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG, "Deleting an instance of widget!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Updating widgets!");
        final Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Intent bIntent = new Intent(BATTERY_ACTION);
        bIntent.putExtra(BATTERY_LEVEL, info.getBatteryLevel(batteryStatus));
        bIntent.putExtra(BATTERY_IMG, info.getBatteryImg(batteryStatus));
        updateViews(bIntent, context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateViews(Intent intent, Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final String level = intent.getStringExtra(BATTERY_LEVEL);
        final int imgId = intent.getIntExtra(BATTERY_IMG, R.drawable.battery);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        views.setImageViewResource(R.id.battery, imgId);
        views.setTextViewText(R.id.battery_txt, level);
        Log.d(TAG, "Updating remote views with level=" + level + ", imgId=" + imgId);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    private BatteryInfo info = new BatteryInfo();
}
