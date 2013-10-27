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

public class AlarmInfo {
    //    private static final Pattern pattern = Pattern.compile("([[:alpha:]]+).*([0-9]?[0-9]:[0-9][0-9]):?[0-9]?[0-9]? ?(AM|PM)?");
    private static final String TAG = "e34clock.AlarmInfo";

    public String extractAlarmText(String sysAlarm) {
//        Matcher matcher = pattern.matcher(sysAlarm);
//        String nextAlarm;
//        if (matcher.find()) {
//            String day = matcher.group(1);
//            StringBuilder alarmBuilder = new StringBuilder(20);
//            String timeStr = matcher.group(1);
//            alarmBuilder.append(timeStr);
//            Log.d(TAG, "Found time " + timeStr);
//            if (matcher.find(1)) {
//                String amPm = matcher.group(2);
//                if (amPm != null) {
//                    alarmBuilder.append(" ");
//                    alarmBuilder.append(amPm);
//                }
//                Log.d(TAG, "Found time extension " + amPm);
//            }
//            nextAlarm = alarmBuilder.toString();
//        } else {
//            nextAlarm = sysAlarm;
//            Log.d(TAG, "Pattern " + pattern + " did not match!");
//        }
//        Log.d(TAG, "Extracted text " + nextAlarm + " from " + sysAlarm);
//        return nextAlarm;
        return sysAlarm;
    }
}
