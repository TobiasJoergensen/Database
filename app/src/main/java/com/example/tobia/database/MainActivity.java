package com.example.tobia.database;

import android.app.Activity;
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

import com.github.mikephil.charting.charts.PieChart;

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

public class MainActivity extends Activity {

    private AsyncTaskParseJson doInBackground;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread.start();

        try {Thread.sleep(5000);}
        catch (InterruptedException e) { Log.d(e.toString(), "Så vi kan ikke sove");}
        Testfunc();

        TextView text = findViewById(R.id.textView);
        text.setText("PodRace");
        pieChart = (PieChart) findViewById(R.id.pieChart);

        pieChart.setCenterText("Our chart!");
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                new AsyncTaskParseJson().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    //MÅSKE SKAL VI LAVE ET HASHMAP I STEDET????????
    public void Testfunc() {

        doInBackground = new AsyncTaskParseJson();

        String test = doInBackground.getIndex(0);

        Log.d(test, "VI FÅR NOGET");
        List<Integer> listen = new ArrayList<>();
        listen = doInBackground.getList();

        for (int i = 0; i < listen.size(); i++) {
            Log.d(listen.get(i).toString(), "list item");
        }

    }
}