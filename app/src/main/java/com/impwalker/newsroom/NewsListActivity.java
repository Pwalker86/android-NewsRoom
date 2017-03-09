package com.impwalker.newsroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phil.walker on 3/7/17.
 */

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsStory>> {
    NewsStoryAdapter mAdapter;
    ListView listView;
    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        String searchQuery = getIntent().getStringExtra("searchQuery");
        queryString = QueryUtils.BASE_URL;

        queryString += "&q=";
        queryString += searchQuery;

        checkConnectivity();

    }

    private void checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            TextView emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<ArrayList<NewsStory>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(queryString);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsStoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsStory>> loader, ArrayList<NewsStory> data) {
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        if (data.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            listView = (ListView) findViewById(R.id.list);
            mAdapter = new NewsStoryAdapter(this, data);
            listView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsStory>> loader) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }
}
