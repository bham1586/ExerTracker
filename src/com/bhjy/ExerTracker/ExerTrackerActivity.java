package com.bhjy.ExerTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ExerTrackerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //set up the tab menu
        TabHost tabs = (TabHost) this.findViewById(R.id.maintabhost);
        tabs.setup();
        TabSpec tspec1 = tabs.newTabSpec("Exercise Tab");
        tspec1.setIndicator("Exercise!");
        tspec1.setContent(R.id.tab1);
        tabs.addTab(tspec1);
        TabSpec tspec2 = tabs.newTabSpec("Second Tab");
        tspec2.setIndicator("Two");
        tspec2.setContent(R.id.tab2);
        tabs.addTab(tspec2);
        TabSpec tspec3 = tabs.newTabSpec("Third Tab");
        tspec3.setIndicator("Three");
        tspec3.setContent(R.id.tab3);
        tabs.addTab(tspec3);
        
        //Create the list of exercises
        final String[] EXERCISES = new String[] {"Pushups", "Pullups", "Situps", "Squats"};
        
        ListView exerciseList = (ListView) this.findViewById(R.id.listView1);
        exerciseList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EXERCISES));
        exerciseList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              // When clicked, show a toast with the TextView text
              //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                  //Toast.LENGTH_SHORT).show();
            	String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
	        	Intent intent = new Intent(actionName);
	        	startActivity(intent);
            }
          });
        
    }
}