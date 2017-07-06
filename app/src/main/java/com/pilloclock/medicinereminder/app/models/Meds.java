package com.pilloclock.medicinereminder.app.models;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

public class Meds {

    private String mMedicine;
    private String mAmount;
    private String mName;
    private String mStatus;
    private String mTime;
    private String mInterval;
    private String mHour;
    private String mMinute;
    private String mDesc;

    public Meds() {

    }

    public Meds(String meds, String medsAmount, String name, String status, String time, String interval, String hour, String minute, String desc) {
        this.mMedicine = meds;
        this.mAmount = medsAmount;
        this.mName = name;
        this.mStatus = status;
        this.mTime = time;
        this.mInterval = interval;
        this.mHour = hour;
        this.mMinute = minute;
        this.mDesc = desc;
    }

    public String getMedicine() {
        return mMedicine;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getName() {
        return mName;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getTime() {
        return mTime;
    }

    public String getInterval() {
        return mInterval;
    }

    public String getHour() {
        return mHour;
    }

    public String getMinute() {
        return mMinute;
    }

    public String getDesc(){
        return mDesc;
    }

    public void setMedicine(String mMedicine) {
        this.mMedicine = mMedicine;
    }

    public void setAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public void setInterval(String mInterval) {
        this.mInterval = mInterval;
    }

    public void setHour(String mHour) {
        this.mHour = mHour;
    }

    public void setMinute(String mMinute) {
        this.mMinute = mMinute;
    }

    public void setDesc(String mDesc){
        this.mDesc = mDesc;
    }

}

