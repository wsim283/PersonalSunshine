package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    private final  int VIEW_TYPE_TODAY = 0;
    private final  int VIEW_TYPE_FUTURE_DAY = 1;
    boolean useTodayLayout = false;
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    public void setUseTodayLayout(boolean useTodayLayout){
        this.useTodayLayout = useTodayLayout;
    }
    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext,high, isMetric) + "/" + Utility.formatTemperature(mContext,low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {


        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layout = R.layout.list_item_forecast;
        if(getItemViewType(cursor.getPosition()) == VIEW_TYPE_TODAY){
            layout = R.layout.list_item_forecast_today;
        }
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && useTodayLayout)  ? VIEW_TYPE_TODAY:VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*
            This is where we fill-in the views with the contents of the cursor.
         */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        String date = Utility.getFriendlyDayString(mContext,cursor.getLong(ForecastFragment.COL_WEATHER_DATE), useTodayLayout);
        String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        String high = Utility.formatTemperature(mContext,cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP), Utility.isMetric(mContext));
        String low = Utility.formatTemperature(mContext,cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP), Utility.isMetric(mContext));

        ((ViewHolder)view.getTag()).getDate().setText(date);
        ((ViewHolder)view.getTag()).getForecast().setText(forecast);
        ((ViewHolder)view.getTag()).getHigh().setText(high);
        ((ViewHolder)view.getTag()).getLow().setText(low);


        int resId;
        if (getItemViewType(cursor.getPosition())==VIEW_TYPE_TODAY) {
            resId = Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID));
        } else {
            resId = Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID));
        }
        ((ViewHolder) view.getTag()).getIcon().setImageResource(resId);
        ((ViewHolder) view.getTag()).getIcon().setContentDescription(forecast);



    }
}