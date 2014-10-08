package com.cloud.ReksaDanaSaham;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.cloud.ReksaDanaSaham.data.KeyValueVO;
import com.cloud.ReksaDanaSaham.utils.GeneralFunctionUtils;
import com.google.analytics.tracking.android.EasyTracker;

public class ReksaDana2 extends Activity {
	private static final String TAG = "ReksaDanaSahamActivity";
	
	/** Called when the activity is first created. */
    ListView listView;

    List<String> title = new ArrayList<String>();
    List<String> reksaDanaSaham = new ArrayList<String>();
	KeyValueVO keyValue = null;
	List<KeyValueVO> listNab = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listHr = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listMggu = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listBln = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listThn1 = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listThn3 = new ArrayList<KeyValueVO>();
	List<KeyValueVO> listThn5 = new ArrayList<KeyValueVO>();
	
	Document doc = null;
	 
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.header_reksa_dana);
		GeneralFunctionUtils.setDisplayHomeAsUpEnabled(this);
		
		Intent i = getIntent();
        // Receiving the Data
        String jenis = i.getStringExtra("Jenis");
        String tipe = i.getStringExtra("Type");
        Log.d(TAG,"Second Screen : " + jenis + " - " + tipe);
        
        String url = "";
        if(getString(R.string.pend_tetap).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_pend_tetap);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=pt";
        }
        else if(getString(R.string.campuran).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_campuran);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=cp";
        }
        else if(getString(R.string.saham).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_saham);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=sh";
        }
        else if(getString(R.string.pasar_uang).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_pasar_uang);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=pu";
        }
        else if(getString(R.string.terproteksi).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_terproteksi);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=pp";
        }
        else if(getString(R.string.indeks_etf).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_indeks_etf);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=id";
        }
        else if(getString(R.string.dollar).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_dollar);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=dl";
        }
        else if(getString(R.string.syariah).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_syariah);//"http://www.infovesta.com/isd/m/mindex.jsp?tipe=sy";
        }
        else if(getString(R.string.p_terbatas).equalsIgnoreCase(tipe)){
        	url = getString(R.string.link_p_terbatas);
        }
        else{
        	Log.d(TAG, "Unable to load Url");
        }
//		String url = "http://www.infovesta.com/isd/m/mindex.jsp?tipe=pt";
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
			dialog = new ProgressDialog(ReksaDana2.this);
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
			
			TextView tvReksaName = (TextView) findViewById(R.id.tv_1);
			tvReksaName.setText("Reksa Dana");
			
			/* title */
			Elements els1 = doc.select("table td b");
			Element element1;
			for (int x=0; x < els1.size(); x++) {
				element1 = els1.get(x);
				reksaDanaSaham.add(element1.ownText());
			}
			
			/* tanggal */
			TextView tvTanggal = (TextView) findViewById(R.id.tanggal); 
//			String tgl = doc.select("table b").get(0).text();
//			if(tgl!=null || !tgl.equals("")){
//				Date t = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
//				tvTanggal.setText("Reksa Dana Tanggal : " + new SimpleDateFormat("dd-MMM-yyyy").format(t));
////				System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(t));
//			}else{
				tvTanggal.setText("Reksa Dana Tanggal : ");
//			}
			

			/* data */
			int column = 0;
			Elements prs = doc.select("div table td[style]");
			for (int i = 0; i < prs.size(); i++) {
				Element element = prs.get(i);
				if(TextUtils.isEmpty(element.ownText())){
					continue;
				}
				DecimalFormat nf = new DecimalFormat("#,##0.00");
				DecimalFormat nf1 = new DecimalFormat("#,###.##"); //NAB
				if(column == 5){
					column = 0;
				}
				
				
				if(column==0){ 
					String data []  = element.attributes().get("style").split(";");
					if(!TextUtils.isEmpty(element.ownText())){
						if(data.length == 2){
							keyValue = new KeyValueVO();
							keyValue.setValue(nf1.format(Double.parseDouble(element.ownText())));
							listNab.add(keyValue);
						}
					}

				}else if(column==1){
					String data []  = element.attributes().get("style").split(";");
					if(data.length > 2){
						String color [] = data[2].split(":");
						int x = defineColor(color[1]);
						keyValue = new KeyValueVO();
						keyValue.setKey(x);
						if("0.0".equals(element.ownText())){
							keyValue.setValue("-");
						}else{
							keyValue.setValue(nf.format(Double.parseDouble(element.ownText())));
						}
						listHr.add(keyValue);
					}
				}
				else if(column==2){
					String data []  = element.attributes().get("style").split(";");
					if(data.length > 2){
						String color [] = data[2].split(":");
						int x = defineColor(color[1]);
						keyValue = new KeyValueVO();
						keyValue.setKey(x);
						if("0.0".equals(element.ownText())){
							keyValue.setValue("-");
						}else{
							keyValue.setValue(nf.format(Double.parseDouble(element.ownText())));
						}
						listBln.add(keyValue);
					}
				}
				else if(column==3){
					String data []  = element.attributes().get("style").split(";");
					if(data.length > 2){
						String color [] = data[2].split(":");
						int x = defineColor(color[1]);
						keyValue = new KeyValueVO();
						keyValue.setKey(x);
						if("0.0".equals(element.ownText())){
							keyValue.setValue("-");
						}else{
							keyValue.setValue(nf.format(Double.parseDouble(element.ownText())));
						}
						listThn1.add(keyValue);
					}
				}else if(column==4){
					String data []  = element.attributes().get("style").split(";");
					if(data.length > 2){
						String color [] = data[2].split(":");
						int x = defineColor(color[1]);
						keyValue = new KeyValueVO();
						keyValue.setKey(x);
						if("0.0".equals(element.ownText())){
							keyValue.setValue("-");
						}else{
							keyValue.setValue(nf.format(Double.parseDouble(element.ownText())));
						}
						listThn3.add(keyValue);
					}
				}
				column++;
			}	
			
		}catch (Exception e) {
			Log.d(TAG, "Error: " + e);
		}

		} else {
//			getNewsFailed();
		}
		listView = (ListView) findViewById(R.id.lv_country); 
	    listView.setAdapter(new EfficientAdapter(this, reksaDanaSaham, listNab, listHr, listBln, listThn1, listThn3));
		dialog.dismiss();
	}
	
	 public int defineColor(String inp){
		 	Resources res = getResources();
			if(("#000;").equals(inp.trim())){
				return res.getColor(R.color.Black);
			}else if(("green").equals(inp.trim())){
				return res.getColor(R.color.DarkGreen);
			}else if(("red").equals(inp.trim())){
				return res.getColor(R.color.Red);
			}else{
				return 0;
			}
		}
	
	 private static class EfficientAdapter extends BaseAdapter {
		 	private LayoutInflater mInflater;
		 	private List<String> reksaName;
		 	private List<KeyValueVO> nab; 
		 	private List<KeyValueVO> hr; 
		 	private List<KeyValueVO> bln; 
		 	private List<KeyValueVO> thn1; 
		 	private List<KeyValueVO> thn3;  
		 	
		 	
	        public EfficientAdapter(Context context, List<String> listReksaName, List<KeyValueVO> listNab, List<KeyValueVO> listHr,
	        		List<KeyValueVO> listBln, List<KeyValueVO> listThn1, List<KeyValueVO> listThn3) {
	            mInflater = LayoutInflater.from(context);
	            reksaName = listReksaName;
	            nab = listNab;
	            hr = listHr;
	            bln = listBln; 
	            thn1 = listThn1;
	            thn3 = listThn3;
	        }
	 
	        public int getCount() {
	        	return reksaName.size();
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
	                convertView = mInflater.inflate(R.layout.detail_reksa_dana, null);
	                holder = new ViewHolder();
	                holder.text1 = (TextView) convertView
	                        .findViewById(R.id.ReksaName);
	                holder.text2 = (TextView) convertView
	                        .findViewById(R.id.Nab);
	                holder.text3 = (TextView) convertView
	                        .findViewById(R.id.Hr);
	                holder.text4 = (TextView) convertView
	                        .findViewById(R.id.Bln);
	                holder.text5 = (TextView) convertView
	                        .findViewById(R.id.Thn1);
	                holder.text6 = (TextView) convertView
	                        .findViewById(R.id.Thn3);
	               
	                convertView.setTag(holder);
	            } else {
	                holder = (ViewHolder) convertView.getTag();
	            }
	                        
	              /* Reksa Dana Name */
	              holder.text1.setText(reksaName.get(position));
	              holder.text1.setTextColor(Color.BLACK);
	              
	              /* nab */
	              holder.text2.setText(nab.get(position).getValue());
	              holder.text2.setTextColor(Color.BLACK);
//	              holder.text2.setTextColor(nab.get(position).getKey());
	              
	              /* hr */
	              holder.text3.setText(hr.get(position).getValue());
	              holder.text3.setTextColor(hr.get(position).getKey());
	              
	              /* bln */
	              holder.text4.setText(bln.get(position).getValue());
	              holder.text4.setTextColor(bln.get(position).getKey());
	              /* thn1 */
	              holder.text5.setText(thn1.get(position).getValue());
	              holder.text5.setTextColor(thn1.get(position).getKey());
	              /* thn3 */
	              holder.text6.setText(thn3.get(position).getValue());
	              holder.text6.setTextColor(thn3.get(position).getKey());
	            
	            return convertView;
	        }
	 
	        static class ViewHolder {
	            TextView text1;
	            TextView text2;
	            TextView text3;
	            TextView text4;
	            TextView text5;
	            TextView text6;
	        }
	 }
		 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reksa_dana, menu);
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
	    	Log.d(TAG, "Press Back Button");
	        this.finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
