package com.bhjy.ExerTracker;

import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Models.Exercise;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ExerTrackerActivity extends Activity {
    
    private ExercisesDataSource exercisesDB;
    private List<Exercise> allExercises;
    private ListView exerciseList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        */
        exercisesDB = new ExercisesDataSource(this);
        exercisesDB.open();
        createButtonListeners();
        
		allExercises = exercisesDB.getAllExercises();
		/*
		
        exerciseList = (ListView) this.findViewById(R.id.listView1);
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
        */
        
    }
    
    private void createButtonListeners(){

		int[] buttonList = {R.id.trackPullupsBtn, R.id.trackPushupsBtn, R.id.trackSitupsBtn, R.id.trackSquatsBtn};

		ImageButton button;
		
		for(final int i:buttonList)
		{
			button = (ImageButton) findViewById(i);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
					Intent intent = new Intent(actionName);
					Bundle bundle = new Bundle();
					if(i == R.id.trackPullupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(0).getId());
					}
					else if(i == R.id.trackPushupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(1).getId());
					}
					else if(i == R.id.trackSitupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(2).getId());
					}
					else if(i == R.id.trackSquatsBtn) {
						bundle.putLong("exercise_id", allExercises.get(3).getId());
					}
					
					intent.putExtras(bundle);
					startActivity(intent);
					//setContentView(R.layout.loading);
				}
			});
			
		}
		
		
		//create the progress Icon
		button = (ImageButton) findViewById(R.id.progressIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowProgressActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
			}
		});
		
		/* DON'T MAKE THIS BUTTON CLICKABLE WHEN ON THIS SCREEN
		//create the tracker icon that goes to the main screen
		button = (ImageButton) findViewById(R.id.trackerIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ExerTrackerActivity.class);
				startActivity(intent);
			}
		});
		*/
		
		/*
		//create the records button
		button = (ImageButton) findViewById(R.id.recordsIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowRecordsActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
			}
		});
		*/

	};
	
    @Override
	protected void onResume() {
		exercisesDB.open();
		//allExercises = exercisesDB.getAllExercises();
		//exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
		super.onResume();
	}

	@Override
	protected void onPause() {
		exercisesDB.close();
		super.onPause();
	}
}