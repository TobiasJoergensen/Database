package com.example.tobia.database;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {

    AppCompatActivity appCompatActivity;
    View view;
    TextView text;
    NestedScrollView mScrollView;
    private BarChart mChart;
    float userGoal = 10;
    private String[]days_lables = {"M","T","O","T","F","L","S",};
    List<String> days = new ArrayList<String>();

    private static List<Double> listen = new ArrayList<>();
    private static List<Integer> idList = new ArrayList<Integer>();
    private static List<Double> tempList = new ArrayList<Double>();
    private static List<Double> waterUsageList = new ArrayList<Double>();
    private static List<Double> flowList = new ArrayList<Double>();
    private static List<String> dateList = new ArrayList<String>();
    private static List<Double> timeUsedList = new ArrayList<Double>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment2, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listTransfer();

        mChart = (BarChart) getActivity().findViewById(R.id.chart);

        mChart.setMaxVisibleValueCount(40);

        mainActivitySetup();
        overUnderGoalDataSet();
        mScrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        mScrollView.setSmoothScrollingEnabled(true);
    }

    private void overUnderGoalDataSet(){
        ArrayList<BarEntry> yValues = new ArrayList<>();

        for(int i=0; i < 7; i++){

            if(waterUsageList.get(i) <= userGoal){
                double dummy = waterUsageList.get(i) / 1000;
                float val1 = (float) dummy;
                float val2 = 0;
                yValues.add(new BarEntry(i, new float[]{val1, val2}));
            }
            else{
                float val1 = userGoal;
                double dummy = waterUsageList.get(i) / 1000;
                float val2 = (float)(dummy - userGoal);
                yValues.add(new BarEntry(i, new float[]{val1, val2}));
            }
        }

        BarDataSet set1;

        set1 = new BarDataSet(yValues, "liter");
        set1.setDrawIcons(false);
        set1.setStackLabels(new String[]{"1", "2"});
        set1.setColors(new int[] {Color.rgb(74,144,226), Color.rgb(226,105,74)});
        set1.setDrawValues(false);


        BarData data = new BarData(set1);
        //data.setValueFormatter(new MyValueFormatter());

        data.setBarWidth(0.3f);

        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.getXAxis().setDrawGridLines(false); //Fjerner lodrette grid-linjer
        mChart.getAxisRight().setEnabled(false); //Fjerner højre linje

        //sætter lables på den x-akse og flytter x-aksen til bunden.
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = mChart.getXAxis();

        for(int i=0; i < 7; i++) {
            Date dateFirst = null;

            String date = dateList.get(i);

            String format = "yyyy-MM-dd HH:mm:ss";

            try {
                dateFirst = new SimpleDateFormat(format).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formatFirst = new SimpleDateFormat("EEEE", new Locale("da", "DK")).format(dateFirst);

            days.add(formatFirst.substring(0,1).toUpperCase());
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinValue(0f);

        //Goal limit-line linjen
        LimitLine goal_line = new LimitLine((float) userGoal);
        goal_line.enableDashedLine(5,10,5);
        goal_line.setLabel("Mål");
        //goal_line.setLabelPosition(LEFT_TOP);
        goal_line.setLineColor(Color.rgb(192,192,192));
        goal_line.setTextColor(Color.rgb(192,192,192));
        yAxis.addLimitLine(goal_line);

        try {
            Date dateFirst = null;
            Date dateLast = null;

            String dateNewest = dateList.get(0);
            String dateOldest = dateList.get(6);

            String format = "yyyy-MM-dd HH:mm:ss";

            dateFirst = new SimpleDateFormat(format).parse(dateNewest);
            dateLast = new SimpleDateFormat(format).parse(dateOldest);

            String formatFirst = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateFirst);
            String formatOld = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateLast);

            //String cap = format.substring(0, 1).toUpperCase() + formatFirst.substring(1);
            text = (TextView) view.findViewById(R.id.calenderViewFrag2);
            text.setText(formatFirst + " - " + formatOld);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void mainActivitySetup() {

        Typeface opensans_bold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_bold.ttf");
        Typeface opensans_extrabold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_extrabold.ttf");
        Typeface opensans_regular = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_regular.ttf");
        Typeface opensans_semibold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_semibold.ttf");

        text = (TextView) view.findViewById(R.id.calenderViewFrag2);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_extrabold);
    }

    private void listTransfer() {
        AsyncTaskParseJson doInBackground = new AsyncTaskParseJson();
        idList = doInBackground.getIdList();
        tempList = doInBackground.getTempList();
        waterUsageList = doInBackground.getWaterUsageList();
        flowList = doInBackground.getFlowList();
        dateList = doInBackground.getDateList();
        timeUsedList = doInBackground.getTimeUsed();
    }

}
