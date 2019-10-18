package com.sport2gether11.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport2gether11.R;
import com.sport2gether11.Workout;
import com.sport2gether11.WorkoutItem;
import com.sport2gether11.WorkoutsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private  ArrayList<WorkoutItem> workoutlog;
     private NotificationsViewModel notificationsViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mRecyclerView = (RecyclerView)root.findViewById(R.id.workoutsrecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
       //mAdapter = new WorkoutsListAdapter(workoutlog);

        mRecyclerView.setLayoutManager(mLayoutManager);
       // mRecyclerView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);


        String thisuser =mAuth.getCurrentUser().getDisplayName();
        Log.e("thisuser", thisuser.toString());
        workoutlog = new ArrayList<>();



        mDatabase.child("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if (dataSnapshot.exists()){
                          for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                              Workout l=npsnapshot.getValue(Workout.class);
                              String rec = l.getReceiver().toString();
                              String time = l.getTimeStamp().toString();
                              String Status = l.getStatus().toString();
                              String sender = l.getSender().toString();
                              String type = l.getWorkOutType().toString();

                              if (rec.equals(thisuser)) {
                                 Log.e("sender", rec.toString());
                                 Log.e("sender", thisuser.toString());
                                 WorkoutItem wi = new WorkoutItem(R.drawable.ic_menu_camera,sender,sender, time, Status,type);
                                  workoutlog.add(wi);
                              }

                              if (sender.equals(thisuser)) {
                                  Log.e("rec", rec.toString());
                                  Log.e("thisuser", thisuser.toString());

                                  WorkoutItem wi = new WorkoutItem(R.drawable.ic_menu_camera,rec,sender , time, Status,type);
                                  workoutlog.add(wi);
                              }
                              mAdapter = new WorkoutsListAdapter(workoutlog);
                             mRecyclerView.setAdapter(mAdapter);

                          }
                      }
                      else
                      {
                          Toast.makeText(getActivity(), "Send your first Workout!", Toast.LENGTH_SHORT).show();
                      }
                  }
                   @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                      }
                      });
        //workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Jenny","2019/10/18 20:00","Approved"));
        return root;
    }



}