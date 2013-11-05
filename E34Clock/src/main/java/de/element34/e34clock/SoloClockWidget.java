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
import android.util.Log;
import android.widget.RemoteViews;

public class SoloClockWidget extends AppWidgetProvider {
    private static String TAG = "e34clock.FullClockWidget";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.v(TAG, "Deleting an instance of widget!");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "Clock widget removed!");
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "Clock widget created!");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, SoloClockWidget.class)));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "Updating widget!");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}
