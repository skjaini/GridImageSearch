package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchOptionsActivity extends Activity {
	public static final String PREFS_NAME = "SearchOptions";
    private static final String IMG_SIZE_PREFERENCE_KEY = "isk";
    private static final String IMG_COLOR_PREFERENCE_KEY = "ick";
    private static final String IMG_TYPE_PREFERENCE_KEY = "itk";
    private static final String SITE_SEARCH_PREFERENCE_KEY = "ssk";

    SharedPreferences settings;
	Spinner prefImgSize;
	Spinner prefImgColor;
	Spinner prefImgType;
	EditText prefSiteSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_options);
		
		// Restore preferences
	    settings = getSharedPreferences(PREFS_NAME, 0);
	    
		prefImgSize = (Spinner) findViewById(R.id.prefImgSize);
		setSpinner(prefImgSize, R.array.img_size_array, IMG_SIZE_PREFERENCE_KEY);
		
		prefImgColor = (Spinner) findViewById(R.id.prefImgColor);
		setSpinner(prefImgColor, R.array.img_color_array, IMG_COLOR_PREFERENCE_KEY);
		
		prefImgType = (Spinner) findViewById(R.id.prefImgType);
		setSpinner(prefImgType, R.array.img_type_array, IMG_TYPE_PREFERENCE_KEY);
		
		prefSiteSearch = (EditText) findViewById(R.id.prefSiteSearch);
		prefSiteSearch.setText(settings.getString(SITE_SEARCH_PREFERENCE_KEY, ""));	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_options, menu);
		return true;
	}

	public void onPreferencesSave(View v) {
		String imgSize = prefImgSize.getSelectedItem().toString();
		String imgColor = prefImgColor.getSelectedItem().toString();
		String imgType = prefImgType.getSelectedItem().toString();
		String siteSearch = prefSiteSearch.getText().toString();
		
		// Save user preferences
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(IMG_SIZE_PREFERENCE_KEY, imgSize);
		editor.putString(IMG_COLOR_PREFERENCE_KEY, imgColor);
		editor.putString(IMG_TYPE_PREFERENCE_KEY, imgType);
		editor.putString(SITE_SEARCH_PREFERENCE_KEY, siteSearch);
		editor.commit();
		
		// Return to SearchActivity
		Intent data = new Intent();
		data.putExtra("imgSize", imgSize);
		data.putExtra("imgColor", imgColor);
		data.putExtra("imgType", imgType);
		data.putExtra("siteSearch", siteSearch);
		
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}
	
	public void setSpinner(Spinner spinner, int resourceId, String key) {
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				resourceId, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		// Set the selected value in drop-down
		String selectedValue = settings.getString(key, "");
		int selectedItemPosition = adapter.getPosition(selectedValue);
		spinner.setSelection(selectedItemPosition);
	}
	
}
