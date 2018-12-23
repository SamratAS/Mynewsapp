package com.example.mynewsapp.mynewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    // reference RecyclerView
    static class ViewHolder {
        private TextView sectionView;
        private TextView titleView;
        private TextView authorView;
        private TextView dateView;
        private ImageView thumbImage;
    }

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);
            holder = new ViewHolder();
            holder.sectionView = convertView.findViewById(R.id.section);
            holder.titleView = convertView.findViewById(R.id.title);
            holder.authorView = convertView.findViewById(R.id.author);
            holder.dateView = convertView.findViewById(R.id.time);
            holder.thumbImage = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        News currentNews = getItem(position);

        holder.thumbImage.setImageDrawable(currentNews.getImage());

        holder.sectionView.setText(currentNews.getSection());

        holder.titleView.setText(currentNews.getTitle());

        holder.authorView.setText(currentNews.getAuthor());

        holder.dateView.setText(currentNews.getDate());

        return convertView;
    }
}
