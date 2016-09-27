package com.example.android.sunshine.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Welly Mulyadi on 12/09/2016.
 */

public class ViewHolder {

    private ImageView icon;
    private TextView date, high, low, forecast;

    public ViewHolder(View parent){
        icon = (ImageView)parent.findViewById(R.id.list_item_icon);
        date = (TextView)parent.findViewById(R.id.list_item_date_textview);
        high = (TextView)parent.findViewById(R.id.list_item_high_textview);
        low = (TextView)parent.findViewById(R.id.list_item_low_textview);
        forecast = (TextView)parent.findViewById(R.id.list_item_forecast_textview);
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getHigh() {
        return high;
    }

    public TextView getLow() {
        return low;
    }

    public TextView getForecast() {
        return forecast;
    }
}
