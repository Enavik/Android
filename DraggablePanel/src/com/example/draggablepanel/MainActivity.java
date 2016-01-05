package com.example.draggablepanel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static int cnt = 0;
	static int dst = 0;
	private String[] titles = {"Редактировать", "Удалить", "Пригласить друзей", "Рассказать друзьям"};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn = (Button) findViewById(R.id.btn1);
		final LinearLayout ll = (LinearLayout) findViewById(R.id.frame2);
		final TextView title = (TextView) findViewById(R.id.text_title);
		
		final ListView listView = (ListView) findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.text_view_item, titles);
		listView.setAdapter(adapter);
		
//		final ImageView view = (ImageView) findViewById(R.id.v_image);
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ll.animate().translationY(ll.getHeight());
//			}
//		});
		
		ll.animate().setListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				if (ll.getVisibility() == View.GONE)
					ll.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if (ll.getVisibility() == View.VISIBLE && ll.getTranslationY() == ll.getHeight())
					ll.setVisibility(View.GONE);
			}

		});
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (ll.getVisibility() == View.GONE) {
//					title.setText(""+cnt++);
					ll.animate().translationY(0);
				} else
					ll.animate().translationY(ll.getHeight());
			}
		});
		
		ll.setOnTouchListener(new OnTouchListener() {
			private double x1;
			private double y1;
			private double x2;
			private double y2;
			private double delta = 0.5;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x1 = event.getX();
						y1 = event.getY();
						break;
					case MotionEvent.ACTION_UP:
						x2 = event.getX();
						y2 = event.getY();
						Log.i("y1 y2", ""+(y2-y1));
						if (y2 - y1 > 0) {
							double vx = x2-x1;
							double vy = y2-y1;
							double nvx = 0;
							double nvy = x1*x2+y1*y2;
							double cos = Math.acos((vx*nvx+vy*nvy)/(Math.sqrt(vx*vx+vy*vy)*Math.sqrt(nvx*nvx+nvy*nvy)));
							if (cos > 0 && cos < delta) {
								ll.animate().translationY(ll.getHeight());
							}
							Log.i("COS", ""+cos);
						}
						break;
				};
//				Log.i("TouchEvent", event.toString());
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
