package com.example.healthyme.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthyme.Database.DbHelper;
import com.example.healthyme.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class FragmentReportMonthlyPressure extends Fragment {

    BarChart barChartBPMonthly;

    public FragmentReportMonthlyPressure() {
        // Required empty public constructor
    }





    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_report_monthly_pressure, container, false);

        // Hooks
        barChartBPMonthly = view.findViewById(R.id.bc_bp_monthly_report);



        // for sugar Level graph
        BarDataSet diastolicBDS = new BarDataSet(diastolic(), "Diastolic");
        BarDataSet systolicBDS = new BarDataSet(systolic(), "Systolic");
        BarData BPBarData = new BarData(diastolicBDS,systolicBDS);
        barChartBPMonthly.setData(BPBarData);







        XAxis xAxis = barChartBPMonthly.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);





        barChartBPMonthly.setDragEnabled(true);
        barChartBPMonthly.setVisibleXRangeMaximum(30);

        barChartBPMonthly.getAxisLeft().setDrawGridLines(false);
        barChartBPMonthly.getXAxis().setDrawGridLines(false);
        barChartBPMonthly.getAxisRight().setDrawAxisLine(false);
        barChartBPMonthly.getAxisRight().setDrawGridLines(false);

        xAxis.setTextColor(getContext().getColor(R.color.color_BW));
        barChartBPMonthly.getAxisRight().setTextColor(getContext().getColor(R.color.color_BW));
        barChartBPMonthly.getAxisLeft().setTextColor(getContext().getColor(R.color.color_BW));
        barChartBPMonthly.getDescription().setTextColor(getContext().getColor(R.color.color_BW));
        diastolicBDS.setColor(getContext().getColor(R.color.Yellowish));
        systolicBDS.setValueTextColor(getContext().getColor(R.color.color_BW));
        diastolicBDS.setValueTextColor(getContext().getColor(R.color.color_BW));
        systolicBDS.setColor(getContext().getColor(R.color.greenish));



        float barSpace = 0.05f;
        float groupSpace = 0.20f;

        BPBarData.setBarWidth(0.35f);

        barChartBPMonthly.getXAxis().setAxisMaximum(0);
        barChartBPMonthly.getXAxis().setAxisMaximum(0+barChartBPMonthly.getBarData().getGroupWidth(groupSpace,barSpace)*30);
        barChartBPMonthly.getAxisLeft().setAxisMinimum(0);

        barChartBPMonthly.groupBars(0, groupSpace,barSpace);
        barChartBPMonthly.invalidate();


        return view ;
    }

    // for adding data into arraylist for Diastolic
    private ArrayList<BarEntry> systolic() {
        // session calling for getting user Email
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email_pref", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", "").toString();
        DbHelper helper = new DbHelper(getContext());
        int id = 0;
        Cursor cursor = helper.getUserData(email);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }


        ArrayList<BarEntry> diastolicValues = new ArrayList<>();

        // fetching BP table last entry
        Cursor cursorSIA = helper.getWeeklyBP(id);
        for (int i = 0; i < cursorSIA.getCount(); i++) {
            cursorSIA.moveToNext();
            diastolicValues.add(new BarEntry( i, Float.parseFloat(cursorSIA.getString(1))));

        }

        return diastolicValues;
    }
    // for adding data into arraylist for Diastolic
    private ArrayList<BarEntry> diastolic() {
        ArrayList<BarEntry> systolicValues = new ArrayList<>();
        // session calling for getting user Email
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email_pref", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", "").toString();
        DbHelper helper = new DbHelper(getContext());
        int id = 0;
        Cursor cursor = helper.getUserData(email);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }

        // fetching BP table last entry
        Cursor cursorDIA = helper.getWeeklyBP(id);


        for (int i = 0; i < cursorDIA.getCount(); i++) {
            cursorDIA.moveToNext();
            systolicValues.add(new BarEntry( i, Float.parseFloat(cursorDIA.getString(2))));

        }
        return systolicValues;
    }

}