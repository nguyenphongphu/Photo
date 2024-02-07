package com.tp.photo.Model;

import com.google.gson.annotations.SerializedName;

public class Photo {
    private String email;
    @SerializedName("image")
    private String imageuser;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }
}
