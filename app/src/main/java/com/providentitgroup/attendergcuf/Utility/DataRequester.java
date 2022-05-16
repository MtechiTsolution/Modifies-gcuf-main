package com.providentitgroup.attendergcuf.Utility;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.cookie.Cookie;

public class DataRequester {
    public static final String BASE_STUDENT_URL = "https://student.gcuf.edu.pk/";
    public static final String BASE_FACULTY_URL = "https://faculty.gcuf.edu.pk/";
    public static final String BASE_STUDENT_MODULE_URL= BASE_STUDENT_URL+"modules/";
    public static final String BASE_FACULTY_MODULE_URL= BASE_FACULTY_URL+"modules/";
    private  static PersistentCookieStore myCookieStore;
    private static AsyncHttpClient client;

    public static final int SUCCESS = 200;
    public static final int INVALID_REQUEST = 400;
    public static final int REDIRECT_REQUEST = 302;



    public static AsyncHttpClient getInstance(Context context){
        if(client==null){
            client = new AsyncHttpClient();
            myCookieStore  = new PersistentCookieStore(context);
            client.setEnableRedirects(true);
            client.setMaxRetriesAndTimeout(8,10000);
            client.setCookieStore(myCookieStore);
        }
        return client;
    }

    public static void clearCookies(Context context){
        if(myCookieStore!=null){
            myCookieStore.clear();
        }
    }
    public static void getCookies(Context context){
        if(myCookieStore!=null){
            for(Cookie cookie :myCookieStore.getCookies()){
                Log.d("TAG",cookie.getName());
                Log.d("TAG",cookie.getValue());
            }
        }
    }

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance(context).get(url, params, responseHandler);
    }

    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getInstance(context).post(url, params, responseHandler);
    }


}
