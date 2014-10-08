package com.cloud.ReksaDanaSaham;

import java.io.IOException;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class MainMenu extends Activity implements OnItemSelectedListener {
	private static final String TAG = "MainMenuActivity";
	private final HttpClient client = new DefaultHttpClient();

	private boolean nextScr = false;
	
	// Spinner element
    Spinner spinner;
         
    // Text View
    TextView text;
    
    // Spinner element
    Spinner spinnerType;
    
    Button btnNextScreen; 
     
    List<String> listData = new ArrayList<String>();
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
//		overridePendingTransition(R.anim.bounce_in, R.anim.bounce_out);
		
		 // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
       
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();   
        
        /* check internet connection */
        new HandleHttpTask().execute("https://www.google.com/");
        
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        btnNextScreen = (Button) findViewById(R.id.btnProcess);
        
        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
            	
            	/* check internet connection */
               new HandleHttpTask().execute("https://www.google.com/");
               nextScr = true;
          
            }
        });
	}
	
	 @Override
	  public void onStart() {
	    super.onStart();
//	    ... // The rest of your onStart() code.
	    EasyTracker.getInstance().setContext(this);
	    EasyTracker.getInstance().activityStart(this); // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	
	
	 /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // Spinner Drop down elements
        List<String> labelTypes = new ArrayList<String>();
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
    

	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        
		String label = "";
        switch (parent.getId()) {
		case R.id.spinner:
			// On selecting a spinner item
	         label = parent.getItemAtPosition(position).toString();
	        	        
	        // Spinner element
       	 	spinnerType = (Spinner) findViewById(R.id.spinnerType);
       	 	// Spinner click listener
       	 	spinnerType.setOnItemSelectedListener(this);
       	 	       	 	
       	 	listData = new ArrayList<String>();
	        
	        if(label.equalsIgnoreCase(getResources().getString(R.string.title_reksa_dana))){
	        	 
	             /* Reksa Dana */
	        	 text = (TextView) findViewById(R.id.text);
	        	 text.setText("Jenis Reksa Dana");
	        	 text.setTextSize(18);
	        	 
	        	 /* set prompt spinner*/
	        	 spinnerType.setPrompt("Jenis Reksa Dana");
	        	 
	        	 listData = new ArrayList<String>();
	        	 Resources res = getResources();
	        	 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_reksa_dana)));
	        }else{
	        	 /* SAHAM */
	        	 text = (TextView) findViewById(R.id.text);
	        	 text.setText("Jenis Saham"); 
	        	 text.setTextSize(18);
	        	 
	        	 /* set prompt spinner*/
	        	 spinnerType.setPrompt("Jenis Saham");
	        	 
	        	 listData = new ArrayList<String>();
	        	 Resources res = getResources();
	        	 listData = new ArrayList<String>(Arrays.asList(res.getStringArray(R.array.list_saham)));
	        }
	        
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, listData);
	 
	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	        // attaching data adapter to spinner
	        spinnerType.setAdapter(dataAdapter);
	        
       	 	spinnerType.setOnItemSelectedListener(new OnItemSelectedListener() {
          	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//          	showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
          	}
          	
          	public void onNothingSelected(AdapterView<?> parent) {
//          	showToast("Spinner2: unselected");
          	}
          	
       	 	});
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
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	      Toast.makeText(this, "Setting selected", Toast.LENGTH_SHORT)
	          .show();
	      break;
	    case R.id.menu1:
	      Toast.makeText(this, "Menu item 1 selected", Toast.LENGTH_SHORT)
	          .show();
	      break;
	    case R.id.menu2:
		      Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT)
		          .show();
		      break;

	    default:
	      break;
	    }

	    return true;
	  } 
	
	
	private class HandleHttpTask extends AsyncTask<String, Void, Void> {
		private String content;

		protected void onPreExecute() {
			
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
			/* if hast next screen and connection internet is available */
			if(nextScr && isConnected(content)){
				//Starting a new Intent
            	Intent nextScreen = null;
            	
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
                nextScr = false;
			}
			else{ 
				/* only check internet connection & change image */
				isConnected(content);
			}
		}

	}
	
	// Check Internet Connection
	private boolean isConnected(String response){
		Log.v(TAG, "receiveResponse: " + response);		
		ImageView imageView = (ImageView) findViewById(R.id.InternetlogoId);
		if (response != null) { 
			imageView.setImageResource(R.drawable.success);
			return true;
		}else {
			imageView.setImageResource(R.drawable.fail);
			showToast("No Internet Connection");
		}
		return false;
	}
	
	private void alertDialogExit(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
   	 	
        // Setting Dialog Title
		builder.setTitle("Exit");

        // Setting Dialog Message
		builder.setMessage("Do you want to exit?");

        // Setting Icon to Dialog
		builder.setIcon(R.drawable.exit_icon);

     // Setting Positive "Yes" Button
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed YES button. Write Logic Here
//            Toast.makeText(getApplicationContext(), "You clicked on YES",
//                                Toast.LENGTH_SHORT).show();
            	finish();
            }
        });
        
        
        // Setting Negative "NO" Button
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed No button. Write Logic Here
//            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            	 
            }
        });

		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
		alertDialog.show();
	}
	
//	 private void openDialog(){
//	    	final Dialog dialog = new Dialog(MainMenu.this);
//	    	dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//	    	dialog.setTitle("Exit");
//	    	dialog.setContentView(R.layout.dialog_exit);
//	    	dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.exit_icon);
////	    	dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//	    	Button btnNo = (Button)dialog.getWindow().findViewById(R.id.btnNo);
//	    	
//	    	btnNo.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}});
//	    	
//	    	Button btnYes = (Button)dialog.getWindow().findViewById(R.id.btnYes);
//	    	
//	    	btnYes.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					finish();
//				}});
//	    	
//	    	dialog.show();
//	    	
//	    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	Log.d(TAG, "Press Back Button");
	    	alertDialogExit();
//	    	openDialog();
	    	
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
