package com.fabriscorol.constitutiondubenin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class FlashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_screen);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(3000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally {
					Intent main = new Intent("com.fabriscorol.constitutiondubenin.TitleListActivity");
					startActivity(main);						
				}
			}
		};
		timer.start();
	}	

}