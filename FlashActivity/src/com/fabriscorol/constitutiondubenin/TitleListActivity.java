package com.fabriscorol.constitutiondubenin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class TitleListActivity extends SherlockFragmentActivity implements OnItemClickListener /*OnBackStackChangedListener*/{
	
	
	private ViewPager pager = null;
	
	private View sidebar = null;
	private View divider = null;
	
	private SimpleContentFragment help = null;
	private SimpleContentFragment about = null;
	
	private static final String HELP = "help";
	private static final String ABOUT = "about";
	private static final String FILE_HELP = 
			"file:///android_asset/misc/help.html";
	private static final String FILE_ABOUT =
			"file:///android_asset/misc/about.html";
	
	JSONArray chapters;
	
	private ListView lv;
	
	private SimpleAdapter subadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*if (getSupportFragmentManager().findFragmentByTag(MODEL) == null) {
			model = new ModelFragment();
			getSupportFragmentManager().beginTransaction()
				.add(model, MODEL).commit();
		} else {
			model = 
			  (ModelFragment)getSupportFragmentManager().findFragmentByTag(MODEL);
		}*/
		
		setContentView(R.layout.title_view);
		
		try {
			JSONObject jsonobj = new JSONObject (loadJSONFromAsset());
			chapters = jsonobj.optJSONArray("chapters");
			
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(chapters.length());
			
			for(int i = 0 ; i < chapters.length(); i++){
				
				JSONObject chapter = chapters.optJSONObject(i);
				HashMap<String, String> item = new HashMap<String, String>();
                item.put("title", chapter.getString("title"));
                item.put("libele", chapter.getString("libelle"));

                list.add(item);
			}
			
			subadapter = new SimpleAdapter(this, list, R.layout.title_item_view, new String[] {"title","libele"}, new int[]{R.id.textView1,R.id.textView2});

			
			lv = (ListView) findViewById(R.id.listView1);
			lv.setAdapter(subadapter);
			
			lv.setOnItemClickListener(this);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		HashMap<String, String> selectedTitle = (HashMap<String, String>) subadapter.getItem(position);
		//Cursor selectedTitle = (Cursor)lv.getItemAtPosition(position);
		Intent tilteId =new Intent(this, MainActivity.class);
		tilteId.putExtra("titleIdPosition", selectedTitle);
		startActivity(tilteId);
	}
	
	public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("book/contents.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
	
	void openSidebar() {
		LinearLayout.LayoutParams p = 
				(LinearLayout.LayoutParams)sidebar.getLayoutParams();
		
		if (p.weight == 0) {
			p.weight = 3;
			sidebar.setLayoutParams(p);
		}
		divider.setVisibility(View.VISIBLE);
	}
	
	void showAbout() {
		if (sidebar != null) {
			openSidebar();
			
			if (about == null) {
				about = SimpleContentFragment.newInstance(FILE_ABOUT);
			}
			
			getSupportFragmentManager().beginTransaction()
										.addToBackStack(null)
										.replace(R.id.sidebar, about).commit();
		} else {
			Intent i = new Intent(this, SimpleContentActivity.class);
			i.putExtra(SimpleContentActivity.EXTRA_FILE, FILE_ABOUT);
			startActivity(i);
		}
	}
	
	void showHelp() {
		if (sidebar != null) {
			openSidebar();
			
			if (about == null) {
				about = SimpleContentFragment.newInstance(FILE_HELP);
			}
			
			getSupportFragmentManager().beginTransaction()
			.addToBackStack(null)
			.replace(R.id.sidebar, help).commit();
		} else {
			Intent i = new Intent(this, SimpleContentActivity.class);
			i.putExtra(SimpleContentActivity.EXTRA_FILE, FILE_HELP);
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_list, menu);
		return(super.onCreateOptionsMenu(menu));
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			pager.setCurrentItem(0, false);
			return(true);
		case R.id.about:
			showAbout();
			return(true);
		case R.id.help:
			showHelp();
			return(true);
		case R.id.update:			
			startService(new Intent(this, DownloadCheckService.class));
			return(true);
		case R.id.settings:
			startActivity(new Intent(this, Preferences.class));
			return(true);
		}
		return(super.onOptionsItemSelected(item));
	}


}
