package com.pilloclock.medicinereminder.app.models;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

public class MedicineDescription {


    private String genericName;

    private String description;

    public MedicineDescription(String genericName, String description) {
        this.genericName = genericName;
        this.description = description;
    }


    public String getGenericName() {
        return genericName;
    }

    public String getDescription() {
        return description;
    }

}
