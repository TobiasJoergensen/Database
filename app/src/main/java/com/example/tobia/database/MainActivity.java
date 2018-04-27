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
import static java.lang.Math.toIntExact;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    PieChart pieChart;
    PieChart greyPieChart;
    double waterUsage = 0;
    int curArrayPlacement = 0;

    private static List<Double> listen = new ArrayList<>();
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
        calenderView.setTextColor(getResources().getColor(R.color.text_color));
        calenderView.setTypeface(opensans_extrabold);

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
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(0);
//        pieChart.setDrawSlicesUnderHole(false);
        pieChart.animateY(1000);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> Vand = new ArrayList<>();
        Vand.clear();
        waterUsage = waterUsageList.get(curArrayPlacement);
        float waterGoal = 10000;

        if (waterUsage <= waterGoal) {
            float sparet = waterGoal - (float)waterUsage;
            Vand.add(new PieEntry((float)waterUsage,  ""));
            Vand.add((new PieEntry(sparet, "")));

            TextView liter = (TextView) findViewById(R.id.literSaved);
            TextView percent = (TextView) findViewById(R.id.savedPercent);
            TextView saved = (TextView) findViewById(R.id.sparetSting);

            saved.setText("Sparet");

            if (sparet % 1 == 0){
                int dummy = Math.round(sparet);
                liter.setText(Integer.toString(dummy/ 1000) + "L");
            }
            else {
                liter.setText(Float.toString(sparet/ 1000) + "L");
            }

            if (waterUsage % 1 == 0 && waterGoal % 1 == 0){
                int dummy = (int) waterUsage;
                int dummy2 = Math.round(waterGoal);
                percent.setText((dummy / 1000) + " / " + (dummy2 / 1000));
            }
            else {
                percent.setText((waterUsage / 1000) + " / " + (waterGoal / 1000));
            }


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

        else {
            float forMeget = waterGoal - (float)waterUsage;
            forMeget = Math.abs(forMeget);
            forMeget = (float)waterUsage - forMeget;
            Vand.add((new PieEntry(forMeget, "")));
            Vand.add(new PieEntry((float)waterUsage,  ""));

            TextView liter = (TextView) findViewById(R.id.literSaved);
            TextView percent = (TextView) findViewById(R.id.savedPercent);
            TextView saved = (TextView) findViewById(R.id.sparetSting);

            saved.setText("Overbrugt");

            if (forMeget % 1 == 0){
                int dummy = Math.round(forMeget);
                liter.setText(Integer.toString(dummy/ 1000) + "L");
            }
            else {
                liter.setText(Float.toString(forMeget/ 1000) + "L");
            }

            if (waterUsage % 1 == 0 && waterGoal % 1 == 0){
                int dummy = (int) waterUsage;
                int dummy2 = Math.round(waterGoal);
                percent.setText((dummy / 1000) + " / " + (dummy2 / 1000));
            }
            else {
                percent.setText((waterUsage / 1000) + " / " + (waterGoal / 1000));
            }


            PieDataSet dataSet = new PieDataSet(Vand, " ");
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(0f);
            final int[] MY_COLORS = {Color.rgb( 255,0,0), Color.argb(0,228,228,228)};

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
            //calculater();
            runOnUiThread(new Runnable() {
                public void run() {
                    pieDrawerGrey();
                    pieDrawer();
                    TextView date = (TextView) findViewById(R.id.calenderView);
                    TextView flow = (TextView) findViewById(R.id.flowUsed);
                    TextView time = (TextView) findViewById(R.id.timeUsed);
                    TextView money = (TextView) findViewById(R.id.moneyUsed);
                    TextView Pris = (TextView) findViewById(R.id.moneyUsed);

                    Pris.setText(priceCount());

                    if ((flowList.get(curArrayPlacement) / 1000) % 1 == 0){
                        double caster = flowList.get(curArrayPlacement);
                        int dummy = (int) caster / 1000;
                        flow.setText(String.valueOf(dummy) + "L");
                    }
                    else {
                        double dummy = flowList.get(curArrayPlacement) / 1000;
                        flow.setText(Double.toString(dummy));
                    }

                    if (timeUsedList.get(curArrayPlacement) % 1 == 0) {
                        double caster = timeUsedList.get(curArrayPlacement);
                        int dummy = (int) caster;
                        time.setText(String.valueOf(dummy));
                    }
                    else {
                        time.setText(flowList.get(curArrayPlacement).toString());
                    }

                    TextView datoTop = (TextView) findViewById(R.id.calenderView);

                    try {
                        Date dateF = null;
                        String string = dateList.get(curArrayPlacement);
                        String format = "yyyy-MM-dd HH:mm:ss";
                        dateF = new SimpleDateFormat(format).parse(string);
                        String test = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateF);


                        String cap = test.substring(0, 1).toUpperCase() + test.substring(1);
                        datoTop.setText(cap.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Looper.loop();
        }
    });

    public String priceCount () {
        double count;

        if (waterUsageList.get(curArrayPlacement)% 1 == 0) {
            double caster = flowList.get(curArrayPlacement);
            int dummy = (int) caster;
            String stringer = String.valueOf(dummy);
            return stringer;
        }
        else {
            count = ((waterUsageList.get(curArrayPlacement) / 1000) * 44 * 4);
        }

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
    public void leftClick(View view) {
        Log.d("størrelse: ", Integer.toString(idList.size()));
        Log.d("størrelse: ", Integer.toString(curArrayPlacement));
        if(curArrayPlacement > 0) {
            curArrayPlacement = curArrayPlacement - 1;

            TextView date = (TextView) findViewById(R.id.calenderView);
            TextView flow = (TextView) findViewById(R.id.flowUsed);
            TextView time = (TextView) findViewById(R.id.timeUsed);
            TextView money = (TextView) findViewById(R.id.moneyUsed);

            pieDrawer();
            money.setText(priceCount());

            try {
                Date dateF = null;
                String string = dateList.get(curArrayPlacement);
                String format = "yyyy-MM-dd HH:mm:ss";
                dateF = new SimpleDateFormat(format).parse(string);
                String test = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateF);

                String cap = test.substring(0, 1).toUpperCase() + test.substring(1);
                date.setText(cap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((flowList.get(curArrayPlacement) / 1000) % 1 == 0){
                double caster = flowList.get(curArrayPlacement);
                int dummy = (int) caster / 1000;
                flow.setText(String.valueOf(dummy) + "L");
                Log.d("ER VI HER: ", "JA");
            }
            else {
                double dummy = flowList.get(curArrayPlacement) / 1000;
                flow.setText(Double.toString(dummy));
            }

            if (timeUsedList.get(curArrayPlacement) % 1 == 0) {
                double caster = timeUsedList.get(curArrayPlacement);
                int dummy = (int) caster;
                time.setText(String.valueOf(dummy));
            }
            else {
                time.setText(flowList.get(curArrayPlacement).toString());
            }
        }

    }

    public void rightClick(View view) {
        Log.d("størrelse: ", Integer.toString(idList.size()));
        Log.d("størrelse: ", Integer.toString(waterUsageList.size()));

        Log.d("størrelse: ", Integer.toString(curArrayPlacement));
        if(curArrayPlacement < (waterUsageList.size() - 1)) {
            curArrayPlacement = curArrayPlacement + 1;
            TextView date = (TextView) findViewById(R.id.calenderView);
            TextView flow = (TextView) findViewById(R.id.flowUsed);
            TextView time = (TextView) findViewById(R.id.timeUsed);
            TextView money = (TextView) findViewById(R.id.moneyUsed);

            pieDrawer();
            money.setText(priceCount());

            try {
                Date dateF = null;
                String string = dateList.get(curArrayPlacement);
                String format = "yyyy-MM-dd HH:mm:ss";
                dateF = new SimpleDateFormat(format).parse(string);
                String test = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateF);

                String cap = test.substring(0, 1).toUpperCase() + test.substring(1);
                date.setText(cap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((flowList.get(curArrayPlacement) / 1000) % 1 == 0){
                double caster = flowList.get(curArrayPlacement);
                int dummy = (int) caster / 1000;
                flow.setText(String.valueOf(dummy) + "L");
            }
            else {
                double dummy = flowList.get(curArrayPlacement) / 1000;
                flow.setText(Double.toString(dummy));
            }

            if (timeUsedList.get(curArrayPlacement) % 1 == 0) {
                double caster = timeUsedList.get(curArrayPlacement);
                int dummy = (int) caster;
                time.setText(String.valueOf(dummy));
            }
            else {
                time.setText(flowList.get(curArrayPlacement).toString());
            }
        }
    }

}