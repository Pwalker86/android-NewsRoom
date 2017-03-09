package com.impwalker.newsroom;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by phil.walker on 3/6/17.
 */

class NewsStoryLoader extends AsyncTaskLoader<ArrayList<NewsStory>> {
    private String queryUrl;

    NewsStoryLoader(Context context, String url) {
        super(context);
        queryUrl = url;
    }

    @Override
    public ArrayList<NewsStory> loadInBackground() {
        String jsonResponse = "";

        URL url = QueryUtils.createURL(queryUrl);

        try {
            jsonResponse = QueryUtils.makeHttpRequest(url);
        } catch (IOException e){
            e.printStackTrace();
        }

        return QueryUtils.extractStories(jsonResponse);
    }
}
