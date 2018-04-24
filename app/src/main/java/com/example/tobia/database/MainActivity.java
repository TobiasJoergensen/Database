package com.example.tobia.database;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    PieChart pieChart;
    PieChart greyPieChart;
    private static List<Double> listen = new ArrayList<>();
    double waterUsage = 0;
    private static List<Integer> idList = new ArrayList<Integer>();
    private static List<Double> tempList = new ArrayList<Double>();
    private static List<Double> waterUsageList = new ArrayList<Double>();
    private static List<Double> flowList = new ArrayList<Double>();
    private static List<String> dateList = new ArrayList<String>();
    private static List<Double> timeUsedList = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        mainActivitySetup();
        thread.start();
    }

    //Den her har Jimmi og Kathrine lavet. Jeg er ikke sikker på, om noget skal slettes heri.
    protected  void mainActivitySetup () {

        //Skriftyper vi kan bruge i appen.
        Typeface opensans_bold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
        Typeface opensans_extrabold = Typeface.createFromAsset(getAssets(), "opensans_extrabold.ttf");
        Typeface opensans_regular = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        Typeface opensans_semibold = Typeface.createFromAsset(getAssets(), "opensans_semibold.ttf");

        //Datovisning i toppen af skærmen
        TextView savedPercent = (TextView) findViewById(R.id.savedPercent);
        savedPercent.setTextColor(getResources().getColor(R.color.grey_text));
        savedPercent.setTypeface(opensans_regular);

        //De tre tekstbokse inde i cirklen.
        TextView calenderView = (TextView) findViewById(R.id.calenderView);
        calenderView.setTextColor(getResources().getColor(R.color.grey_text));
        calenderView.setTypeface(opensans_regular);

        TextView literSaved = (TextView) findViewById(R.id.literSaved);
        literSaved.setTextColor(getResources().getColor(R.color.grey_text));
        literSaved.setTypeface(opensans_extrabold);

        TextView sparetString = (TextView) findViewById(R.id.sparetSting);
        sparetString.setTextColor(getResources().getColor(R.color.grey_text));
        sparetString.setTypeface(opensans_regular);

        //De tre bokse til tal af flow, tid og penge
        TextView flowUsed = (TextView) findViewById(R.id.flowUsed);
        flowUsed.setTextColor(getResources().getColor(R.color.text_color));
        flowUsed.setTypeface(opensans_semibold);

        TextView timeUsed = (TextView) findViewById(R.id.timeUsed);
        timeUsed.setTextColor(getResources().getColor(R.color.text_color));
        timeUsed.setTypeface(opensans_semibold);

        TextView moneyUsed = (TextView) findViewById(R.id.moneyUsed);
        moneyUsed.setTextColor(getResources().getColor(R.color.text_color));
        moneyUsed.setTypeface(opensans_semibold);

        //De tre navne bokse til flow, tid og penge
        TextView flowView = (TextView) findViewById(R.id.flowView);
        flowView.setTextColor(getResources().getColor(R.color.text_color));
        flowView.setTypeface(opensans_regular);

        TextView timeView = (TextView) findViewById(R.id.timeView);
        timeView.setTextColor(getResources().getColor(R.color.text_color));
        timeView.setTypeface(opensans_regular);

        TextView moneyView = (TextView) findViewById(R.id.moneyView);
        moneyView.setTextColor(getResources().getColor(R.color.text_color));
        moneyView.setTypeface(opensans_regular);

        //De tre bokse til liter, sekunder og pris
        TextView literMinute = (TextView) findViewById(R.id.literMinute);
        literMinute.setTextColor(getResources().getColor(R.color.grey_text));
        literMinute.setTypeface(opensans_regular);

        TextView timeTotal = (TextView) findViewById(R.id.timeTotal);
        timeTotal.setTextColor(getResources().getColor(R.color.grey_text));
        timeTotal.setTypeface(opensans_regular);

        TextView moneySpent = (TextView) findViewById(R.id.moneySpent);
        moneySpent.setTextColor(getResources().getColor(R.color.grey_text));
        moneySpent.setTypeface(opensans_regular);


        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));

        //Tablayoutets farve og skrifttype
        tabLayout.setTabTextColors(R.color.grey_text, R.color.primary_white);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.primary_white));

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(opensans_extrabold);
                    ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.text_color));
                }
            }
        }

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

    public AsyncTaskParseJson doInBackground = new AsyncTaskParseJson();
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
//            AsyncTaskParseJson doInBackground = new AsyncTaskParseJson();
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
            listTransfer();
            calculater();
            runOnUiThread(new Runnable() {
                public void run() {            pieDrawerGrey();
                    pieDrawer();
                    TextView datoTop = (TextView) findViewById(R.id.calenderView);
                    datoTop.setText(dateList.get(0));

                    TextView flow = (TextView) findViewById(R.id.flowUsed);
                    flow.setText(flowList.get(0).toString());

                    TextView tid = (TextView) findViewById(R.id.timeUsed);
                    tid.setText(timeUsedList.get(0).toString());

                    TextView Pris = (TextView) findViewById(R.id.moneyUsed);
                    Pris.setText(priceCount());
                }
            });
            Looper.loop();
        }
    });

    public String priceCount () {

        double count = ((waterUsageList.get(0) / 1000) * 44 * 4);
        String stringer = String.valueOf(count);
        return stringer;
    }

    private void listTransfer() {
        idList = doInBackground.getIdList();
        tempList = doInBackground.getTempList();
        waterUsageList = doInBackground.getWaterUsageList();
        flowList = doInBackground.getFlowList();
        dateList = doInBackground.getDateList();
        timeUsedList = doInBackground.getTimeUsed();
    }
    public void buttonClick(View view) {
        TextView text = (TextView) findViewById(R.id.calenderView);
        text.setText("trykket");
    }
}