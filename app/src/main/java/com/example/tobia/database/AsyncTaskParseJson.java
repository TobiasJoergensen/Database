package com.example.tobia.database;

import android.graphics.Region;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.github.mikephil.charting.data.PieEntry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    final String TAG = "AsyncTaskParseJson.java";

    String yourJsonStringUrl = "http://jimmibagger.dk/getter.php";

    JSONArray dataJsonArr = null;

    public JSONObject jObj = null;

    private static List<Integer> idList = new ArrayList<Integer>();
    private static List<Double> tempList = new ArrayList<Double>();
    private static List<Double> waterUsageList = new ArrayList<Double>();
    private static List<Double> flowList = new ArrayList<Double>();
    private static List<String> dateList = new ArrayList<String>();
    private static List<Double> timeUsedList = new ArrayList<Double>();

    public List<Integer> getIdList() { return idList; }
    public List<Double> getTempList() { return tempList; }
    public List<Double> getWaterUsageList() { return waterUsageList; }
    public List<Double> getFlowList() { return flowList; }
    public List<String> getDateList() { return dateList; }
    public List<Double> getTimeUsed() { return timeUsedList; }


    private static List<PieEntry> idPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> tempListPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> waterUsagePieList = new ArrayList<PieEntry>();
    private static List<PieEntry> flowPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> datePieList = new ArrayList<PieEntry>();
    private static List<PieEntry> timeUsedPieList = new ArrayList<PieEntry>();

    public List<PieEntry> getIdPieList() { return idPieList; }
    public List<PieEntry> getTempListPieList() { return tempListPieList; }
    public List<PieEntry> getWaterUsagePieList() { return waterUsagePieList; }
    public List<PieEntry> getFlowPieList() { return flowPieList; }
    public List<PieEntry> getDatePieList() { return datePieList; }
    public List<PieEntry> getTimeUsedPieList() { return timeUsedPieList; }

    @Override
    protected void onPreExecute() {}

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }


    @Override
    protected String doInBackground(String... arg0) {

        idList.clear();
        tempList.clear();
        waterUsageList.clear();
        flowList.clear();
        dateList.clear();
        timeUsedList.clear();

        HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

        HttpGet httpPost = new HttpGet(yourJsonStringUrl);

        String jsonResult = "";

        try {

            HttpResponse response = httpClient.execute(httpPost);

            jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            System.out.println("Returned Json object " + jsonResult.toString());
            Log.d(jsonResult.toString(), "ting");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jObj = new JSONObject(jsonResult);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        try {
            dataJsonArr = jObj.getJSONArray("DocumentSequence");

            for (int i = 0; i < dataJsonArr.length(); i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);

                String id = c.getString("ID");
                String flowAverage = c.getString("waterAverage");
                String waterUsage = c.getString("waterUsage");
                String timeUsed = c.getString("timeUsed");
                String date = c.getString("date");

                Log.e(TAG, "id: " + id
                        + ", flow: " + flowAverage
                        + ", waterUsage: " + waterUsage
                        + ", date: " + date
                        + ", timeUsed: " + timeUsed);
                idList.add(Integer.parseInt(id));
                flowList.add(Double.parseDouble(flowAverage));
                waterUsageList.add(Double.parseDouble(waterUsage));
                dateList.add(date.toString());
                timeUsedList.add(Double.parseDouble(timeUsed));

                idPieList.add(new PieEntry(Integer.parseInt(id)));
                flowPieList.add(new PieEntry(Float.parseFloat(flowAverage)));
                waterUsagePieList.add(new PieEntry(Float.parseFloat(waterUsage)));
                timeUsedPieList.add(new PieEntry(Float.parseFloat(timeUsed)));
            }
        }


        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }


    protected void onPostExecute(String strFromDoInBg) {
    }
}
