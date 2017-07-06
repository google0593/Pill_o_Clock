package com.pilloclock.medicinereminder.app.utils;

import com.google.firebase.database.FirebaseDatabase; /***
 * Created by nikol on May.
 * Project name: Pillo'Clock
 */

/**
 * this class allows firebase to store data locally
 * */
public class FirebasePersistence extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Enable disk persistence. Store firebase offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
