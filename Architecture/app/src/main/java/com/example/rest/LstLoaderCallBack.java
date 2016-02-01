package com.example.rest;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.architecture.MainActivity;

public class LstLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor>{
    private Context ctx;
    private ListView list;

    public LstLoaderCallBack(Context _ctx, ListView _list) {
        Log.d("ListLoaderLog", "Create loader callback from constructor");
        this.ctx = _ctx;
        list = _list;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("ListLoaderLog", "onCreateLoader() run " + id);
        return new CursorLoader(ctx, MainActivity.CONTACT_URI, new String[]{Tests.Test.TEST_ID,Tests.Test.TITLE, Tests.Test.TEXT}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("ListLoaderLog", "onLoadFinished() run " + ctx.hashCode());
        ((SimpleCursorAdapter)list.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter)list.getAdapter()).swapCursor(null);
    }

}
