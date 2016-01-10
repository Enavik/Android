package com.example.draggablepanel;

import java.util.concurrent.TimeUnit;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	public static String tag = "Service:MyService";
	private PendingIntent pint;
	private Intent brIntent = new Intent(MainActivity.BROADCAST_ACTION);

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(tag, "Bind service - MyService");
		doWork(intent);
		return new Binder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(tag, "Create service - MyService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(tag, "Start command service - MyService - " + startId);
		doWork(intent);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(tag, "Destroy service - MyService");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(tag, "Unbind service - MyService");
		return true;
	}
	
	public void doWork(Intent intent) {
		pint = intent.getParcelableExtra("data");
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(5);
					brIntent.putExtra("data", "all work!");
					sendBroadcast(brIntent);
					pint.send(MyService.this, MainActivity.STATUS, new Intent().putExtra("data", "all work!"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
	}
	

}
