package com.sport2gether11;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutsViewHolder> {

    private ArrayList<WorkoutItem> mWorkoutList;

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

    public WorkoutsListAdapter(ArrayList<WorkoutItem> worklist)
    {
        mWorkoutList = worklist;
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
    }

    @Override
    public int getItemCount() {
        return mWorkoutList.size();
    }


}
