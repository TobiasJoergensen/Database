package com.example.tobia.database;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    final String TAG = "AsyncTaskParseJson.java";

    // set your json string url here
    String yourJsonStringUrl = "http://jimmibagger.dk/getter.php";

    // contacts JSONArray
    JSONArray dataJsonArr = null;

    public JSONObject jObj = null;
    private static List<PieEntry> pieList = new ArrayList<PieEntry>();
    private static List<Integer> list = new ArrayList<Integer>();

    public List<Integer> getList() {
        return list;
    }
    public List<PieEntry> getPieList() {
        return pieList;
    }

    public String getIndex (int i) {
        return list.get(i).toString();
    }


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
                String firstname = c.getString("id");
                String lastname = c.getString("temp");
                String username = c.getString("flow");
                String waterUsage = c.getString("waterUsage");
                String data = c.getString("date");

                // show the values in our logcat
                Log.e(TAG, "id: " + firstname
                        + ", temp: " + lastname
                        + ", flow: " + username
                        + ", waterUsage: " + waterUsage
                        + ", data: " + data);
                int dummy = Integer.parseInt(waterUsage);
                pieList.add(new PieEntry(dummy));
                list.add(dummy);
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
