package com.impwalker.newsroom;

/**
 * Created by phil.walker on 3/6/17.
 */

class NewsStory {
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

    NewsStory(String title, String section, String date, String url){
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmSection() {
        return mSection;
    }

    String getmDate() {
        return mDate;
    }

    public String getmUrl() {
        return mUrl;
    }

}
