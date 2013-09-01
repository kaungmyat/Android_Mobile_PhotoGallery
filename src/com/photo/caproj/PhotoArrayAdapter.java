package com.photo.caproj;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.photo.caproj.R;

class PhotoArrayAdapter extends ArrayAdapter<PhotoItem> {
	private final Activity context;
	private final PhotoItem[] pItm;

	public PhotoArrayAdapter(Activity context, PhotoItem[] pItm) {
	    super(context, R.layout.row, pItm);
	    this.context = context;
	    this.pItm = pItm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    View rowView = inflater.inflate(R.layout.row, null);
	    ImageView imgView = (ImageView) rowView.findViewById(R.id.imageView1);
	    TextView caption = (TextView) rowView.findViewById(R.id.textView1);
	    TextView location = (TextView) rowView.findViewById(R.id.textView2);
	    try {
            caption.setText(pItm[position].get("caption").toString());
	        location.setText(pItm[position].get("location").toString());
	        String imgurl = "http://137.132.247.137/images/thumb-" + pItm[position].get("file").toString();
	        imgView.setImageBitmap(PhotoHelper.getRemoteImage(imgurl));
	    } catch (Exception e) {
	    }
	    return rowView;
	  }
} 
