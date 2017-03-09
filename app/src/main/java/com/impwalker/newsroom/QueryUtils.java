package com.impwalker.newsroom;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by phil.walker on 3/6/17.
 */

class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    static final String BASE_URL = "http://content.guardianapis.com/search?api-key=test";

    private QueryUtils(){
    }

    static URL createURL(String queryUrl) {
        URL url;
        try {
            url = new URL(queryUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<NewsStory> extractStories(String response) {

        ArrayList<NewsStory> stories = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(response);
            JSONObject responseBlock = root.getJSONObject("response");
            JSONArray storyArray = responseBlock.getJSONArray("results");
            if (storyArray != null){
                int len = storyArray.length();

                for (int i = 0; i < len; i++) {
                    JSONObject story = storyArray.getJSONObject(i);

                    String title = story.getString("webTitle");
                    String sectionName = story.getString("sectionName");
                    String url = story.getString("webUrl");
                    String date = story.getString("webPublicationDate");

                    stories.add(new NewsStory(title, sectionName, date, url));
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        return stories;
    }
}
