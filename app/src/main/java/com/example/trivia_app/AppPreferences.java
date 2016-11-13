package com.example.trivia_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

public class AppPreferences {
    private static final String KEY_ANSWERED = "answered";


    private static SharedPreferences getSharedPreferences(@NonNull Context context){
        return context.getApplicationContext().getSharedPreferences("string",Context.MODE_PRIVATE);
    }

    public static int getAnswered(@NonNull Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(KEY_ANSWERED,0);
    }

    public static void saveAnswered(@NonNull Context context, int answered){
        getSharedPreferences(context).edit().putInt(KEY_ANSWERED, answered).apply();
    }

}
