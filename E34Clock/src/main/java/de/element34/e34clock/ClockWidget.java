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

import static de.element34.e34clock.Constants.BATTERY_IMG;
import static de.element34.e34clock.Constants.BATTERY_LEVEL;

public class ClockWidget extends AppWidgetProvider {
    private static String TAG = "e34clock.ClockWidget";
    private BatteryInfo batteryInfo = new BatteryInfo();
    private AlarmInfo alarmInfo = new AlarmInfo();
    private String batteryLevel;
    private int batteryImgId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent " + intent);
        super.onReceive(context, intent);
        String intentAction = intent.getAction();
        if (Intent.ACTION_DATE_CHANGED.equals(intentAction) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intentAction) ||
                Intent.ACTION_TIME_CHANGED.equals(intentAction) ||
                "android.intent.action.ALARM_CHANGED".equals(intentAction)) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateViews(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, ClockWidget.class)));
        } else if (Constants.BATTERY_ACTION.equals(intentAction)) {
            batteryLevel = intent.getStringExtra(BATTERY_LEVEL);
            batteryImgId = intent.getIntExtra(BATTERY_IMG, R.drawable.battery);
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateViews(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, ClockWidget.class)));
        } else if (Intent.ACTION_SCREEN_OFF.equals(intentAction)) {
            context.stopService(new Intent(context, BatteryService.class));
        } else if (Intent.ACTION_SCREEN_ON.equals(intentAction)) {
            context.startService(new Intent(context, BatteryService.class));
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
        if (batteryLevel == null) {
            final Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = batteryInfo.getBatteryLevel(batteryStatus);
            batteryImgId = batteryInfo.getBatteryImg(batteryStatus);
        }
        updateViews(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_rel_layout);
        setBatteryStatus(views);
        setAlarmText(context, views);
        setDate(context, views);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    private void setDate(Context context, RemoteViews views) {
        final Time dateTime = new Time();
        dateTime.setToNow();
        final Resources resources = context.getResources();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int leftPadding = resources.getDimensionPixelSize(R.dimen.date_padding_left);
        int weekdayPx = resources.getDimensionPixelSize(R.dimen.weekday_height);
        int paddingDate = resources.getDimensionPixelSize(R.dimen.date_padding_top);//(int)Math.ceil(weekdayPx/2);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setPaddingIceCreamSandwich(int leftPadding, int topPadding, RemoteViews views) {
        views.setInt(R.id.date, "setPaddingLeft", leftPadding);
        views.setInt(R.id.date, "setPaddingTop", topPadding);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setPadding(int leftPadding, int topPadding, RemoteViews views) {
        views.setViewPadding(R.id.date, leftPadding, topPadding, 0, 0);
    }

    private void setBatteryStatus(RemoteViews views) {
        Log.d(TAG, "Updating battery status: level=" + batteryLevel + ", image id=" + batteryImgId);
        views.setImageViewResource(R.id.battery, batteryImgId);
        views.setTextViewText(R.id.battery_txt, batteryLevel);
    }

    private void setAlarmText(Context context, RemoteViews views) {
        String nextAlarm = alarmInfo.extractAlarmText(Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED));
        if ("".equals(nextAlarm)) {
            Log.d(TAG, "Hiding alarm bell!");
            views.setViewVisibility(R.id.alarm, View.INVISIBLE);
        } else {
            Log.d(TAG, "Showing alarm bell!");
            views.setViewVisibility(R.id.alarm, View.VISIBLE);
        }
        Log.d(TAG, "Setting alarm time to " + nextAlarm);
        views.setTextViewText(R.id.alarm_txt, nextAlarm);
    }
}
