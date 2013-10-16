package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	Button btnNext;
	Button btnPrevious;
	
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	private static final int REQUEST_CODE = 10;

	// user preferences
	int startIdx = 0;
	String imgSize = "";
	String imgColor = "";
	String imgType = "";
	String siteSearch = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void onSettingsClick(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SearchOptionsActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		  imgSize = data.getExtras().getString("imgSize");
		  imgColor = data.getExtras().getString("imgColor");
		  imgType = data.getExtras().getString("imgType");
		  siteSearch = data.getExtras().getString("siteSearch");
	  }
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnPrevious = (Button) findViewById(R.id.btnPrevious);
	}

	public String getRequestUrl() {
		String query = etQuery.getText().toString();
		String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&&v=1.0&"+
				"start=" +  startIdx + "&q="  + Uri.encode(query) +
				"&imgsz=" + imgSize +
				"&imgcolor=" + imgColor +
				"&imgtype=" + imgType +
				"&aa_sitesearch=" + siteSearch;
		
		return searchUrl;
	}
	
	public void onImageSearch(View v) {
		int tagValue = Integer.parseInt(v.getTag().toString());
		
		if(tagValue != 0) {
			startIdx += tagValue;
			btnPrevious.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
		} else {
			startIdx = 0;
			btnNext.setVisibility(View.VISIBLE);
		}
		
		String requestUrl = getRequestUrl();
		loadImages(requestUrl);
	}
	
	public void loadImages(String requestUrl) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(requestUrl,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							
							Log.d("DEBUG", imageResults.toString());
						} catch(JSONException e) {
							e.printStackTrace();
						}
					}
		});
	}
}
