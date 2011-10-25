package com.silvermongoose.ecc_ff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.silvermongoose.ecc_ff.Get_Users.LocalBinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ECC_FF extends Activity {
	
	private Tech_DB_Adapter dbHelper;
	private Cursor cursor;
	Get_Users get_users;
	boolean mBound = false;
	ArrayList<Object> techs;
	static final int DIALOG_PASSWORD = 0;
	static final int DIALOG_LOADING = 1;
	private int selectedID;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
			techs = getUsers("http://dev.cneal.net/eccticket/api/getTechs.php?techActive=1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      /*  dbHelper = new Tech_DB_Adapter(this);
        dbHelper.open();
        
       for(int x = 0; x < techs.size(); x++) {
       	Tech tempTech = techs.get(x);
       	dbHelper.createTech(tempTech.getTechID(), tempTech.getTechFirstName(), tempTech.getTechLastName(), tempTech.getTechEmail(), tempTech.getTechPhone());
       } */
       
       Spinner spinner = (Spinner) findViewById(R.id.login_spinner);
       arrayListAdapter adapter = new arrayListAdapter(techs);
       spinner.setAdapter(adapter);
       
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           /**
            * Called when a new item was selected (in the Spinner)
            */
           public void onItemSelected(AdapterView<?> parent,
        		   View view, int pos, long id) {
        	   //ImageView image = (ImageView) dialog.findViewById(R.id.image);
        	   //image.setImageResource(R.drawable.android);
        	   if (id > 0) {
        		   showDialog(DIALOG_PASSWORD);
        		   selectedID = (int) id;
        	   }
           }

           public void onNothingSelected(AdapterView parent) {
             // Do nothing.
           }
       });
    }
    
    protected void onStart() {
    	super.onStart();
    }
    
    
    protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
    
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        Button login_button;
        final EditText password_field;
        switch(id) {
        case DIALOG_PASSWORD:
    	   	//Context mContext = getApplicationContext();
    	    dialog = new Dialog(this);
    	   	dialog.setContentView(R.layout.password_dialog);
    	   	dialog.setTitle("Enter Password:");
    	  //Set Password Field
    	   	password_field = (EditText) dialog.findViewById(R.id.passwordField);
    	   	password_field.setText("");
    	  //Set Login Button
        	login_button = (Button) dialog.findViewById(R.id.loginButton);
        	login_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String password = (String) password_field.getText().toString();
					Log.v("test", "----------------------------" + password);
					
					String result = MD5(password);
					Log.v("test", "----------------------------" + result);
					if(checkPassword(result)){
						password_field.setText("");
					} else {
						showDialog(DIALOG_PASSWORD);
						password_field.setText("");
					}
					dismissDialog(DIALOG_LOADING);
				}
			});
            break;
        case DIALOG_LOADING:
        	dialog = new Dialog(this);
        	dialog.setContentView(R.layout.loading_dialog);
        	break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
    public ArrayList<Object> getUsers(String uri) throws Exception {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(uri));
			
			String response = client.execute(request, new BasicResponseHandler());
			
			Gson gson = new GsonBuilder().create();
			
			Type collectionType = new TypeToken<Collection<Tech>>(){}.getType();
			Collection<Tech> ints2 = gson.fromJson(response, collectionType);
			
			ArrayList<Object> test = new ArrayList<Object>();
			test.addAll(ints2);
			System.out.println("test");
			return test;
			
			/*Spinner spinner = (Spinner) findViewById(R.id.login_spinner);
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.login_array, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());*/
			
		} finally {
			
		}
	}
    
    public boolean checkPassword(String md5Password) {
    	dismissDialog(DIALOG_PASSWORD);
    	showDialog(DIALOG_LOADING);
    	boolean check = false;
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://dev.cneal.net/eccticket/api/login.php");
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);   
        nameValuePairs.add(new BasicNameValuePair("username", "20"));
        nameValuePairs.add(new BasicNameValuePair("password", md5Password));
        
        try {
        	post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	try {
				String response = client.execute(post, new BasicResponseHandler());
				Log.v("test", "-----------------------------" + response);
				if(response.equals("null"))
				{
					check = false;
				} else {
					check = true;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        }
    	
    	return check;
    }
    
    private class arrayListAdapter implements SpinnerAdapter {
    	
    	ArrayList<Object> data = null;
    	
    	public arrayListAdapter (ArrayList<Object> data_in)
    	{
    		data = data_in;
    	}
    	
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return android.R.layout.simple_spinner_dropdown_item;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = new TextView(getApplicationContext());
            v.setTextColor(Color.BLACK);
            v.setText(data.get(position).toString());
            return v;
        }

		@Override
		public int getViewTypeCount() {
			return android.R.layout.simple_spinner_dropdown_item;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) 
		{
		  if (convertView == null)
		  {
		    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
		  }

		  TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
		  textView.setText(data.get(position).toString());

		  return convertView;
		}
    }
    
    public String MD5(String md5) {
    	   try {
    	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
    	        byte[] array = md.digest(md5.getBytes());
    	        StringBuffer sb = new StringBuffer();
    	        for (int i = 0; i < array.length; ++i) {
    	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
    	       }
    	        return sb.toString();
    	    } catch (java.security.NoSuchAlgorithmException e) {
    	    }
    	    return null;
    	}
}