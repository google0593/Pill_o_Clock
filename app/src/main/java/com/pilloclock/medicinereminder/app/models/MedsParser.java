package com.pilloclock.medicinereminder.app.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

//JSON Parser
public class MedsParser {
    public static String desc;

    //ToDo need to pass a string later
    public void parse(String medicine) {
        Log.d(TAG, "parse: " + medicine);
        new JSONTask().execute("https://api.fda.gov/drug/label.json?search=" + medicine);
    }


    //publish results on the UI thread without having to manipulate threads and/or handlers.
    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //https://www.youtube.com/watch?v=qzn02HLf72o
                //Log.d(TAG, "parse: " + buffer);
                String parsedJson = buffer.toString();
                JSONObject parentObject = new JSONObject(parsedJson);
                JSONArray parentArray = parentObject.getJSONArray("results");

                JSONObject finalObject = parentArray.getJSONObject(0);

                //return results
                return finalObject.getString("indications_and_usage");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //results
            if(s==null || s.equals("")){
                desc="Please try the generic name of the medicine or no information found.";
            }else {
                desc = s;
            }

            Log.d(TAG, "PARSED!: " + s);
        }


    }

}
