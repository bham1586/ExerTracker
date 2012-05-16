package com.bhjy.ExerTracker;

import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Models.Exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.ImageButton;
=======
>>>>>>> e2f596842ed981324ed2b8d7ab6da5957dacdd11
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ExerTrackerActivity extends Activity {
<<<<<<< HEAD
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		final ImageButton button = (ImageButton) findViewById(R.id.trackPullupsBtn);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
				//setContentView(R.layout.loading);
			}
		});
		

		/*
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
*/
=======
    
    private ExercisesDataSource exercisesDB;
    
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
        
        exercisesDB = new ExercisesDataSource(this);
        exercisesDB.open();

		List<Exercise> allExercises = exercisesDB.getAllExercises();
		
        ListView exerciseList = (ListView) this.findViewById(R.id.listView1);
        exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
        //exerciseList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EXERCISES));
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
        
        Button addExerciseButton = (Button) this.findViewById(R.id.newExerciseButton);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowAddExerciseActivity";
	        	Intent intent = new Intent(actionName);
	        	startActivity(intent);				
			}
		});
        
        
    }
    
    @Override
	protected void onResume() {
		exercisesDB.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		exercisesDB.close();
		super.onPause();
>>>>>>> e2f596842ed981324ed2b8d7ab6da5957dacdd11
	}
}