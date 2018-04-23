package com.example.tobia.database;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends Activity {

    private AsyncTaskParseJson doInBackground;
    PieChart pieChart;
    PieChart greyPieChart;
    private static List<Double> listen = new ArrayList<>();
    double waterUsage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        Pageviewver ();
        thread.start();
    }

    //Den her har Jimmi og Kathrine lavet. Jeg er ikke sikker på, om noget skal slettes heri.
    protected  void Pageviewver () {

        TextView textviev = (TextView) findViewById(R.id.textView);

        Resources res = getResources();

        int m_color = getResources().getColor(R.color.primary_white);

        final int[] MY_COLORS = {Color.rgb(228,228,228)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.setTabTextColors(R.color.text_color, R.color.text_color);
        tabLayout.setBackgroundColor(getColor(R.color.primary_white));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };

        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public void pieDrawerGrey () {
        greyPieChart = (PieChart) findViewById(R.id.pieChartGrey);
        greyPieChart.setExtraOffsets(5,10,5,5);
        greyPieChart.setHoleRadius(95f);
        greyPieChart.setDrawHoleEnabled(true);
        greyPieChart.setHoleColor(Color.WHITE);
        greyPieChart.setTouchEnabled(false);
        greyPieChart.animateY(1000);
        greyPieChart.setDrawEntryLabels(false);
        greyPieChart.getLegend().setEnabled(false);
        greyPieChart.getDescription().setEnabled(false);
        greyPieChart.setNoDataText("");

        ArrayList<PieEntry> Vand = new ArrayList<>();
        waterUsage = 1;
        Vand.add(new PieEntry((float)waterUsage,  ""));

        PieDataSet dataSet = new PieDataSet(Vand, " ");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);
        final int[] MY_COLORS = {Color.rgb(228,228,228)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        greyPieChart.setData(data);
        greyPieChart.invalidate();
    }

    public void pieDrawer () {

        //HER ER LINK TIL DOKUMENTATION AF PIECHART: https://github.com/PhilJay/MPAndroidChart
        pieChart = (PieChart) findViewById(R.id.pieChart);

        pieChart.setUsePercentValues(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(10f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(90f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTouchEnabled(false);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(0);
//        pieChart.setDrawSlicesUnderHole(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> Vand = new ArrayList<>();
        waterUsage = 400;
        float waterGoal = 500;
        float sparet = waterGoal - (float)waterUsage;
        Vand.add(new PieEntry((float)waterUsage,  ""));
        Vand.add((new PieEntry(sparet, "")));

        PieDataSet dataSet = new PieDataSet(Vand, " ");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);
        final int[] MY_COLORS = {Color.rgb( 74,144,226), Color.argb(0,228,228,228)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void calculater() {
        for (int i = 0; i < listen.size(); i++) {
            waterUsage = waterUsage + listen.get(i);
        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            AsyncTaskParseJson doInBackground = new AsyncTaskParseJson();
            try {
                doInBackground.execute();
//                try {Thread.sleep(5000);}
//                catch (InterruptedException e) { Log.d(e.toString(), "Så vi kan ikke sove");}
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (doInBackground.getStatus() != AsyncTaskParseJson.Status.FINISHED) {
                Log.d("xD", "Venter på vi er færdige");
                Log.d("Status på async: ", doInBackground.getStatus().toString());
                try {Thread.sleep(50);}
                catch (InterruptedException e) { Log.d(e.toString(), "Så vi kan ikke sove");}
            }
            Looper.prepare();
            Log.d("xD", "Vi er færdige");
            calculater();
            runOnUiThread(new Runnable() {
                public void run() {            pieDrawerGrey();
                    pieDrawer();
                }
            });
            Looper.loop();
        }
    });
}