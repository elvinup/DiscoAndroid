package com.purdue.a407.cryptodisco.Helpers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateAxisValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> dates;

    public DateAxisValueFormatter(ArrayList<String> dates) {
        this.dates = dates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return dates.get((int)value);
    }
}
