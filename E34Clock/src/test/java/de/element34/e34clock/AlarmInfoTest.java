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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by slanger on 23.10.13.
 */
public class AlarmInfoTest {

    private final AlarmInfo alarmInfo = new AlarmInfo();

    @Test
    public void testExtractAlarmText() throws Exception {
        assertEquals("Text(Wed 8:00 AM) was not extracted properly!", "8:00 AM", alarmInfo.extractAlarmText("Wed 8:00 AM"));
        assertEquals("Text(Wed 19:00) was not extracted properly!", "19:00", alarmInfo.extractAlarmText("Wed 19:00"));
        assertEquals("Text(Wed 09:00) was not extracted properly!", "09:00", alarmInfo.extractAlarmText("Wed 09:00"));
        assertEquals("Text(Wed 9:00 PM) was not extracted properly!", "9:00 PM", alarmInfo.extractAlarmText("Wed 9:00 PM"));
        assertEquals("Text(Thu 12:00 PM) was not extracted properly!", "12:00 PM", alarmInfo.extractAlarmText("Wed 12:00 PM"));
    }
}
