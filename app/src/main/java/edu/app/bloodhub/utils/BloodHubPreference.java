package edu.app.bloodhub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import edu.app.bloodhub.model.User;

public class BloodHubPreference {

    private static final String USER = "user";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String IS_ADMIN = "is_admin";
    private static final String TOKEN = "token";

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public BloodHubPreference(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean loggedInStatus){
        editor.putBoolean(IS_LOGGED_IN,loggedInStatus);
        editor.apply();
    }

    public boolean isLoggedIN(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public void setAdmin(boolean loggedInStatus) {
        editor.putBoolean(IS_ADMIN, loggedInStatus);
        editor.apply();
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean(IS_ADMIN, false);

    }

    public void setUser(User user){
        Gson gson = new Gson();
        editor.putString(USER,gson.toJson(user));
        editor.apply();
    }

    public User getUser(){
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(USER,""),User.class);
    }

    public void setToken(String token){
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
    }
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
