package com.it381.sql3rd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tv = findViewById(R.id.textView);

        String result = request("https://plantplaces.com/perl/mobile/viewplantsjson.pl?Combined_Name=date");

        JSONObject json = null;
        try {
            json = new JSONObject(result);

            String module = "";
            for(int i=0; i < json.getJSONArray("plants").length(); i++)
            {
                module += json.getJSONArray("plants").getJSONObject(i).getString("genus")+ "\t\t\t" +
                        json.getJSONArray("plants").getJSONObject(i).getString("species") +"\n";
            }

            tv.setText(module);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String request(String uri) {
        StringBuilder sb = new StringBuilder();
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            int statusCode = urlConnection.getResponseCode();
            if (statusCode ==  200) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                // temporary string to hold each line read from the reader.
                String inputLine;
                while ((inputLine = bin.readLine()) != null) {
                    sb.append(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }
}