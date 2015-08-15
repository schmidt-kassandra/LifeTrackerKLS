package edu.rosehulman.schmidkl.lifetrackerkls;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);		
		ImageView imageView = (ImageView) findViewById(R.id.imageView);		
		Intent data = getIntent();	
		Bitmap image = BitmapFactory.decodeFile(data.getStringExtra(ItemActivity.KEY_IMAGE_PATH));
		imageView.setImageBitmap(image);
	}
}