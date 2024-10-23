package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLogin {

    @SerializedName("Username")
    @Expose
    private String username;

    @SerializedName("Password")
    @Expose
    private String password;

    public PostLogin(String username, String password) {
        this.username = username;
        this.password = password;
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
}
