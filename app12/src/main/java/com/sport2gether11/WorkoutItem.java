package com.sport2gether11;

import java.util.jar.Attributes;

public class WorkoutItem
{
    private int mImageResource;
    private String mName;
    private String mWorkoutTime;
    private String mStatus;
    private String sender;
    private String WType;

    public WorkoutItem(int Resource,String Name1,String sendedby,String Workout2,String status,String Type)
    {
        mImageResource = Resource;
        mName = Name1;
        sender = sendedby;
        mWorkoutTime=Workout2;
        mStatus = status;
        WType = Type;
    }

    public String getWType() {
        return WType;
    }

    public void setWType(String WType) {
        this.WType = WType;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
