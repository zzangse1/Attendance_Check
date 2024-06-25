package com.zzangse.attendance_check;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_ACCOUNT = "account";

    public static void setUserId(Context context, String input) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MY_ID", input);
        editor.apply(); // Use apply() instead of commit() for asynchronous saving
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        return prefs.getString("MY_ID", "");
    }

    public static void setCheckBox(Context context, String isCheck) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MY_CHECK", isCheck);
        editor.apply();
    }

    public static String getCheckBox(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        return prefs.getString("MY_CHECK", "false");
    }

    public static void setUserPass(Context context, String input) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MY_PASS", input);
        editor.apply(); // Use apply() instead of commit() for asynchronous saving
    }

    public static String getUserPass(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        return prefs.getString("MY_PASS", "");
    }

    public static void clearUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply(); // Use apply() instead of commit() for asynchronous saving
    }
}
