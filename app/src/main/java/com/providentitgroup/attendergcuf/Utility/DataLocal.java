package com.providentitgroup.attendergcuf.Utility;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class DataLocal {
    private static final String LOCAL_FILE_NAME="my_data";
    public static void saveString(Context context, String key, String value ){
        context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE)
                .edit().putString(key,value).apply();
    }
    public static void saveBoolean(Context context, String key, Boolean value ){
        context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE)
                .edit().putBoolean(key,value).apply();
    }
    public static void destroyData(Context context){
        context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static String getString(Context context, String key){
        return context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE).getString(key,"");
    }
    public static Boolean getBoolean(Context context, String key){
        return context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE).getBoolean(key,false);
    }
    public static boolean isExists(Context context, String key){
        return context.getSharedPreferences(LOCAL_FILE_NAME,Context.MODE_PRIVATE).contains(key);
    }

    //Only Save Object who has string child properties
    public static void saveJSONObject(Context context, JSONObject jsonObject) throws JSONException {
        Iterator<?> iterator = jsonObject.keys();
        while(iterator.hasNext()){
            String key = iterator.next().toString();
            String value = jsonObject.getString(key);
            saveString(context, key, value);
        }
    }
}
