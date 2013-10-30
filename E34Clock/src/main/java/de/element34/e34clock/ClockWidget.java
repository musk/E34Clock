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

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import static de.element34.e34clock.Constants.BATTERY_IMG;
import static de.element34.e34clock.Constants.BATTERY_LEVEL;

public class ClockWidget extends AppWidgetProvider {
    private static String TAG = "e34clock.ClockWidget";
    private BatteryInfo batteryInfo = new BatteryInfo();
    private AlarmInfo alarmInfo = new AlarmInfo();

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "Deleting an instance of widget!");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "ClockWidget removed from all screens!");
        stopClockService(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "ClockWidget created!");
        startClockService(context);
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateViews(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, ClockWidget.class)));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent " + intent);
        String intentAction = intent.getAction();
        if (Constants.ACTION_CLOCK_CHANGED.equals(intentAction)) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateViews(intent, context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, ClockWidget.class)));
        } else if (Intent.ACTION_SCREEN_OFF.equals(intentAction)) {
            stopClockService(context);
        } else if (Intent.ACTION_SCREEN_ON.equals(intentAction)) {
            startClockService(context);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Updating widget!");
        startClockService(context);
        updateViews(context, appWidgetManager, appWidgetIds);
    }

    private void setAlarmText(Context context, RemoteViews views) {
        List<String> nextAlarm = alarmInfo.extractAlarmText(Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED));
        Log.d(TAG, "Retrieved alarm [" + nextAlarm + "]!");
        if (nextAlarm.isEmpty()) {
            Log.d(TAG, "Hiding alarm bell!");
            views.setViewVisibility(R.id.alarm, View.INVISIBLE);
        } else {
            Log.d(TAG, "Showing alarm bell!");
            views.setViewVisibility(R.id.alarm, View.VISIBLE);
            views.setTextViewText(R.id.alarm_day_txt, nextAlarm.get(0));
            Log.d(TAG, "Setting alarm day to " + nextAlarm.get(0));
            if (nextAlarm.size() > 1) {
                if (nextAlarm.size() > 2) {
                    views.setTextViewText(R.id.alarm_txt, nextAlarm.get(1) + " " + nextAlarm.get(2));
                    Log.d(TAG, "Setting alarm time to " + nextAlarm.get(1) + " " + nextAlarm.get(2));
                } else {
                    views.setTextViewText(R.id.alarm_txt, nextAlarm.get(1));
                    Log.d(TAG, "Setting alarm time to " + nextAlarm.get(1));
                }
            }
        }
    }

    private void setBatteryStatus(RemoteViews views, Intent intent) {
        String batteryLevel = intent.getStringExtra(BATTERY_LEVEL);
        int batteryImgId = intent.getIntExtra(BATTERY_IMG, R.drawable.battery);
        Log.d(TAG, "Updating battery status: level=" + batteryLevel + ", image id=" + batteryImgId);
        views.setImageViewResource(R.id.battery, batteryImgId);
        views.setTextViewText(R.id.battery_txt, batteryLevel);
    }

    private void setDate(Context context, RemoteViews views) {
        final Time dateTime = new Time();
        dateTime.setToNow();
        final Resources resources = context.getResources();
        int leftPadding = resources.getDimensionPixelSize(R.dimen.date_padding_left);
        int weekdayPx = resources.getDimensionPixelSize(R.dimen.weekday_height);
        int paddingDate = resources.getDimensionPixelSize(R.dimen.date_padding_top);
        int topPadding = dateTime.weekDay * weekdayPx + paddingDate;
        String date = dateTime.format("%d.%m.%Y");
        Log.d(TAG, "Setting date to " + date + " for weekday " + dateTime.weekDay + ", left=" + leftPadding + ", top=" + topPadding);
        views.setTextViewText(R.id.date, date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setPadding(leftPadding, topPadding, views);
            Log.d(TAG, "Using setPadding");
        } else {
            setPaddingIceCreamSandwich(leftPadding, topPadding, views);
            Log.d(TAG, "Using setPaddingIceCreamSandwich");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setPadding(int leftPadding, int topPadding, RemoteViews views) {
        views.setViewPadding(R.id.date, leftPadding, topPadding, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setPaddingIceCreamSandwich(int leftPadding, int topPadding, RemoteViews views) {
        views.setInt(R.id.date, "setPaddingLeft", leftPadding);
        views.setInt(R.id.date, "setPaddingTop", topPadding);
    }

    private void startClockService(Context context) {
        Log.d(TAG, "Start service!");
        context.startService(new Intent(context, ClockService.class));
    }

    private void stopClockService(Context context) {
        Log.d(TAG, "Stopping service!");
        context.stopService(new Intent(context, ClockService.class));
    }

    private void updateViews(Intent intent, Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        views.setOnClickPendingIntent(R.id.network,
                PendingIntent.getActivity(context, 0,
                        new Intent(Settings.ACTION_WIFI_SETTINGS), 0));
        views.setOnClickPendingIntent(R.id.battery,
                PendingIntent.getActivity(context, 0,
                        new Intent(Intent.ACTION_POWER_USAGE_SUMMARY), 0));
        setBatteryStatus(views, intent);
        setAlarmText(context, views);
        setDate(context, views);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    private void updateViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Intent intent = new Intent(Constants.ACTION_CLOCK_CHANGED);
        intent.putExtra(BATTERY_LEVEL, batteryInfo.getBatteryLevel(batteryStatus));
        intent.putExtra(BATTERY_IMG, batteryInfo.getBatteryImg(batteryStatus));
        updateViews(intent, context, appWidgetManager, appWidgetIds);
    }
}
