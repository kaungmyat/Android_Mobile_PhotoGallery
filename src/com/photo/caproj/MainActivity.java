package com.photo.caproj;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

//import com.myorg.CustomerDbAdapter;
//import com.myorg.CustomerDetails;
import com.photo.caproj.PhotoItem;
import com.photo.caproj.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.provider.MediaStore;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.MediaColumns;

public class MainActivity extends ListActivity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int REQUEST_CODE = 1;
	PhotoItem fotoItem;
	private Bitmap bitmap;
	PhotoItem[] pItm;
	ImageView imageView;
	Uri imageUri;
	TextView txt;

	@Override
	protected void onResume() {
		//super.onCreate(savedInstanceState);
		super.onResume();
		StrictMode.setThreadPolicy(ThreadPolicy.LAX);
		pItm = PhotoHelper.PhotoRead();
		// values = Arrays.copyOf(pItm, pItm.length, String[].class);
		// List<PhotoItem> values = new
		// ArrayList<PhotoItem>(Arrays.asList(pItm));
		PhotoArrayAdapter adapter = new PhotoArrayAdapter(this, pItm);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.browse:

			return (true);
		case R.id.take_photo:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			imageUri = Uri.fromFile(new File("/sdcard/", "IMG_"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, R.id.imageView1);
			return (true);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (resultCode == RESULT_OK) {
			 if (requestCode == R.id.imageView1) {
				 Uri img = (data == null ? imageUri : data.getData());
				 Transfer.uploadFile("FileUpload", myGeo(),
						 getRealPathFromURI(img));
				 Intent i = new Intent(this, MainActivity.class);
				 startActivity(i);
			 }
		 }
		 
	}

	private String getRealPathFromURI(Uri uri) {
		String filePath;
		if (uri != null && "content".equals(uri.getScheme())) {
			Cursor cursor = this
					.getContentResolver()
					.query(uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();
			filePath = cursor.getString(0);
			cursor.close();
		} else {
			filePath = uri.getPath();
		}
		return (filePath);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.update:
			AdapterContextMenuInfo adpInfo = (AdapterContextMenuInfo) item.getMenuInfo();
			int index = (int) adpInfo.id;
			PhotoItem p = pItm[index];
			
			Intent i = new Intent(this, Update.class);
			i.putExtra("id", p.get("id"));
	        i.putExtra("caption", p.get("caption"));
	        i.putExtra("location", p.get("location"));
			
			startActivity(i);
			
			return true;
			
		case R.id.delete:
			// for ListView, can do following for index to data
	         AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	         int index1 = (int) info.id;
	         PhotoItem m = pItm[index1];
	         // do something with m
//	         Bundle bundle = new Bundle();
//	         bundle.putString("id", m.get("id"));
//	         bundle.putString("caption", m.get("caption"));
//	         bundle.putString("location", m.get("location"));
//	         Intent i = new Intent(this, PhotoDetail.class);
//	         i.putExtra("delObj", bundle);
//	         startActivity(i);
	         delete(Integer.parseInt(m.get("id")));
	         return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ImageViewActivity.class);
		intent.putExtra("selObj", pItm[position]);
		startActivity(intent);
		
	}
	
//	String myLocation() {
//	    StringBuffer sb = new StringBuffer();
//	    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//	    List<String> providers = lm.getProviders(true);
//	      lm.requestLocationUpdates(providers.get(0), 1000, 0, new LocationListener() {
//	        public void onLocationChanged(Location location) {}
//	        public void onProviderDisabled(String provider){}
//	        public void onProviderEnabled(String provider){}
//	        public void onStatusChanged(String provider, int status, Bundle extras){}
//	      });
//	      Location loc = lm.getLastKnownLocation(providers.get(0));
//	      if (loc == null) sb.append("No location");
//	      else sb.append(String.format("%f,%f", 
//	                                loc.getLatitude(), loc.getLongitude()));
//	      //String ret = latlngStringFromLocation(this, loc.getLatitude(), loc.getLongitude());
//	    //}
//	    return(sb.toString());
//	      //return ret;
//	}
	
	String myGeo() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation("network");
	    Geocoder gc = new Geocoder(this, Locale.getDefault());
	    List<Address> addresses = null;
	    try {
	      addresses = gc.getFromLocation(loc.getLatitude(),
	                                     loc.getLongitude(), 1);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return "error" + addresses;
	    }
	    return addresses.get(0).getAddressLine(0).toString();
	}
	
	void delete(int id) {
		   List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		   qparams.add(new BasicNameValuePair("id", String.valueOf(id)));
		   try{
			   URI uri = URIUtils.createURI("http", "137.132.247.137", -1, "/python/delete", 
					   URLEncodedUtils.format(qparams, "UTF-8"), null);

			   HttpGet httpget = new HttpGet(uri);
			   HttpResponse response = new DefaultHttpClient().execute(httpget);
			   InputStream inp = response.getEntity().getContent();
		   }catch(Exception e){
			  e.printStackTrace();
		  }
		   Intent i = new Intent(this, MainActivity.class);
		   startActivity(i);
		}
	
	
}