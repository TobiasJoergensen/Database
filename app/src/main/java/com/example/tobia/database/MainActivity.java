package com.example.tobia.database;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class    MainActivity extends Activity {

    private AsyncTaskParseJson doInBackground;
    PieChart pieChart;
    private static List<Double> listen = new ArrayList<>();
    double waterUsage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        thread.start();
    }

    public void pieDrawer () {

        //HER ER LINK TIL DOKUMENTATION AF PIECHART: https://github.com/PhilJay/MPAndroidChart
        pieChart = (PieChart) findViewById(R.id.pieChart);

        pieChart.setCenterText("Our chart!");
        pieChart.setUsePercentValues(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(10f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.CYAN);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> Vand = new ArrayList<>();

        Vand.add(new PieEntry((float)waterUsage, "Your use"));
        Vand.add((new PieEntry(500f, "The world")));

        PieDataSet dataSet = new PieDataSet(Vand, "Vand fordeling");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.YELLOW);
        data.setValueTextSize(10f);

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
            Log.d("xD", "Vi er færdige");
            calculater();
            pieDrawer();
        }
    });
}