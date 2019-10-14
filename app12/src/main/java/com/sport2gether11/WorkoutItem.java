package com.sport2gether11;

import java.util.jar.Attributes;

public class WorkoutItem
{
    private int mImageResource;
    private String mName;
    private String mWorkoutTime;
    private String mStatus;


    public WorkoutItem(int Resource,String Name1,String Workout2)
    {
        mImageResource = Resource;
        mName = Name1;
        mWorkoutTime=Workout2;
    }


    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmWorkoutTime() {
        return mWorkoutTime;
    }

    public void setmWorkoutTime(String mWorkoutTime) {
        this.mWorkoutTime = mWorkoutTime;
    }
}
