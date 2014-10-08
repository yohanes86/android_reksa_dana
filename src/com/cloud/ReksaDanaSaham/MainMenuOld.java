package com.cloud.ReksaDanaSaham;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.ReksaDanaSaham.data.JenisVO;
import com.cloud.ReksaDanaSaham.helper.DatabaseHandler;

public class MainMenuOld extends Activity implements OnItemSelectedListener {
	private static final String TAG = "MainMenuActivity";
	private final HttpClient client = new DefaultHttpClient();

	private ProgressDialog dialog;
	// Spinner element
    Spinner spinner;
         
    // Text View
    TextView text;
    
    // Spinner element
    Spinner spinnerType;
    
    Button btnNextScreen; 
    
    Document doc;
    
    List<String> listData = new ArrayList<String>();
    
    DatabaseHandler db = new DatabaseHandler(this);
    
    int flag = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		 // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
       
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();   
        
//        checkInternet();
        new HandleHttpTask().execute("https://www.google.com/");
        
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        btnNextScreen = (Button) findViewById(R.id.btnProcess);
        
        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
            	 //Starting a new Intent
            	Intent nextScreen = null;
            	
            	
//            	if(!hasConnection(getApplicationContext())){
//            		
//            	}
//               
            	            	
            	if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.title_reksa_dana))){
            		nextScreen = new Intent(getApplicationContext(), ReksaDana.class);
            	}else if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.title_saham))){
            		nextScreen = new Intent(getApplicationContext(), Saham.class);
            	}else{
            		Log.e(TAG, "Invalid Spinner Item Selected");
            	}
 
                //Sending data to another Activity
                nextScreen.putExtra("Jenis", spinner.getSelectedItem().toString());
                nextScreen.putExtra("Type", spinnerType.getSelectedItem().toString());
 
                Log.d(TAG, spinner.getSelectedItem().toString() +" - "+ spinnerType.getSelectedItem().toString());
 
                startActivity(nextScreen);
            }
        });
	}
	
	 /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // Spinner Drop down elements
        List<String> labelTypes = new ArrayList<String>();///db.getAllLabels();
//        labelTypes.add("- Please Select -");
        labelTypes.add(getResources().getString(R.string.title_reksa_dana));
        labelTypes.add(getResources().getString(R.string.title_saham));
        Log.d(TAG, "Label Type: " + labelTypes.toString());
 
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labelTypes);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    
    
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerType(String url) {
//        // database handler
//        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
 
        // Spinner Drop down elements
//        List<String> labelTypes = new ArrayList<String>();///db.getAllLabels();
//        labelTypes.add("Saham1");
//        labelTypes.add("Saham2");
//        Log.d(TAG, "Label Type: " + listData.toString());
    	
    	/* Reksa Saham Type */
//    	listData = new ArrayList<String>();
//    	Document doc = null;
//		try {
//			doc = Jsoup.connect(url).get();
//			Elements els1 = doc.select("a[href][target]");
//			for (int i = 1; i < els1.size(); i++) {
//				Element element = els1.get(i);
//				listData.add(element.ownText());
//			}
//			Log.d(TAG, "Label Type: " + listData.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	 /* call HTTP CLIENT */
		 new HandleHttpTask().execute(url);
//		 Log.d(TAG, "list Data: " +listData.toString());
 
        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listData);
 
        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
//        spinnerType.setAdapter(dataAdapter);
    }
	
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        
		String label = "";
        switch (parent.getId()) {
		case R.id.spinner:
			// On selecting a spinner item
	         label = parent.getItemAtPosition(position).toString();
	        
	        // Showing selected spinner item
//	        Toast.makeText(parent.getContext(), "You selected: " + label,
//	                Toast.LENGTH_LONG).show();
//	        showToast("Spinner1: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
	        
	        // Spinner element
       	 	spinnerType = (Spinner) findViewById(R.id.spinnerType);
       	 	// Spinner click listener
       	 	spinnerType.setOnItemSelectedListener(this);
       	 	
//       	 List<JenisVO> listJenis = db.getAllJenis();
       	 	
       	 	listData = new ArrayList<String>();
	        
	        if(label.equalsIgnoreCase(getResources().getString(R.string.title_reksa_dana))){
	        	 
	             /* Reksa Dana */
	        	 text = (TextView) findViewById(R.id.text);
	        	 text.setText("Jenis Reksa Dana");
	        	 text.setTextSize(18);
	        	 
	        	 /* set prompt spinner*/
	        	 spinnerType.setPrompt("Jenis Reksa Dana");
	        	 
	        	// Loading spinner data from database
//	        	 loadSpinnerType(URL_REKSA_DANA);
	        	 
	        	 /* buka ini utk async */
//	        	 flag = 1;
//	        	 new HandleHttpTask().execute(getResources().getString(R.string.url_reksa_dana));
	        	 
	        	 
//	        	 listData = db.getAllListReksaDana();
//	        	 if(listData.size() == 0){
//	        		 Log.d("TAG", "Insert Reksa Dana Data to DB");
//	        		 Resources res = getResources();
//	        		 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_reksa_dana)));
//	        		 for (String listReksaDana : listData) {
//		        		 JenisVO jenis = new JenisVO();
//			        	 jenis.setReksaDana(listReksaDana.toString());
//			        	 db.addReksaDana(jenis);
//		        	 }
//	        	 }else{
//	        		 Log.d("TAG", "Load RD From DB");
//	        		 listData = db.getAllListReksaDana();
//	        	 }
	        	 
	        	 listData = new ArrayList<String>();
	        	 Resources res = getResources();
	        	 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_reksa_dana)));
//	        	 listData.add(getString(R.string.pend_tetap));
//	        	 listData.add(getString(R.string.campuran));
//	        	 listData.add(getString(R.string.saham));
//	        	 listData.add(getString(R.string.pasar_uang));
//	        	 listData.add(getString(R.string.terproteksi));
//	        	 listData.add(getString(R.string.indeks_etf));
//	        	 listData.add(getString(R.string.dollar));
//	        	 listData.add(getString(R.string.syariah));
	        	 
	        	
	        	
//	        	 text.setVisibility(View.GONE);
//	        	 text.setVisibility(View.VISIBLE);
	        	
	        }else{
	        	 /* SAHAM */
	        	 text = (TextView) findViewById(R.id.text);
	        	 text.setText("Jenis Saham"); 
	        	 text.setTextSize(18);
	        	 
	        	 /* set prompt spinner*/
	        	 spinnerType.setPrompt("Jenis Saham");
	        	 
	        	// Loading spinner data from database
//	        	 loadSpinnerType(URL_SAHAM);
	        	 
	        	 /* buka ini utk async */
//	        	 flag = 2;
//	        	 new HandleHttpTask().execute(getResources().getString(R.string.url_saham));
	        	 
//	        	 listData = db.getAllListSaham();
//	        	 if(listData.size() == 0){
//	        		 Log.d("TAG", "Insert Saham Data to DB");
//	        		 Resources res = getResources();
//	        		 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_saham)));
//	        		 for (String listSaham : listData) {
//		        		 JenisVO jenis = new JenisVO();
//			        	 jenis.setSaham(listSaham.toString());
//			        	 db.addReksaDana(jenis);
//		        	 }
//	        	 }else{
//	        		 Log.d("TAG", "Load Saham From DB");
//	        		 listData = db.getAllListSaham();
//	        	 }
	        	 
	        	 
	        	 listData = new ArrayList<String>();
	        	 Resources res = getResources();
	        	 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_saham)));
//	        	 listData.add(getString(R.string.lq45));
//	        	 listData.add(getString(R.string.bisnis27));
//	        	 listData.add(getString(R.string.kompas100));
//	        	 listData.add(getString(R.string.all));	        	 
	        	 
	        	 
	        	
	        }
	        
	        /* hapus ini utk async task */ 
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, listData);
	 
	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	        // attaching data adapter to spinner
	        spinnerType.setAdapter(dataAdapter);
	        
       	 	spinnerType.setOnItemSelectedListener(new OnItemSelectedListener() {
          	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//          		showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
          	}
          	
          	public void onNothingSelected(AdapterView<?> parent) {
//          		showToast("Spinner2: unselected");
          	}
          	
       	 	});
       	 	/* sampe sini */ 
			break;	
		default:
			break;
		}
        
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
 
    }
    
    /** Helper Functions */
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_saham, menu);
		return true;
	}
	
	
	private class HandleHttpTask extends AsyncTask<String, Void, Void> {
		
		private String content;

		protected void onPreExecute() {
			// Toast toast = Toast.makeText(getApplicationContext(),
			// getResources().getString(R.string.lbl_loading),
			// Toast.LENGTH_SHORT);
			// toast.show();
//			dialog = new ProgressDialog(MainMenu.this);
//			dialog.setIndeterminate(true);
//			dialog.setTitle("Loading");
//			// dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
//			dialog.setCancelable(true);
//			dialog.setMessage(getResources().getString(R.string.label_loading));
//			dialog.show();
		}

		protected Void doInBackground(String... params) {
			try {
				HttpParams httpParameters = client.getParams();
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 30000);
				HttpConnectionParams.setSoTimeout(httpParameters, 30000);
				Log.v(TAG, "PARAM: " + params[0]);
				HttpGet httpget = new HttpGet(params[0]);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				content = client.execute(httpget, responseHandler);
			} catch (ClientProtocolException e) {
				Log.v(TAG, "ClientProtocolException: " + e);
				content = null;
			} catch (IOException e) {
				Log.v(TAG, "IOException: " + e);
				content = null;
			} catch (Exception e) {
				Log.v(TAG, "Exception: " + e);
				content = null;
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
//			Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
//			receiveResponse(content);
			isConnected(content);
		}

	}
	
	private void isConnected(String response){
		Log.v(TAG, "receiveResponse: " + response);
		// Check Internet Connection
		ImageView imageView = (ImageView) findViewById(R.id.InternetlogoId);
		if (response != null) { 
			imageView.setImageResource(R.drawable.success);
		}else {
			imageView.setImageResource(R.drawable.fail);
		}
//		if (response != null) { 
//			return true;
//		}
//		return false;
	}
	
	private void receiveResponse(String response) {
		Log.v(TAG, "receiveResponse: " + response);
		if (response != null) { 
			try {
//				listData = new ArrayList<String>();
//				doc = Jsoup.parse(response);
//				/* Reksa Dana Type */
//		    	Elements els1 = doc.select("a[href][target]");
//		    	Element element = null;
//				for (int i = 1; i < els1.size(); i++) {
//					element = els1.get(i);
//					listData.add(element.ownText());
//				}
//				Log.d(TAG, "List Data: " + listData.toString());	
	
				Resources res = getResources();
				listData = new ArrayList<String>(); 
				if(flag == 1){
					Log.d("TAG", "Flag 1");
					listData = db.getAllListReksaDana();
					if(listData.size() == 0){
						 Log.d("TAG", "Insert Reksa Dana Data to DB");
		        		 	listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_reksa_dana)));
//						 	doc = Jsoup.parse(response);
//						 	Elements els1 = doc.select("a[href][target]");
//					    	Element element = null;
//							for (int i = 1; i < els1.size(); i++) {
//								element = els1.get(i);
//								listData.add(element.ownText());
//							}
							 for (String listReksaDana : listData) {
				        		 JenisVO jenis = new JenisVO();
					        	 jenis.setReksaDana(listReksaDana.toString());
					        	 db.addReksaDana(jenis);
				        	 }
					}else{
						Log.d("TAG", "Load Reksa Dana From DB");
						listData = db.getAllListReksaDana();
					}
				}
				
				else if(flag == 2){
					Log.d("TAG", "Flag 2");
					listData = db.getAllListSaham();
					if(listData.size() == 0){
						 Log.d("TAG", "Insert Saham Data to DB");
						 	
//						 	doc = Jsoup.parse(response);
//						 	Elements els1 = doc.select("a[href][target]");
//					    	Element element = null;
//							for (int i = 1; i < els1.size(); i++) {
//								element = els1.get(i);
//								listData.add(element.ownText());
//							}
							
		        		 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_saham)));
		        		 for (String listSaham : listData) {
			        		 JenisVO jenis = new JenisVO();
				        	 jenis.setSaham(listSaham.toString());
				        	 db.addSaham(jenis);
			        	 }
					}else{
						Log.d("TAG", "Load Saham From DB");
						listData = db.getAllListSaham();
					}
				}
		
					
			}catch (Exception e) {
				Log.d(TAG, "Error: " + e);
			}

		} else {
//			getNewsFailed();
		}
		
		 // Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listData);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinnerType.setAdapter(dataAdapter);
        
		dialog.dismiss();
	}
	
	
	/* check internet */
	public void checkInternet() {
//		// Check Internet Connection
//		ImageView imageView = (ImageView) findViewById(R.id.InternetlogoId);
//		if(MainMenu.checkSpeedTest() == true){ //if (hasConnection(this)) {
//			imageView.setImageResource(R.drawable.success);
//		}else {
//			imageView.setImageResource(R.drawable.fail);
//		}
		
	}
	
	public static boolean hasConnection(Context context) { 
//	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
//	    	Log.d(TAG, "Network Wifi"); 
//	    	Toast.makeText(context, "Network WIfi", Toast.LENGTH_SHORT).show();
//	       return true;
//	    }
//
//	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
//	    	Toast.makeText(context, "Mobile Network", Toast.LENGTH_SHORT).show();
//	    	Log.d(TAG, "Mobile Network");
//	      return true;
//	    }
//
//	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//	    if (activeNetwork != null && activeNetwork.isConnected()) {
//	    	Log.d(TAG, "Active Network");
//	    	Toast.makeText(context, "Active Network", Toast.LENGTH_SHORT).show();
//	      return true;
//	    }
	
		try {
			URL url = new URL("http://www.infovesta.com/isd/free/data-indeks.jsp");
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setConnectTimeout(1000 * 5); // Timeout is in seconds
			urlc.connect();
			if (urlc.getResponseCode() == 200) {
			    // http response is OK
			    Log.d(TAG, "Connection to " + url.toString() + " successful!");
			    return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace(); 
			Log.d(TAG, "Error MalformedURLException:" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Error IOException: " + e.getMessage());
		} 
		
//		Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
	    return false;
	  }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	Log.d(TAG, "Press Back Button");
	        this.finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
