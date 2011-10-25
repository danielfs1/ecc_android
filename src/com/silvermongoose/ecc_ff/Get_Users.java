package com.silvermongoose.ecc_ff;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Get_Users extends Service {

	private final IBinder mBinder = new LocalBinder();

	//getUsers("http://dev.cneal.net/eccticket/api/getTechs.php");

	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public Collection<Tech> getUsers(String uri) throws Exception {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(uri));
			
			String response = client.execute(request, new BasicResponseHandler());
			
			Gson gson = new GsonBuilder().create();
			
			Type collectionType = new TypeToken<Collection<Tech>>(){}.getType();
			Collection<Tech> ints2 = gson.fromJson(response, collectionType);
			
			System.out.println(response);
			
			return ints2;
			
			/*Spinner spinner = (Spinner) findViewById(R.id.login_spinner);
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.login_array, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());*/
			
		} finally {
			
		}
	}
	
	public class LocalBinder extends Binder {
        Get_Users getService() {
            // Return this instance of LocalService so clients can call public methods
            return Get_Users.this;
        }
    }

}
