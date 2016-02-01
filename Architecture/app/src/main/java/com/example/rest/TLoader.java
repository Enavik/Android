package com.example.rest;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.concurrent.TimeUnit;

public class TLoader extends AsyncTaskLoader<String>{
    private static int tick = 0;
    public TLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Now tick is " + tick++;
    }
}
