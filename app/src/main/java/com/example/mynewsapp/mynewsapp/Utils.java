package com.example.mynewsapp.mynewsapp;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFromJson(jsonResponse);

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the Url", e);
        }
        return url;
    }
    private static final int readTime = 1000;
    private static final int connectTime = 15000;
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTime);
            urlConnection.setConnectTimeout(connectTime);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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

    private static List<News> extractFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray jsonArray = response.getJSONArray("results");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentNews = jsonArray.getJSONObject(i);

                if (currentNews.has("fields")) {
                    JSONObject fields = currentNews.getJSONObject("fields");
                    Drawable thumbnail = LoadImageFromUrl(fields.optString("thumbnail"));

                    JSONArray tagsArray = currentNews.getJSONArray("tags");
                    String author = "";
                    if (tagsArray.length() == 0) {
                        author = null;
                    } else {
                        for (int j = 0; j < tagsArray.length(); j++) {
                            JSONObject writer = tagsArray.getJSONObject(j);
                            author = writer.optString("webTitle");
                        }
                    }

                    String section = currentNews.optString("sectionName");
                    String title = currentNews.optString("webTitle");
                    String url = currentNews.optString("webUrl");
                    String date = currentNews.optString("webPublicationDate");

                    news.add(new News(section, title, url, date, author, thumbnail));
                } else {
                    Log.e("JSON_TAG", "NO FIELD JSON OBJECT");
                }
            }
            return news;
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the news Json results", e);
        }
        return null;
    }

    private static Drawable LoadImageFromUrl(String imageurl) {
        Drawable drawable = null;
        InputStream inputStream;

        try {
            inputStream = new URL(imageurl).openStream();
            drawable = Drawable.createFromStream(inputStream, null);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }
}

