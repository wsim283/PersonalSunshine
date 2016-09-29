package com.example.android.sunshine.app.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Welly Mulyadi on 21/09/2016.
 */

public class SunshineService  extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public static final String LOCATION_QUERY_EXTRA = "lqe";
    private final String LOG_TAG = SunshineService.class.getSimpleName();
    public SunshineService() {
        super("SunshineService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    static public class AlarmReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {


            String locationQuery = intent.getStringExtra(LOCATION_QUERY_EXTRA);

            Intent serviceIntent = new Intent(context, SunshineService.class);
            serviceIntent.putExtra(LOCATION_QUERY_EXTRA, locationQuery);
            context.startService(serviceIntent);


        }
    }

}
