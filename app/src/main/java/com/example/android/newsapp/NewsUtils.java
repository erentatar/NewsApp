package com.example.android.newsapp;

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

public class NewsUtils {

    private static final String LOG_TAG = NewsActivity.class.getName();
    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";
    private static final String WEB_TITLE = "webTitle";
    private static final String SECTION_NAME = "sectionName";
    private static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final String WEB_URL = "webUrl";
    private static final String TAGS = "tags";
    private static final String TYPE = "type";
    private static final String CONTRIBUTOR = "contributor";

    private NewsUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractNewsFromJson(jsonResponse);
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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

    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static ArrayList<News> extractNewsFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse))
            return null;

        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject(RESPONSE);
            JSONArray results = response.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);

                String newsTitle = currentNews.getString(WEB_TITLE);
                String newsSection = currentNews.getString(SECTION_NAME);
                String newsDate = currentNews.getString(WEB_PUBLICATION_DATE);
                String newsUrl = currentNews.getString(WEB_URL);
                String newsAuthor = "N/A";

                JSONArray tags = currentNews.getJSONArray(TAGS);
                if (tags.length() > 0) {
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tag = tags.getJSONObject(j);
                        String type = tag.getString(TYPE);
                        if (type.equals(CONTRIBUTOR)) {
                            newsAuthor = "by " + tag.getString(WEB_TITLE);
                        }
                    }
                }

                News news = new News(newsTitle, newsSection, newsAuthor, newsDate, newsUrl);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("NewsUtils", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }

}
