package com.example.architecture;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.R;
import com.example.rest.LstLoaderCallBack;
import com.example.rest.TLoader;
import com.example.rest.Tests;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private TextView tView;
    private TextView tView2;
    private Button btn;
    private Button btn2;
    private int tick = 0;
    public static ListView list;
    public static Context ctx;
    public static final Uri CONTACT_URI = Uri
            .parse("content://com.example.rest.providers.TContentProvider/test");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        tView = (TextView) findViewById(R.id.textview);
        tView2 = (TextView) findViewById(R.id.textview2);
        if (savedInstanceState != null && savedInstanceState.containsKey("text"))
            tView2.setText(savedInstanceState.getString("text"));
        btn = (Button) findViewById(R.id.act1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().getLoader(0).forceLoad();
            }
        });
        btn2 = (Button) findViewById(R.id.act2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ATask().execute();
            }
        });
        getLoaderManager().initLoader(0, null, this);

        Button add = (Button) findViewById(R.id.add_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(Tests.Test.TITLE, "Title 1 =)");
                cv.put(Tests.Test.TEXT, "It's working!!!");
                getContentResolver().insert(CONTACT_URI, cv);
            }
        });

        list = (ListView) findViewById(R.id.list_view);
        String[] from = {Tests.Test.TEST_ID,Tests.Test.TITLE, Tests.Test.TEXT};
        int[] to = {R.id.item_id, R.id.item_title, R.id.item_text};

        Cursor c = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        list.setAdapter(new SimpleCursorAdapter(this, R.layout.list_item, c, from, to, 0));

        getLoaderManager().initLoader(1, null, new LstLoaderCallBack(this, list));
        Log.d("MainActivityLog", "" + getLoaderManager().getLoader(1).hashCode());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, YouGidActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
        outState.putString("text", "Now tick is " + tick);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new TLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, String data) {
        tView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private class ATask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Now tick is " + tick++;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tView2.setText(s);
        }
    }
}
