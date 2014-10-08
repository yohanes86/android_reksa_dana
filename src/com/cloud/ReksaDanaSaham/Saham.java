package com.cloud.ReksaDanaSaham;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cloud.ReksaDanaSaham.utils.GeneralFunctionUtils;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Saham extends Activity {
	private static final String TAG = "SahamActivity";
	
	private ProgressDialog dialog;
	
	ListView listView;
	
	private List<String> listCodeName = new ArrayList<String>();
	private List<String> listOpen = new ArrayList<String>();
	private List<String> listHigh = new ArrayList<String>();
	private List<String> listLow = new ArrayList<String>();
	private List<String> listClose = new ArrayList<String>();
	
	Document doc = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.header_saham);
		GeneralFunctionUtils.setDisplayHomeAsUpEnabled(this);
		
		Intent i = getIntent();
        // Receiving the Data
        String jenis = i.getStringExtra("Jenis");
        String tipe = i.getStringExtra("Type");
        Log.d(TAG,"Second Screen : " + jenis + " - " + tipe);
        
        String url = "";
        if(getString(R.string.lq45).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_lq45);//"http://www.infovesta.com/isd/m/mindexs.jsp?tipe=LQ45";
        }
        else if(getString(R.string.bisnis27).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_bisnis27);//"http://www.infovesta.com/isd/m/mindexs.jsp?tipe=BISNIS27";
        }
        else if(getString(R.string.kompas100).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_kompas100);//"http://www.infovesta.com/isd/m/mindexs.jsp?tipe=KOMPAS100";
        }
        else if(getString(R.string.all).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_all_saham);//"http://www.infovesta.com/isd/m/mindexs.jsp?tipe=ALL";
        }
        else{
        	Log.d(TAG, "Unable to load Url");
        }
//		String url = "http://www.infovesta.com/isd/m/mindexs.jsp";
		new HandleHttpTask().execute(url);	
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
	
	private class HandleHttpTask extends AsyncTask<String, Void, Void> {
		private final HttpClient client = new DefaultHttpClient();
		private String content;

		protected void onPreExecute() {
			// Toast toast = Toast.makeText(getApplicationContext(),
			// getResources().getString(R.string.lbl_loading),
			// Toast.LENGTH_SHORT);
			// toast.show();
			dialog = new ProgressDialog(Saham.this);
			dialog.setIndeterminate(true);
			// dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
			dialog.setTitle("Loading");
			dialog.setCancelable(true);
			dialog.setMessage(getResources().getString(R.string.label_loading));
			dialog.show();
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
			receiveResponse(content);
		}
	}
	
	
	private void receiveResponse(String response) {
		Log.v(TAG, "receiveResponse: " + response);
		if (response != null) { 
			
		try {
//			Document doc = Jsoup.connect(url).get();
			doc = Jsoup.parse(response);
			
			/* tanggal */
			TextView tvTanggal = (TextView) findViewById(R.id.tanggal); 
			String tgl = doc.select("table b").get(0).text();
			if(tgl!=null || !tgl.equals("")){
				Date t = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
				tvTanggal.setText("Saham Tanggal : " + new SimpleDateFormat("dd-MMM-yyyy").format(t));
//				System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(t));
			}else{
				tvTanggal.setText("Saham Tanggal : ");
			}
			

			/* data */
			int column = 0;
			Elements prs = doc.select("table table td");
			for (int i = 0; i < prs.size(); i++) {
				Element element = prs.get(i);
				if(element.ownText().equals("") || element.ownText() == null || element.ownText().equals("DATA SAHAM")){
					continue;
				}
				DecimalFormat nf1 = new DecimalFormat("#,##0");
				DecimalFormat nf2 = new DecimalFormat("#,###"); //#,##0.00
				if(column == 5){
					column = 0;
				}
			
				if(column==0){
					listCodeName.add(element.ownText());
				}else if(column==1){
					listOpen.add(nf1.format(Double.parseDouble(element.ownText())));
				}else if(column==2){
					listHigh.add(nf2.format(Double.parseDouble(element.ownText())));
				}else if(column==3){
					listLow.add(nf2.format(Double.parseDouble(element.ownText())));
				}else if(column==4){
					listClose.add(nf2.format(Double.parseDouble(element.ownText())));
				}
				column++;
			}	
			
//			System.out.println(listCodeName.toString());
//			System.out.println(listOpen.toString());
//			System.out.println(listHigh.toString());
//			System.out.println(listLow.toString());
//			System.out.println(listClose.toString());
			
			
		}catch (Exception e) {
			Log.d(TAG, "Error: " + e);
		}

		
		} else {
//			getNewsFailed();
		}
		listView = (ListView) findViewById(R.id.lv_country); 
	    listView.setAdapter(new EfficientAdapter(this, listCodeName, listOpen, listHigh, listLow, listClose));
		dialog.dismiss();
	}
	
	private static class EfficientAdapter extends BaseAdapter {
	 	private LayoutInflater mInflater;
	 	private List<String> stockCode;
	 	private List<String> open; 
	 	private List<String> high; 
	 	private List<String> low; 
	 	private List<String> close; 
	 	
        public EfficientAdapter(Context context, List<String> listCodeName, List<String> listOpen, List<String> listHigh, List<String> listLow,
        		List<String> listClose) {
            mInflater = LayoutInflater.from(context);
            stockCode = listCodeName;
            open = listOpen;
            high = listHigh;
            low = listLow;
            close = listClose;
           
        }
 
        public int getCount() {
        	return stockCode.size();
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.detail_saham, null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView
                        .findViewById(R.id.StockCode);
                holder.text2 = (TextView) convertView
                        .findViewById(R.id.Open);
                holder.text3 = (TextView) convertView
                        .findViewById(R.id.High);
                holder.text4 = (TextView) convertView
                        .findViewById(R.id.Low);
                holder.text5 = (TextView) convertView
                        .findViewById(R.id.Close);
              
               
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
                        
              /* Reksa Dana Name */
              holder.text1.setText(stockCode.get(position));
              holder.text1.setTextColor(Color.BLACK);
              
              /* nab */
              holder.text2.setText(open.get(position));
              holder.text2.setTextColor(Color.BLACK);
              
              /* hr */
              holder.text3.setText(high.get(position));
              holder.text3.setTextColor(Color.BLACK);
              
              /* mggu */
              holder.text4.setText(low.get(position));
              holder.text4.setTextColor(Color.BLACK);
              /* bln */
              holder.text5.setText(close.get(position));
              holder.text5.setTextColor(Color.BLACK);
              
            
            return convertView;
        }
 
        static class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
            TextView text5;
          
        }
 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_saham, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.menu_logout:
//			new AlertDialog.Builder(this)
//					.setIcon(android.R.drawable.ic_dialog_alert)
//					.setTitle(
//							getResources().getString(R.string.lbl_menu_logout))
//					.setMessage(
//							getResources().getString(
//									R.string.lbl_menu_logout_confirm))
//					.setPositiveButton(getString(R.string.label_yes),
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									setMenuExit();
//
//								}
//
//							})
//					.setNegativeButton(getString(R.string.label_no), null)
//					.show();
//			return true;
		case android.R.id.home:
			Intent intent = new Intent(this, MainMenu.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
