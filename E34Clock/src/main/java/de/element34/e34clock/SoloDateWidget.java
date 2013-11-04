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

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

public class SoloDateWidget extends AppWidgetProvider {
    private static final ClockUtil util = new ClockUtil();
    private static String TAG = "e34clock.SoloDateWidget";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received intent " + intent);
        String action = intent.getAction();
        if (Constants.ACTION_CLOCK_CHANGED.equals(action)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, SoloDateWidget.class)));
        } else if (Constants.ACTION_SERVICE_RESTART.equals(action)) {
            util.startClockService(context);
        } else
            super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.v(TAG, "Deleting an instance of widget!");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "Date widget removed!");
        util.stopClockService(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "Date widget created!");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateViews(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, SoloDateWidget.class)));
        util.startClockService(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "Updating widget!");
        updateViews(context, appWidgetManager, appWidgetIds);
    }

    private void updateViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.date);
        final Resources resources = context.getResources();
        util.updateDate(views, resources.getDimensionPixelSize(R.dimen.solo_date_padding_left),
                resources.getDimensionPixelSize(R.dimen.solo_date_padding_top),
                resources.getDimensionPixelSize(R.dimen.solo_weekday_height));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}
