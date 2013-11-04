/*
 * (c) Copyright 2013 Stefan Langer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.element34.e34clock;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class ClockUtil {
    private static final String TAG = "e34clock.ClockUtil";

    public void startClockService(Context context) {
        Log.d(TAG, "Starting clock service!");
        context.startService(new Intent(context, ClockService.class));
    }

    public void stopClockService(Context context) {
        Log.d(TAG, "Stopping clock service!");
        context.stopService(new Intent(context, ClockService.class));
        context.sendBroadcast(new Intent(Constants.ACTION_SERVICE_RESTART));
    }

    public void updateDate(RemoteViews views, int left, int top, int height) {
        final Time dateTime = new Time();
        dateTime.setToNow();
        int topPadding = dateTime.weekDay * height + top;
        String date = dateTime.format("%d.%m.%Y");
        Log.v(TAG, "Setting date to " + date + " for weekday " + dateTime.weekDay + ", left=" + left + ", top=" + topPadding);
        views.setTextViewText(R.id.date, date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setPadding(left, topPadding, views);
        } else {
            setPaddingIceCreamSandwich(left, topPadding, views);
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
}
