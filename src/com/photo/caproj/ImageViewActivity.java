package com.photo.caproj;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
	ImageView imageView;
	HashMap<String, String> photoItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		Bundle b = getIntent().getExtras();
		photoItem = (HashMap<String, String>) b.get("selObj");
		String uri = "http://137.132.247.137/images/" + photoItem.get("file").toString();
	    imageView.setImageBitmap(PhotoHelper.getRemoteImage(uri));

	}
}
