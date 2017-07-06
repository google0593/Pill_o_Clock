package com.pilloclock.medicinereminder.app.models;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

public class Pharmacy {
    private String mPharmacyname;
    private String mPharmacyaddress;
    private String mpharmacyphone;

    public Pharmacy() {
    }

    public Pharmacy(String mPharmacyname, String mPharmacyaddress, String mPharmacyphone) {
        this.mPharmacyname = mPharmacyname;
        this.mPharmacyaddress = mPharmacyaddress;
        this.mpharmacyphone = mPharmacyphone;
    }

    public String getPharmacyname() {
        return mPharmacyname;
    }

    public void setPharmacyname(String mPharmacyName) {
        this.mPharmacyname = mPharmacyName;
    }

    public String getPharmacyaddress() {
        return mPharmacyaddress;
    }

    public void setPharmacyaddress(String mPharmacyAddress) {
        this.mPharmacyaddress = mPharmacyAddress;
    }

    public String getPharmacyphone() {
        return mpharmacyphone;
    }

    public void setPharmacyphone(String mpharmacyphone) {
        this.mpharmacyphone = mpharmacyphone;
    }
}
