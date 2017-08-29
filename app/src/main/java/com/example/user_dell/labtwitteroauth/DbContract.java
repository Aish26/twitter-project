package com.example.user_dell.labtwitteroauth;

import android.provider.BaseColumns;

/**
 * Created by USER - DELL on 25-08-2017.
 */
public class DbContract {

    public static final class MenuEntry implements BaseColumns {

        public static final String TABLE_NAME = "twitterfeed";
        public static final String TIME = "time";
        public static final String Id = "Id";
        public static final String LINK = "link";
    }
}