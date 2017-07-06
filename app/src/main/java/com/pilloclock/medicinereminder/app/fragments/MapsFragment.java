package com.pilloclock.medicinereminder.app.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pilloclock.medicinereminder.app.activities.LoginActivity;
import com.pilloclock.medicinereminder.app.models.Pharmacy;
import com.pilloclock.medicinereminder.app.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {

    private static final String TAG = "";
    private PlacePicker.IntentBuilder builder;

    private TextView pharmacyName;
    private TextView pharmacyAdd;
    private TextView pharmacyPhone;
    private Place place;
    private DatabaseReference ref;

    private String latLong;
    private int PLACE_PICKER_REQUEST = 1;
    private Context context;
    private static final int PERMS_REQUEST_CODE = 123;

    public MapsFragment() {
        // Required empty public constructor
    }

    //ToDo need to add "add button" function.
    //ToDo Fix double adding of data in firebase.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        context = this.getContext();

        ref = FirebaseDatabase.getInstance().getReference().child("pharmacy").child(LoginActivity.userID);

        pharmacyName = (TextView) v.findViewById(R.id.txtPharName);
        pharmacyAdd = (TextView) v.findViewById(R.id.txtPharAdd);
        pharmacyPhone = (TextView) v.findViewById(R.id.txtPharPhone);


        Button btn_pharmacy = (Button) v.findViewById(R.id.btn_update_pharmacy);

        //Build Google API (Goggle Place Picker)
        btn_pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn_call = (Button) v.findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber();
            }
        });

        Button btn_map = (Button) v.findViewById(R.id.btn_locate_gps);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
            }
        });
        requestPerms();

        return v;
    }

    //update UI after onCreate
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPharmacyLoc();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //TODO: Call Button
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(getContext(), data);

                //update database
                getPharmacyLoc();
                //display new data
                updatePharmacy();

                Toast.makeText(getContext(), "Pharmacy Updated!", Toast.LENGTH_LONG).show();
                updatePharmacy();

            }
        }
    }

    private void updatePharmacy() {
        //access root -> pharmacy.

        ref.push().setValue(new Pharmacy(
                place.getName().toString(),
                place.getAddress().toString(),
                place.getPhoneNumber().toString()));
        latLong = place.getLatLng().toString();
    }

    private void getPharmacyLoc(){
        ref.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot msgSnapshot: dataSnapshot.getChildren()) {
                    Pharmacy phar = msgSnapshot.getValue(Pharmacy.class);

                    pharmacyName.setText(phar.getPharmacyname());
                    pharmacyAdd.setText(phar.getPharmacyaddress());
                    pharmacyPhone.setText(phar.getPharmacyphone());

                    Log.d(TAG, "onDataChange: Connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onDataChange: Database Error");

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callNumber(){
        if(hasPermissions()){
            if(pharmacyAdd.getText() != null){
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+ pharmacyPhone.getText()));
                startActivity(call);
            }

        }else{
            requestPerms();
        }

    }

    private void goToLocation(){
        if(pharmacyAdd.getText() != null){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q="+pharmacyAdd.getText()));
            startActivity(intent);
        }

    }
    private boolean hasPermissions(){
        int res;
        String[] perm = new String[]{
                Manifest.permission.CALL_PHONE
        };

        for( String perms : perm){
            res = context.checkCallingOrSelfPermission(perms);{
                if(!(res == PackageManager.PERMISSION_GRANTED)){
                    return false;
                }
            }
        }
        return true;
    }
    private void requestPerms(){
        String[] perm = new String[]{
                android.Manifest.permission.CALL_PHONE
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(perm,PERMS_REQUEST_CODE);
        }
    }


}
