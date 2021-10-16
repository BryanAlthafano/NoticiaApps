package com.example.noticiaapp.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.noticiaapp.Model.Login.LoginData;

import java.util.HashMap;

public class Session {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_ID = "user_id";
    private static final String FULLNAME = "fullname";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";

    public Session (Context mContext) {
        this.context = mContext;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(LoginData user) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER_ID, user.getUserId());
        editor.putString(FULLNAME, user.getFullname());
        editor.putString(USERNAME, user.getUsername());
        editor.putString(EMAIL, user.getEmail());
        editor.commit();
    }

    public HashMap<String, String> getUser() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(FULLNAME, sharedPreferences.getString(FULLNAME, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        return user;
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

}
