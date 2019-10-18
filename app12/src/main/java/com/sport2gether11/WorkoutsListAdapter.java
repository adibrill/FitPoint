package com.sport2gether11;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutsViewHolder> {

    private ArrayList<WorkoutItem> mWorkoutList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Activity mcon;

    public static class WorkoutsViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mName;
        public TextView mType;
        public TextView mWorkoutTime;
        public TextView mstatustext;
        public Button okbutton;
        public Button cancelbutton;




        public WorkoutsViewHolder(View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.PartnerImage);
            mName = itemView.findViewById(R.id.PartnerName);
            mWorkoutTime = itemView.findViewById(R.id.WorkoutTime);
            mstatustext = itemView.findViewById(R.id.workoutStatus);
            okbutton = itemView.findViewById(R.id.OKbutton);
            cancelbutton=itemView.findViewById(R.id.CancelButton);
            mType = itemView.findViewById(R.id.workouttypedata);


        }
    }

    public WorkoutsListAdapter(ArrayList<WorkoutItem> worklist,Activity _mcon)
    {
        mWorkoutList = worklist;
        mcon = _mcon;
    }


    @NonNull
    @Override
    public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item,parent,false);
        WorkoutsViewHolder wvh = new WorkoutsViewHolder(v);
        return wvh;

    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
    //pass data to list
        WorkoutItem currentWork = mWorkoutList.get(position);
        holder.mImageView.setImageResource(currentWork.getmImageResource());
        holder.mName.setText(currentWork.getmName());
        holder.mWorkoutTime.setText(currentWork.getmWorkoutTime());
        holder.mstatustext.setText(currentWork.getmStatus());
        holder.mType.setText(currentWork.getWType());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String myname = mAuth.getCurrentUser().getDisplayName();

        //////////

       // Log.e("getSender",currentWork.getSender().toString());
        // the name on the workoutitem should be the reciever in order to

        Log.e("getSender",currentWork.getSender());
        Log.e("myname",myname);
        if(!currentWork.getSender().toString().equals(myname)) {

            if (!currentWork.getmStatus().equals("pending")) {
                holder.okbutton.setVisibility(View.INVISIBLE);
                holder.cancelbutton.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            holder.okbutton.setVisibility(View.INVISIBLE);
            holder.cancelbutton.setVisibility(View.INVISIBLE);
        }


        holder.okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final String type = holder.mType.getText().toString();
                String myname = mAuth.getCurrentUser().getDisplayName();

                final String timestamp =  holder.mWorkoutTime.getText().toString();
                final String partnername = holder.mName.getText().toString();

                Log.e("partnername111",partnername);
                Log.e("timestamp111",timestamp);
                Log.e("type111",type);

                mDatabase.child("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                                Workout l = npsnapshot.getValue(Workout.class);


                                if((myname.equals(l.getReceiver().toString())&& partnername.equals(l.getSender().toString()))) {
                                    if (l.getTimeStamp().equals(timestamp) && l.getStatus().equals(("pending"))&& l.getWorkOutType().equals((type))) {
                                        l.setStatus("approved");

                                        mDatabase.child("Workouts").child(npsnapshot.getKey()).setValue(l);
                                        Log.e("approved","approved");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // mDatabase.child("Workouts").child(generatedString).setValue(workout1);

                Toast.makeText(v.getContext(), "Approved :)", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final String type = holder.mType.getText().toString();
                String myname = mAuth.getCurrentUser().getDisplayName();

                final String timestamp =  holder.mWorkoutTime.getText().toString();
                final String partnername = holder.mName.getText().toString();

                Log.e("partnername111",partnername);
                Log.e("timestamp111",timestamp);
                Log.e("type111",type);

                mDatabase.child("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                                Workout l = npsnapshot.getValue(Workout.class);


                                if((myname.equals(l.getReceiver().toString())&& partnername.equals(l.getSender().toString()))) {
                                    if (l.getTimeStamp().equals(timestamp) && l.getStatus().equals(("pending"))&& l.getWorkOutType().equals((type))) {
                                        l.setStatus("cancelled");

                                        mDatabase.child("Workouts").child(npsnapshot.getKey()).setValue(l);
                                        Log.e("cancelled","cancelled");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                Toast.makeText(view.getContext(), "Cancelled :(", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcon,MemberProfileActivity.class);
                //final TextView partnername =(TextView) .findViewById(R.id.PartnerName);
                i.putExtra("PartnerUserName",holder.mName.getText().toString());

                Log.i("partnername1212",holder.mName.getText().toString());

                if(mcon.getIntent() != null)
                {
                    String sport1= mcon.getIntent().getStringExtra("sport1");
                    String sport2=mcon.getIntent().getStringExtra("sport2");
                    String sport3=mcon.getIntent().getStringExtra("sport3");

                    i.putExtra("sport1",sport1);
                    i.putExtra("sport2",sport2);
                    i.putExtra("sport3",sport3);
                }
                else
                {
                    i.putExtra("yoga","sport1");
                    i.putExtra("yoga","sport2");
                    i.putExtra("yoga","sport3");
                }

                mcon.startActivity(i);
            }
        });

*/

    }

    @Override
    public int getItemCount() {
        return mWorkoutList.size();
    }

}
