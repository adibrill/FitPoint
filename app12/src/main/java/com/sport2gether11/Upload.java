package com.sport2gether11;

import android.net.Uri;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload() {
        // Empty constructor needed
    }

    public Upload(String name, String imgUrl){
        if (name.trim().equals("")){
            name = "No name";
        }
        this.mName = name;
        this.mImageUrl = imgUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
