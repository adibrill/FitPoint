package com.sport2gether11.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        workoutlog = new ArrayList<>();

        // TODO
        //add firebase data!!!

       // Log.i("userdisplayname",mAuth.getCurrentUser().getDisplayName());
        //Toast.makeText(getActivity(),mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        mDatabase.child("Workouts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Workout currentWorkout = dataSnapshot.getValue(Workout.class);

                String rec = currentWorkout.getReceiver().toString();
                String thisuser = mAuth.getCurrentUser().getDisplayName().toString();
                String time = currentWorkout.getTimeStamp().toString();
                String Status = currentWorkout.getStatus().toString();

               // Log.e("username",currentWorkout.toString());

                if(rec.equals(thisuser)) {
                    Log.e("rec",rec.toString());
                    Log.e("thisuser",thisuser.toString());


                    try {

                        workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,rec, time,Status ));
                        Log.e("workoutlog",workoutlog.toString());
                    }
                    catch(Exception e)
                    {
                        Log.e("exception",e.toString());
                    }
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        //workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Jenny","2019/10/15 15:00","Pending"));
        //workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Andrew","2019/10/17 18:30","Canceled"));
        //workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Jenny","2019/10/18 20:00","Approved"));


        mRecyclerView = (RecyclerView)root.findViewById(R.id.workoutsrecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new WorkoutsListAdapter(workoutlog);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

    public void addworkouttolist(Workout currentWorkout) {

        String rec = currentWorkout.getReceiver().toString();
        String thisuser = mAuth.getCurrentUser().getDisplayName().toString();
        String time = currentWorkout.getTimeStamp().toString();
        String Status = currentWorkout.getStatus().toString();

        Log.e("username",currentWorkout.toString());

        if(rec.equals(thisuser)) {
              workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,rec, time,Status ));
        }

    }

}