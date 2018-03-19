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

    // set your json string url here
    String yourJsonStringUrl = "http://jimmibagger.dk/getter.php";

    // contacts JSONArray
    JSONArray dataJsonArr = null;

    public JSONObject jObj = null;

    private static List<Integer> idList = new ArrayList<Integer>();
    private static List<Double> tempList = new ArrayList<Double>();
    private static List<Double> waterUsageList = new ArrayList<Double>();
    private static List<Double> flowList = new ArrayList<Double>();
    private static List<String> dateList = new ArrayList<String>();

    public List<Integer> getIdList() { return idList; }
    public List<Double> getTempList() { return tempList; }
    public List<Double> getWaterUsageList() { return waterUsageList; }
    public List<Double> getFlowList() { return flowList; }
    public List<String> getDateList() { return dateList; }

    private static List<PieEntry> idPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> tempListPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> waterUsagePieList = new ArrayList<PieEntry>();
    private static List<PieEntry> flowPieList = new ArrayList<PieEntry>();
    private static List<PieEntry> datePieList = new ArrayList<PieEntry>();

    public List<PieEntry> getIdPieList() { return idPieList; }
    public List<PieEntry> getTempListPieList() { return tempListPieList; }
    public List<PieEntry> getWaterUsagePieList() { return waterUsagePieList; }
    public List<PieEntry> getFlowPieList() { return flowPieList; }
    public List<PieEntry> getDatePieList() { return datePieList; }

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

                // Storing each json item in variable
                String id = c.getString("id");
                String temp = c.getString("temp");
                String flow = c.getString("flow");
                String waterUsage = c.getString("waterUsage");
                String date = c.getString("date");

                // show the values in our logcat
                Log.e(TAG, "id: " + id
                        + ", temp: " + temp
                        + ", flow: " + flow
                        + ", waterUsage: " + waterUsage
                        + ", data: " + date);
                idList.add(Integer.parseInt(id));
                tempList.add(Double.parseDouble(temp));
                flowList.add(Double.parseDouble(flow));
                waterUsageList.add(Double.parseDouble(waterUsage));
                dateList.add(date.toString());

                idPieList.add(new PieEntry(Integer.parseInt(id)));
                tempListPieList.add(new PieEntry(Float.parseFloat(temp)));
                flowPieList.add(new PieEntry(Float.parseFloat(flow)));
                waterUsagePieList.add(new PieEntry(Float.parseFloat(waterUsage)));
            }
        }


        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {
    }
}
