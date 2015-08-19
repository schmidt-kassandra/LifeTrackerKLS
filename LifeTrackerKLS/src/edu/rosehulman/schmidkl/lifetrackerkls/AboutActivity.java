package edu.rosehulman.schmidkl.lifetrackerkls;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author schmidkl
 * An activity that loads a short about LifeTracker in Main Activity
 *
 */

public class AboutActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
}