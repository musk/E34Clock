package de.element34.e34clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import static de.element34.e34clock.E34Clock.E34CLOCK;

/**
 * Copyrigfht 2013
 * Created by slanger on 18.10.13.
 */
public class E34ClockWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(E34CLOCK.name(), "E34ClockWidget created!");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(E34CLOCK.name(), "E34ClockWidget completely removed!");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(E34CLOCK.name(),"Deleting an instance of widget!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(E34CLOCK.name(), "Updating widgets!");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
