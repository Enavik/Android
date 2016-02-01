package com.example.rest;

import android.net.Uri;
import android.provider.BaseColumns;

public class Tests {

    public Tests() {
    }

    public static final class Test implements BaseColumns {
        private Test() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + TContentProvider.AUTHORITY + "/notes");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.notes";

        public static final String TEST_ID = "_id";

        public static final String TITLE = "title";

        public static final String TEXT = "text";
    }

}