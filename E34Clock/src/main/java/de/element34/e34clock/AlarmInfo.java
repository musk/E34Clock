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

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlarmInfo {
    private static final Pattern pattern = Pattern.compile("(\\w+)\\D+([0-9]?[0-9]:[0-9][0-9]) *(AM|PM)?");
    private static final String TAG = "e34clock.AlarmInfo";

    public List<String> extractAlarmText(String sysAlarm) {
        Matcher matcher = pattern.matcher(sysAlarm);
        List<String> nextAlarm = new ArrayList<String>();
        if (matcher.find()) {
            String weekDay = matcher.group(1);
            Log.d(TAG, "Found weekday " + weekDay);
            nextAlarm.add(weekDay);
            String timeStr = matcher.group(2);
            Log.d(TAG, "Found time " + timeStr);
            nextAlarm.add(timeStr);
            if (matcher.find(2)) {
                String amPm = matcher.group(3);
                if (amPm != null && !"".equals(amPm.trim())) {
                    Log.d(TAG, "Found time extension " + amPm);
                    nextAlarm.add(amPm);
                }
            }
        } else {
            nextAlarm.add(sysAlarm);
            Log.d(TAG, "Pattern " + pattern + " did not match!");
        }
        Log.d(TAG, "Extracted text " + nextAlarm + " from " + sysAlarm);
        return nextAlarm;
    }
}
