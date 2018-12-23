package com.example.mynewsapp.mynewsapp;

import android.graphics.drawable.Drawable;

public class News {
        private String section;
        private String title;
        private String url;
        private String date;
        private String author;
        private Drawable image;

        public News(String section, String title, String url, String date,String author, Drawable image) {
            this.section = section;
            this.title = title;
            this.url = url;
            this.date = date;
            this.author = author;
            this.image = image;
        }

        public String getSection() {
            return section;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getDate() {
            return date;
        }

        public String getAuthor(){ return author;}

        public Drawable getImage() {
            return image;
        }
}
