package com.example.android.newsappstage1;

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
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView categoryView = listItemView.findViewById(R.id.section);
        categoryView.setText(currentNews.getSection());

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());

        TextView dateView = listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(currentNews.getDate());
        dateView.setText(formattedDate);

//        TextView timeView = listItemView.findViewById(R.id.time);
//        String formattedTime = formatTime(currentNews.getDate());
//        timeView.setText(formattedTime);

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
}
