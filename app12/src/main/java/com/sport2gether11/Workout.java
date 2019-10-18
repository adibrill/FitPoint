package com.sport2gether11;

public class Workout {

    private String sender;
    private String receiver;
    private String status;
    private String WorkOutType;
    private String timeStamp;

    public String getWorkOutType() {
        return WorkOutType;
    }

    public void setWorkOutType(String workOutType) {
        WorkOutType = workOutType;
    }



    public Workout(String receiver, String sender, String status, String timeStamp,String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.timeStamp = timeStamp;
        this.WorkOutType = type;
    }

    public Workout() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", status='" + status + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", type='" + WorkOutType + '\'' +
                '}';
    }
}
