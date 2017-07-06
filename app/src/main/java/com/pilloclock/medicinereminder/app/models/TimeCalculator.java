package com.pilloclock.medicinereminder.app.models;

/***
 * Created by nikol on May.
 * Project name: Pillo'Clock
 */

public class TimeCalculator {

    public static String setTime(String _hour, String _minute) {
        int hour = Integer.parseInt(_hour);
        int minute = Integer.parseInt(_minute);
        String time;
        String day;
        String sDayWith0 = "0";
        String sMin = null;
        if (hour >= 13) {
            hour = hour - 12;
            day = "PM";
        } else {
            day = "AM";
        }

        if(minute < 10){
            sMin = "0" + Integer.toString(minute);
        }else{
            sMin = Integer.toString(minute);
        }
        time = Integer.toString(hour) + ":" + sMin + day;
        return time;
    }
}
