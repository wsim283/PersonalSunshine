package com.example.android.sunshine.app;

/**
 * Created by Welly Mulyadi on 13/09/2016.
 */

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
@TargetApi(11)
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_SPEED = 5;
    private static final int COL_WEATHER_HUMID = 6;
    private static final int COL_WEATHER_PRESSURE = 7;
    private static final int COL_WEATHER_DEG = 8;
    private static final int COL_WEATHER_CONDITION_ID = 9;

    TextView detail_day, detail_date, detail_humid, detail_wind, detail_high, detail_low, detail_forecast, detail_pressure;
    ImageView detail_icon;
    CompassView detail_compass;
    Uri mUri = null;

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    ShareActionProvider mShareActionProvider;
    String mForecast = null;
    View rootView;
    int mPosition = -1;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
         detail_day = (TextView)rootView.findViewById(R.id.detail_day);
         detail_date = (TextView)rootView.findViewById(R.id.detail_date);
         detail_humid = (TextView)rootView.findViewById(R.id.detail_humidity);
         detail_wind = (TextView)rootView.findViewById(R.id.detail_wind);
         detail_high = (TextView)rootView.findViewById(R.id.detail_high);
         detail_low = (TextView)rootView.findViewById(R.id.detail_low);
         detail_forecast = (TextView)rootView.findViewById(R.id.detail_forecast);
         detail_pressure = (TextView)rootView.findViewById(R.id.detail_pressure);
        detail_icon = (ImageView)rootView.findViewById(R.id.detail_icon);
        detail_compass = (CompassView)rootView.findViewById(R.id.detail_compass);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            if (mForecast != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,getArguments(),this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(args != null && args.getParcelable("uri") != null){
            mUri = args.getParcelable("uri");
        }else{
            mUri = null;
        }

        return (mUri == null) ? null:new CursorLoader(getActivity(), mUri,DETAIL_COLUMNS,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()) {

            long dateInMillis = data.getLong(COL_WEATHER_DATE);
            String day = Utility.getDayName(getActivity(), dateInMillis);
            String date = Utility.getFormattedMonthDay(getActivity(),dateInMillis);
            String high = Utility.formatTemperature(getActivity(),
                    data.getDouble(COL_WEATHER_MAX_TEMP),
                    Utility.isMetric(getActivity()));
            String low = Utility.formatTemperature(getActivity(),
                    data.getDouble(COL_WEATHER_MIN_TEMP),
                    Utility.isMetric(getActivity()));
            String forecast = data.getString(COL_WEATHER_DESC);
            String wind = Utility.formatWind(getActivity(),
                    data.getDouble(COL_WEATHER_SPEED),
                    data.getDouble(COL_WEATHER_DEG));
            String pressure = getString(R.string.format_pressure, data.getDouble(COL_WEATHER_PRESSURE));
            String humid = getString(R.string.format_humidity,data.getDouble(COL_WEATHER_HUMID));

            mForecast = date + " Max: " +
                    high + " Min: " +
                    low + " Desc: " +
                    forecast;

            detail_day.setText(day);
            detail_date.setText(date);
            detail_high.setText(high);
            detail_low.setText(low);
            detail_forecast.setText(forecast);
            detail_wind.setText(wind);
            detail_humid.setText(humid);
            detail_pressure.setText(pressure);
            //Log.v(LOG_TAG, wind + " detected");
            detail_compass.setCompassDirection(wind);

            int resId = Utility.getArtResourceForWeatherCondition(data.getInt(COL_WEATHER_CONDITION_ID));
            detail_icon.setImageResource(resId);
            detail_icon.setContentDescription(forecast);


        }

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }
}