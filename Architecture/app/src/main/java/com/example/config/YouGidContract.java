package com.example.config;

import android.provider.BaseColumns;

public class YouGidContract {
    public static final String AUTHORITY = "com.example.rest.YouGidContentProvider";
    public static final String DATABASE = "yougid.db";
    public static final int DATABASE_VERSION = 1;

    public static class Marks implements BaseColumns {
        public static final String TABLE_NAME = "marks";
        public static final String SNIPPET = "snippet";
        public static final String TEXT = "text";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lon";
        public static final String OWNER = "owner_id";

        public static final String DIR_TYPE = "vnd.android.cursor.dir/vnd.yougid.marks";
        public static final String ITEM_TYPE = "vnd.android.cursor.dir/vnd.yougid.mark";

        public static final int DIR = 1;
        public static final int ITEM = 2;
        public static final int ASYNCDIR = 3;
    }

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String NAME = "name";
        public static final String SECOND_NAME = "s_name";
        public static final String FRIEND = "friend_id";
    }
}
