package com.tp.photo.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("unique_id")
    private String user_id;
    @SerializedName("name")
    private String username;
    private String email;
    private String password;
    @SerializedName("image")
    private String imageuser;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
