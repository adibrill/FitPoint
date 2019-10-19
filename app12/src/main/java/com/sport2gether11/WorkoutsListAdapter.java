package com.sport2gether11;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
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
        public Button callbutton;
        public Button whatsappbutton;



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
            callbutton = itemView.findViewById(R.id.callbutton);
            whatsappbutton = itemView.findViewById(R.id.whatsappbutton);


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



        if(currentWork.getmStatus().equals("approved")) {
            holder.whatsappbutton.setVisibility(View.VISIBLE);
            holder.callbutton.setVisibility(View.VISIBLE);

            holder.whatsappbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                                    User find_user = npsnapshot.getValue(User.class);
                                    if (find_user.getUserName().equals(holder.mName.getText())) {



                                        try {
                                            String text = "hi, lets decide where to meet :)";// Replace with your message.

                                            String toNumber = "+972"+find_user.getPhoneNumber(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                                            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                                            mcon.startActivity(intent);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            });

            holder.callbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                                    User find_user = npsnapshot.getValue(User.class);
                                    if (find_user.getUserName().equals(holder.mName.getText())) {

                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+find_user.getPhoneNumber()));//change the number

                                        try
                                        {
                                          mcon.startActivity(callIntent);
                                        }
                                        catch (SecurityException e)
                                        {
                                            Log.e("cant call",e.toString());
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
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



    }

    @Override
    public int getItemCount() {
        return mWorkoutList.size();
    }

}
