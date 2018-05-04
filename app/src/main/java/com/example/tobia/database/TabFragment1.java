package com.example.tobia.database;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {

    android.app.Activity activity;
    android.content.res.Resources res;

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    PieChart pieChart;
    PieChart greyPieChart;
    double waterUsage = 0;
    int curArrayPlacement = 0;
    float waterGoal = 10000;
    int mNumOfTabs;
    private ImageView group;
    TextView text;
    View view;
    TabLayout tab;
    ViewGroup viewGroup;
    DecimalFormat df = new DecimalFormat();

    private static List<Double> listen = new ArrayList<>();
    private static List<Integer> idList = new ArrayList<Integer>();
    private static List<Double> tempList = new ArrayList<Double>();
    private static List<Double> waterUsageList = new ArrayList<Double>();
    private static List<Double> flowList = new ArrayList<Double>();
    private static List<String> dateList = new ArrayList<String>();
    private static List<Double> timeUsedList = new ArrayList<Double>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_fragment1, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        df.setMaximumFractionDigits(2);
        listTransfer();
        mainActivitySetup();
        pieDrawerGrey();
        pieDrawer();
    }

    protected  void mainActivitySetup () {

        //Skriftyper vi kan bruge i appen.
        Typeface opensans_bold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_bold.ttf");
        Typeface opensans_extrabold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_extrabold.ttf");
        Typeface opensans_regular = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_regular.ttf");
        Typeface opensans_semibold = Typeface.createFromAsset(getActivity().getResources().getAssets(), "opensans_semibold.ttf");

        //Datovisning i toppen af skærmen
        text = (TextView) view.findViewById(R.id.savedPercent);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_regular);

        //De tre tekstbokse inde i cirklen.
        text = (TextView) view.findViewById(R.id.calenderView);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_extrabold);

        text = (TextView) view.findViewById(R.id.literSaved);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_extrabold);

        text = (TextView) view.findViewById(R.id.sparetSting);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_regular);

        //De tre bokse til tal af flow, tid og penge
        text = (TextView) view.findViewById(R.id.flowUsed);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_semibold);

        text = (TextView) view.findViewById(R.id.timeUsed);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_semibold);

        text = (TextView) view.findViewById(R.id.moneyUsed);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_semibold);

        //De tre navne bokse til flow, tid og penge
        text = (TextView) view.findViewById(R.id.flowView);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_regular);

        text = (TextView) view.findViewById(R.id.timeView);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_regular);

        text = (TextView) view.findViewById(R.id.moneyView);
        text.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        text.setTypeface(opensans_regular);

        //De tre bokse til liter, sekunder og pris
        text = (TextView) view.findViewById(R.id.literMinute);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_regular);

        text = (TextView) view.findViewById(R.id.timeTotal);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_regular);

        text = (TextView) view.findViewById(R.id.moneySpent);
        text.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
        text.setTypeface(opensans_regular);


        // Create an instance of the tab layout from the view.
        tab = (TabLayout) view.findViewById(R.id.tab_layout);

        viewGroup = (ViewGroup) tab.getChildAt(0);
        int tabsCount = viewGroup.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            viewGroup = (ViewGroup) viewGroup.getChildAt(j);
            int tabChildsCount = viewGroup.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = viewGroup.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(opensans_extrabold);
                    ((TextView) tabViewChild).setTextColor(res.getColor(R.color.text_color));
                }
            }
        }

        tab.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public void pieDrawerGrey () {
        greyPieChart = (PieChart) view.findViewById(R.id.pieChartGrey);
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
        pieChart = (PieChart) view.findViewById(R.id.pieChart);

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

        text = (TextView) view.findViewById(R.id.moneyUsed);
        text.setText(priceCount());

        if (waterUsage <= waterGoal) {
            float sparet = waterGoal - (float)waterUsage;
            Vand.add(new PieEntry((float)waterUsage,  ""));
            Vand.add((new PieEntry(sparet, "")));

            text = (TextView) view.findViewById(R.id.sparetSting);
            text.setText("Sparet");

            if (sparet % 1 == 0){
                int dummy = Math.round(sparet);
                text = (TextView) view.findViewById(R.id.literSaved);
                text.setText(Integer.toString(dummy/ 1000) + "L");
            }
            else {
                text = (TextView) view.findViewById(R.id.literSaved);
                float dummy = sparet / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text.setText(dummy2 + "L");
            }

            if (waterUsage % 1 == 0 && waterGoal % 1 == 0){
                int dummy = (int) waterUsage;
                int dummy2 = Math.round(waterGoal);
                text = (TextView) view.findViewById(R.id.savedPercent);
                text.setText((dummy / 1000) + " / " + (dummy2 / 1000));
            }
            else {
                text = (TextView) view.findViewById(R.id.savedPercent);
                double dummy = waterUsage / 1000;
                double dummy2 = waterGoal / 1000;
                String dummy3 = new DecimalFormat("#.##").format(dummy);
                String dummy4 = new DecimalFormat("#.##").format(dummy2);
                text.setText(dummy3 + " / " + dummy4);
            }

            try {
                Date dateF = null;
                String string = dateList.get(curArrayPlacement);
                String format = "yyyy-MM-dd HH:mm:ss";
                dateF = new SimpleDateFormat(format).parse(string);
                String test = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateF);

                String cap = test.substring(0, 1).toUpperCase() + test.substring(1);
                text = (TextView) view.findViewById(R.id.calenderView);
                text.setText(cap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((flowList.get(curArrayPlacement) / 1000) % 1 == 0){
                double caster = flowList.get(curArrayPlacement);
                int dummy = (int) caster / 1000;
                text = (TextView) view.findViewById(R.id.flowUsed);
                text.setText(String.valueOf(dummy) + "L");
                Log.d("ER VI HER: ", "JA");
            }
            else {
                double dummy = flowList.get(curArrayPlacement) / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text = (TextView) view.findViewById(R.id.flowUsed);
                text.setText(dummy2);
            }

            if (timeUsedList.get(curArrayPlacement) % 1 == 0) {
                double caster = timeUsedList.get(curArrayPlacement);
                int dummy = (int) caster;
                dummy = dummy / 1000;
                text = (TextView) view.findViewById(R.id.timeUsed);
                text.setText(String.valueOf(dummy));
            }
            else {
                double dummy = timeUsedList.get(curArrayPlacement) / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text = (TextView) view.findViewById(R.id.timeUsed);
                text.setText(dummy2);
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
            int overForbrug = (int)Math.abs(forMeget);
            forMeget = Math.abs(forMeget);
            forMeget = (float)waterUsage - forMeget;
            Vand.add((new PieEntry(forMeget, "")));
            Vand.add(new PieEntry((float)waterUsage,  ""));

            text = (TextView) view.findViewById(R.id.sparetSting);
            text.setText("Overbrugt");

            if (forMeget % 1 == 0){
                text = (TextView) view.findViewById(R.id.literSaved);
                text.setText(Integer.toString((overForbrug / 1000) ) + "L");
            }
            else {
                text = (TextView) view.findViewById(R.id.literSaved);
                float dummy = forMeget / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text.setText(dummy2 + "L");

                text.setText(Float.toString(forMeget/ 1000) + "L");
            }

            if (waterUsage % 1 == 0 && waterGoal % 1 == 0){
                int dummy = (int) waterUsage;
                int dummy2 = Math.round(waterGoal);
                text = (TextView) view.findViewById(R.id.savedPercent);
                text.setText((dummy / 1000) + " / " + (dummy2 / 1000));
            }
            else {
                double dummy = waterUsage / 1000;
                double dummy2 = waterGoal / 1000;
                String dummy3 = new DecimalFormat("#.##").format(dummy);
                String dummy4 = new DecimalFormat("#.##").format(dummy2);
                text.setText(dummy3 + " / " + dummy4);
                text = (TextView) view.findViewById(R.id.savedPercent);
                text.setText(dummy3 + " / " + dummy4);
            }

            try {
                Date dateF = null;
                String string = dateList.get(curArrayPlacement);
                String format = "yyyy-MM-dd HH:mm:ss";
                dateF = new SimpleDateFormat(format).parse(string);
                String test = new SimpleDateFormat("EEEE, dd MMMM", new Locale("da", "DK")).format(dateF);

                String cap = test.substring(0, 1).toUpperCase() + test.substring(1);
                text = (TextView) view.findViewById(R.id.calenderView);
                text.setText(cap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((flowList.get(curArrayPlacement) / 1000) % 1 == 0){
                double caster = flowList.get(curArrayPlacement);
                int dummy = (int) caster / 1000;
                text = (TextView) view.findViewById(R.id.flowUsed);
                text.setText(String.valueOf(dummy) + "L");
                Log.d("ER VI HER: ", "JA");
            }
            else {
                double dummy = flowList.get(curArrayPlacement) / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text = (TextView) view.findViewById(R.id.flowUsed);
                text.setText(dummy2);
            }

            if (timeUsedList.get(curArrayPlacement) % 1 == 0) {
                double caster = timeUsedList.get(curArrayPlacement);
                int dummy = (int) caster;
                dummy = dummy / 1000;
                text = (TextView) view.findViewById(R.id.timeUsed);
                text.setText(String.valueOf(dummy));
            }
            else {
                double dummy = timeUsedList.get(curArrayPlacement) / 1000;
                String dummy2 = new DecimalFormat("#.##").format(dummy);
                text = (TextView) view.findViewById(R.id.timeUsed);
                text.setText(dummy2);
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

    public String priceCount () {
        double count;

        if (waterUsageList.get(curArrayPlacement)% 1 == 0) {
            double caster = flowList.get(curArrayPlacement);
            int dummy = (int) caster;
            String stringer = String.valueOf(dummy);
            return stringer;
        }
        else {
            count = ((waterUsageList.get(curArrayPlacement) / 1000) * 44 * 4 / 1000);
        }
        String stringer = new DecimalFormat("#.##").format(count).toString();
        return stringer;
    }

    public AsyncTaskParseJson doInBackground;

    private void listTransfer() {
        AsyncTaskParseJson lister = new AsyncTaskParseJson();
        idList = lister.getIdList();
        tempList = lister.getTempList();
        waterUsageList = lister.getWaterUsageList();
        flowList = lister.getFlowList();
        dateList = lister.getDateList();
        timeUsedList = lister.getTimeUsed();
    }

    public void leftClick(View view) {
        Log.d("størrelse: ", Integer.toString(idList.size()));
        Log.d("størrelse: ", Integer.toString(curArrayPlacement));
        if(curArrayPlacement > 0) {
            curArrayPlacement = curArrayPlacement - 1;

            TextView date = (TextView) activity.findViewById(R.id.calenderView);
            TextView flow = (TextView) activity.findViewById(R.id.flowUsed);
            TextView time = (TextView) activity.findViewById(R.id.timeUsed);
            TextView money = (TextView) activity.findViewById(R.id.moneyUsed);

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
            TextView date = (TextView) activity.findViewById(R.id.calenderView);
            TextView flow = (TextView) activity.findViewById(R.id.flowUsed);
            TextView time = (TextView) activity.findViewById(R.id.timeUsed);
            TextView money = (TextView) activity.findViewById(R.id.moneyUsed);

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
