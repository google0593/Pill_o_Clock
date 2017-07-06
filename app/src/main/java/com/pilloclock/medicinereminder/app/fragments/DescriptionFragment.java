package com.pilloclock.medicinereminder.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pilloclock.medicinereminder.app.activities.LoginActivity;
import com.pilloclock.medicinereminder.app.models.Meds;
import com.pilloclock.medicinereminder.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static io.fabric.sdk.android.Fabric.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment {


    private final static String fda_api = "https://api.fda.gov/drug/label.json?search=";
    private String description;
    private static Context context;
    private ArrayList listItems;
    private String a;
    private TextView tv;
    private int test =0;
    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_description,null);
        context = this.getContext();
        tv = (TextView) v.findViewById(R.id.txtMedDesc);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_description);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(LoginActivity.userID);

        FirebaseRecyclerAdapter<Meds, MedsViewHolder> recylcerAdapter = new FirebaseRecyclerAdapter<Meds, MedsViewHolder>(Meds.class, R.layout.card_view_description, MedsViewHolder.class, databaseRef) {
            @Override
            protected void populateViewHolder(MedsViewHolder viewHolder, Meds model, int position) {
                viewHolder.setMedicine(model.getMedicine());
                viewHolder.setDescription(model.getDesc());
                Log.d(TAG, "populateViewHolder: " + a);
;

            }
        };
        recyclerView.setAdapter(recylcerAdapter);
        listItems = new ArrayList<>();

        return v;
    }


    public static class MedsViewHolder extends RecyclerView.ViewHolder {
        public final TextView test;
        public final TextView textviewsz;
        private String testingers;
        public MedsViewHolder(View itemView) {
            super(itemView);
            test = (TextView) itemView.findViewById(R.id.txtMedNameDescription);
            textviewsz = (TextView) itemView.findViewById(R.id.txtMedDesc);
        }

        public void setMedicine(String medicine){
            test.setText(medicine);
        }

        public void setDescription(String description){
            textviewsz.setText(description);
        }

    }
}
