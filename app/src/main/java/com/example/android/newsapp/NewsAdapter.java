package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);

            holder = new ViewHolder();
            holder.titleView = listItemView.findViewById(R.id.title);
            holder.sectionView = listItemView.findViewById(R.id.section);
            holder.authorView = listItemView.findViewById(R.id.author);
            holder.dateView = listItemView.findViewById(R.id.date);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        News currentNews = getItem(position);

        holder.titleView.setText(currentNews.getTitle());
        holder.sectionView.setText(currentNews.getSection());
        holder.authorView.setText(currentNews.getAuthor());
        holder.dateView.setText(formatDate(currentNews.getDate()));

        return listItemView;
    }

    private String formatDate(String dateObject) {
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = "";
        try {
            date = dateFormat.format(dateFormatJSON.parse(dateObject));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String formatTime(String dateObject) {
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = "";
        try {
            time = timeFormat.format(dateFormatJSON.parse(dateObject));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    static class ViewHolder {
        TextView titleView;
        TextView sectionView;
        TextView authorView;
        TextView dateView;
    }
}
