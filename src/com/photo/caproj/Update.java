package com.photo.caproj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.photo.caproj.R;

public class Update extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(ThreadPolicy.LAX);
		setContentView(R.layout.update);
		Button btnUpdate = (Button) findViewById(R.id.button1);
		Bundle b = getIntent().getExtras();
		final String rowid = b.getString("id");
		final String caption = b.getString("caption");
		final String location = b.getString("location");
		
		final EditText t1 = (EditText) findViewById(R.id.caption);
		final EditText t2 = (EditText) findViewById(R.id.location);
		t1.setText(caption);
		t2.setText(location);
		Log.i("Location", location);
		
		btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				update(rowid, t1.getText().toString(), t2.getText().toString());
				finish();
				
			}
		});
	}

	public void update(String id, String caption, String location) {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("id", id));
		qparams.add(new BasicNameValuePair("caption", caption));
		qparams.add(new BasicNameValuePair("location", location));
		URI uri;
		try {
			uri = URIUtils.createURI("http", "137.132.247.137", -1,
					"/python/update", URLEncodedUtils.format(qparams, "UTF-8"),
					null);
			HttpGet httpget = new HttpGet(uri);
			HttpResponse response = new DefaultHttpClient().execute(httpget);
			InputStream inp = response.getEntity().getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	String myLocation() {
	    StringBuffer sb = new StringBuffer();
	    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    List<String> providers = lm.getProviders(true);
	    //for (String provider : providers) {
		  //  sb.append(provider); sb.append(":\n");
		    lm.requestLocationUpdates(providers.get(0), 1000, 0, new LocationListener() {
	        public void onLocationChanged(Location location) {}
	        public void onProviderDisabled(String provider){}
	        public void onProviderEnabled(String provider){}
	        public void onStatusChanged(String provider, int status, Bundle extras){}
	      });
	      Location loc = lm.getLastKnownLocation(providers.get(0));
	      if (loc == null) sb.append("No location");
	      else sb.append(String.format("%f,%f", 
	                                loc.getLatitude(), loc.getLongitude()));
	      //String ret = latlngStringFromLocation(this, loc.getLatitude(), loc.getLongitude());
	    //}
	    return(ret);
	}
	
	
	String myGeo() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation("network");
	    Geocoder gc = new Geocoder(this, Locale.getDefault());
	    List<Address> addresses = null;
	    try {
	      addresses = gc.getFromLocation(loc.getLatitude(),
	                                     loc.getLongitude(), 1);
	    } catch (IOException e) {
	    }
	    return addresses.get(0).getAddressLine(0).toString();
	} */
}
