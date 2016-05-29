package com.lhp.toggleview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.lhp.toggleview.ToggleView.OnSwitchStateChangedListener;

public class MainActivity extends Activity {

	private ToggleView toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toggle = (ToggleView) findViewById(R.id.toggle);

		toggle.setSwitchBackgroundResource(R.drawable.switch_background);

		toggle.setSlideButtonResource(R.drawable.slide_button);

		toggle.setSwitchState(false);
		// 设置开关状态的监听
		toggle.setOnSwitchStateChangedListener(new OnSwitchStateChangedListener(){

			@Override
			public void onStateChanged(boolean state) {
				Toast.makeText(getApplicationContext(), "Switch State:" + state, 0).show();
			}
			
		});

	}

}
