package com.example.wareregy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleylogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PLOGIN = "plogin";
    private static final String KEY_EXP = "keyexp";
    private static final String KEY_ID = "keyid";
    private static final String KEY_NIVEL = "keynivel";
    private static final String KEY_MINXP = "keyminxp";
    private static final String KEY_MAXXP = "keymaxxp";
    private static final String KEY_NRREG = "nreg";


    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(Utilizador user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getNome());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_PLOGIN, user.getPrimeiroLogin());
        editor.putInt(KEY_EXP, user.getExp());
        editor.putInt(KEY_NIVEL, user.getNivel());
        editor.putInt(KEY_MINXP, user.getMinXp());
        editor.putInt(KEY_MAXXP, user.getMaxXp());
        editor.putInt(KEY_NRREG, user.getNrReg());
        editor.apply();
    }

    public void setPrimeiroLogin(int val) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PLOGIN, val);

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public Utilizador getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Utilizador(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getInt(KEY_PLOGIN, 0),
                sharedPreferences.getInt(KEY_EXP, 0),
                sharedPreferences.getInt(KEY_NIVEL, 0),
                sharedPreferences.getInt(KEY_MINXP, 0),
                sharedPreferences.getInt(KEY_MAXXP, 0),
                sharedPreferences.getInt(KEY_NRREG, 0)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, Login.class));
    }
}
