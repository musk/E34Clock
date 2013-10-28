package de.element34.e34clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Copyrigfht 2013
 * Created by slanger on 18.10.13.
 */
public class E34ClockWidget extends AppWidgetProvider {
    private static final String TAG = "e34clock.E34ClockWidget";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "E34ClockWidget created!");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "E34ClockWidget completely removed!");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG,"Deleting an instance of widget!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Updating widgets!");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_layout);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
