package com.example.architecture;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
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
import static com.example.config.YouGidContract.*;

public class YouGidActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ListView list;
    private Uri uri = Uri.parse("content://"+ AUTHORITY+"/"+Marks.TABLE_NAME);
    private String[] from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_gid);
        from = new String[]{Marks._ID, Marks.SNIPPET, Marks.TEXT};
        Cursor c = getContentResolver().query(uri, from, null, null, null);
        int[] to = {R.id.item_id, R.id.item_title, R.id.item_text};
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(new SimpleCursorAdapter(this, R.layout.list_item, c, from, to, 0));
        getLoaderManager().initLoader(0, null, this);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int cnt = getContentResolver().delete(uri, Marks._ID+"=?", new String[]{String.valueOf(((TextView)view.findViewById(R.id.item_id)).getText())});
                Log.d("YouGidActivity", "Delete count " + cnt);
                return true;
            }
        });
        Button btn = (Button) findViewById(R.id.add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues val = new ContentValues();
                val.put(Marks.SNIPPET, "Title");
                val.put(Marks.TEXT, "TEXT");
                val.put(Marks.LONGITUDE, 1.0);
                val.put(Marks.LATITUDE, 1.0);
                val.put(Marks.OWNER, 1);
                getContentResolver().insert(uri, val);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uri, from, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((SimpleCursorAdapter)list.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter)list.getAdapter()).swapCursor(null);
    }
}
