package com.XC.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MainActivity extends Activity {

	private FlowLayout flowLayout;
	private String[] mVals = new String[] { "Hello", "Android", "Weclome Hi ",
			"Button", "TextView", "Hello", "Android", "Weclome",
			"Button ImageView", "TextView", "Helloworld", "Android",
			"Weclome Hello", "Button Text", "TextView" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flowLayout = (FlowLayout) findViewById(R.id.fl);
		LayoutInflater mInflater = LayoutInflater.from(this);
		for (int i = 0; i < mVals.length; i++) {
			TextView tv = (TextView) mInflater
					.inflate(R.layout.tv, flowLayout, false);
			tv.setText(mVals[i]);
			flowLayout.addView(tv);
		}
	}

}
