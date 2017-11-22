package com.example.jeff.jeff23andme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * Created by jeff on 11/17/17.
 */

public class Utils {
    private Utils() {
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setToken(final Context context, String token) {
        if (context == null)
            return;

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(Constant.USER_PREF_TOKEN, token).apply();
    }

    public static String getToken(final Context context) {
        if (context == null)
            return null;
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Constant.USER_PREF_TOKEN, null);
    }
}
