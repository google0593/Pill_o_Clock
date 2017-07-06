package com.pilloclock.medicinereminder.app.fragments;


import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pilloclock.medicinereminder.app.activities.LoginActivity;
import com.pilloclock.medicinereminder.app.models.Meds;
import com.pilloclock.medicinereminder.app.models.MedsParser;
import com.pilloclock.medicinereminder.app.models.TimeCalculator;
import com.pilloclock.medicinereminder.app.R;
import com.pilloclock.medicinereminder.app.utils.CirclePhotoTransform;
import com.pilloclock.medicinereminder.app.utils.NotificationReceiver;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;

    private TextView txtMeds;
    private TextView txtAmount;
    private TextView txtHour;
    private Spinner measurement;
    private TimePicker timePicker;
    private ImageView userPhoto;
    private String medicine;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Meds, MedsViewHolder> recyclerAdapter;
    private DatabaseReference databaseRef;

    private int hour;
    private int timeHour;
    private int timeMinute;
    private String status;
    private NotificationReceiver nr;
    private boolean toBeDeleted;

    public HomeFragment() {
        // Required empty public constructor
    }

    //called after onCreate
    //put UI stuff in here.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
            context = this.getContext();

        //initialize receiver class
        nr = new NotificationReceiver();

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_meds);
        txtMeds = (TextView) v.findViewById(R.id.txtMedName);
        txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        TextView patientName = (TextView) v.findViewById(R.id.user_name);
        measurement = (Spinner) v.findViewById(R.id.txtMeasure);
        userPhoto = (ImageView) v.findViewById(R.id.user_photo);

        setUserPhoto();

        //set user name/welcome message
        patientName.setText(LoginActivity.userName);

        //populate spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.amount, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurement.setAdapter(arrayAdapter);

        //initialize list view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(LoginActivity.userID);

        firebase();

        Button testBtn = (Button) v.findViewById(R.id.btnTest);
        Button addBtn = (Button) v.findViewById(R.id.btnAddMeds);

        //parse medicine details from FDA (Food and Drug Administration)
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedsParser hi = new MedsParser();
                hi.parse(medicine);
            }
        });

        //add medicine button listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call add medicine method.
                medicine = txtMeds.getText().toString();
                MedsParser hi = new MedsParser();
                hi.parse(medicine);
                addMedicine();
            }
        });
        //setNotification();
        Log.d(TAG, "onCreateView: life cycle checker");



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: life cycle checker");

    }

    //ToDo need to do something about this part.
    @Override
    public void onDestroy() {
        super.onDestroy();
        //cleanup the adapter so it stops listening
        //mAdapter.cleanup();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setUserPhoto() {
        Picasso.with(context)
                .load(LoginActivity.userPhoto)
                //if no photo found or invalid URI use place holder photo
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .transform(new CirclePhotoTransform())
                .into(userPhoto);
    }

    //add button method
    private void addMedicine() {

        /*check if all text fields are filled .length()
         * remove leading and trailing white spaces .trim()*/

        if (txtAmount.getText().toString().trim().length() > 0 && txtMeds.getText().toString().trim().length() > 0) {
            timePickerDialog();

        } else {
            Toast.makeText(context, "Please fill all the details!", Toast.LENGTH_SHORT).show();
        }

    }


    private void timePickerDialog() {

        /*For future preferences:
         * If you want a dialog with custom layout you have to call dialog.show() first
         * then initialize views (Buttons, TextView etc)
         * then add listener after.*/

        //initialize alert dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        //initialize inflater
        LayoutInflater _inflater = getActivity().getLayoutInflater();

        mBuilder.setView(_inflater.inflate(R.layout.dialog_medicine, null));

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        Button btnPositive = (Button) dialog.findViewById(R.id.btnPositive);
        Button btnNegative = (Button) dialog.findViewById(R.id.btnNegative);
        txtHour = (TextView) dialog.findViewById(R.id.txtHour);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);


        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtHour.getText().toString().trim().length() > 0) {
                    //check the android current version. If its greater than API 23('M' Marshmallow).
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timeHour = timePicker.getHour();
                        timeMinute = timePicker.getMinute();
                        hour = Integer.parseInt(txtHour.getText().toString().trim());
                        dataToPush();
                        dialog.dismiss();
                    } else {
                        timeHour = timePicker.getCurrentHour();
                        timeMinute = timePicker.getCurrentMinute();
                        hour = Integer.parseInt(txtHour.getText().toString().trim());
                        dataToPush();
                        dialog.dismiss();
                        Toast.makeText(context, "Added to reminder list..", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Enter hours.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void dataToPush() {



        int systemTime = (int) System.currentTimeMillis();

        String _systemTime = Integer.toString(systemTime);
        String _hour = Integer.toString(hour);
        String _timeHour = Integer.toString(timeHour);
        String _timeMinute = Integer.toString(timeMinute);

        medicine = txtMeds.getText().toString().trim();
        String amount = txtAmount.getText().toString().trim() + " " + measurement.getSelectedItem().toString().trim();
        String userName = LoginActivity.userName;
        String desc = MedsParser.desc;
        status = "off";
        //push data to database
        databaseRef.push().setValue(new Meds(medicine, amount, userName, status, _systemTime, _hour, _timeHour, _timeMinute, desc));
        Toast.makeText(context, "Notification is now saved.", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "dataToPush: " + medicine + " " + amount + " " + hour + " " + timeHour + ":" + timeMinute + " " + userName + " " + desc);


    }


    //handles the recycler view and its listeners
    private void firebase() {

        /*To set an event listener, first we need to add a view in the View Holder Class
         * define a View (mView) and initialize it in the view constructor mView = itemView
         * then set the listener in populateViewHolder*/

        recyclerAdapter = new FirebaseRecyclerAdapter<Meds, MedsViewHolder>(Meds.class, R.layout.card_view, MedsViewHolder.class, databaseRef) {
            @Override
            protected void populateViewHolder(final MedsViewHolder viewHolder, final Meds model, final int position) {


                //assign value to views
                
                viewHolder.setMedicine(model.getMedicine());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setStartTime(TimeCalculator.setTime(model.getHour(), model.getMinute()));

                //check if alarm exist
                Boolean isAlarmSet = (PendingIntent.getBroadcast(context, Integer.parseInt(model.getTime()), new Intent(context, NotificationReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
                if (model.getStatus().equalsIgnoreCase("on")) {

                    viewHolder.setStatus(true);
                    if (!isAlarmSet) {
                        Log.d(TAG, "populateViewHolder: alarm is not set");

                    }
                } else {
                    viewHolder.setStatus(false);
                    Log.d(TAG, "onClick: OFF");
                }

                //Switch listner
                viewHolder.switchStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.switchStatus.isChecked()) {
                            status = "on";
                            Toast.makeText(context, "Alarm is set at " + TimeCalculator.setTime(model.getHour(), model.getMinute()) + " every " + model.getInterval() + "hours.", Toast.LENGTH_SHORT).show();
                            recyclerAdapter.getRef(position)
                                    .child("status")
                                    .setValue("on");

                            //sets the alarm
                            nr.setNotifData(context, model.getMedicine(), model.getInterval(), model.getAmount());
                            nr.setAlarm(context, Integer.parseInt(model.getHour()), Integer.parseInt(model.getMinute()), Integer.parseInt(model.getTime()));
                        } else {
                            status = "off";
                            recyclerAdapter.getRef(position)
                                    .child("status")
                                    .setValue("off");
                            nr.cancelNotification(context, Integer.parseInt(model.getTime()));

                            Toast.makeText(context, "Alarm off.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //delete alarm notification
                viewHolder.deleteMeds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("Confirm delete")
                                .setMessage("Do you really want to delete this notification?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        nr.cancelNotification(context, Integer.parseInt(model.getTime()));
                                        Toast.makeText(context, "Notification deleted.", Toast.LENGTH_SHORT).show();
                                        recyclerAdapter.getRef(position).removeValue();

                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });
                Log.d(TAG, "populateViewHolder bool alarm set: " + isAlarmSet);
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void confirmDelete(){

    }

    //Recycler view holder
    //has to be public
    public static class MedsViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView txtMedName;
        final TextView txtAmount;
        final TextView txtTime;
        final Switch switchStatus;
        final Button deleteMeds;



        public MedsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txtMedName = (TextView) itemView.findViewById(R.id.txtMedicineName);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtTime = (TextView) itemView.findViewById(R.id.txtStartTime);
            switchStatus = (Switch) itemView.findViewById(R.id.switchStatus);
            deleteMeds = (Button) itemView.findViewById(R.id.btnDelete);
        }

        public void setMedicine(String medicine) {
            txtMedName.setText(medicine);
        }

        public void setAmount(String amount) {
            txtAmount.setText(amount);
        }

        public void setStartTime(String time) {
            txtTime.setText(time);
        }

        public void setStatus(Boolean status) {
            switchStatus.setChecked(status);
        }
    }


}