package com.impwalker.newsroom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phil.walker on 3/6/17.
 */

class NewsStoryAdapter extends ArrayAdapter<NewsStory> {
    private Context context;

    NewsStoryAdapter(@NonNull Context context, ArrayList<NewsStory> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final NewsStory currentNewsStory = getItem(position);

        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        titleView.setText(currentNewsStory.getmTitle());

        TextView sectionView = (TextView) convertView.findViewById(R.id.section_view);
        sectionView.setText(currentNewsStory.getmSection());

        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);
        dateView.setText(currentNewsStory.getmDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNewsStory.getmUrl()));
                context.startActivity(browserIntent);
            }
        });

        return convertView;
    }
}
