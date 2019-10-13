package com.sport2gether11;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutsViewHolder> {

    private ArrayList<WorkoutItem> mWorkoutList;

    public static class WorkoutsViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mName;
        public TextView mWorkoutTime;


        public WorkoutsViewHolder(View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.PartnerImage);
            mName = itemView.findViewById(R.id.PartnerName);
            mWorkoutTime = itemView.findViewById(R.id.WorkoutTime);

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


    }

    @Override
    public int getItemCount() {
        return mWorkoutList.size();
    }


}
