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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ExerTrackerActivity extends Activity {

	private ExercisesDataSource exercisesDB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		createButtonListeners();
	
	    exercisesDB = new ExercisesDataSource(this);
        exercisesDB.open();

	}



		//NOTE:  NO LONGER USING TAB MENU
		/*

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


        /*
        Button addExerciseButton = (Button) this.findViewById(R.id.newExerciseButton);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowAddExerciseActivity";
	        	Intent intent = new Intent(actionName);
	        	startActivity(intent);				
			}
		});


    }*/

		@Override
		protected void onResume() {
			exercisesDB.open();
			super.onResume();
		}

		@Override
		protected void onPause() {
			exercisesDB.close();
			super.onPause();
		}
	
	
	
		private void createButtonListeners(){

			int[] buttonList = {R.id.trackPullupsBtn, R.id.trackPushupsBtn, R.id.trackSitupsBtn, R.id.trackSquatsBtn};

			ImageButton button;

			for(int i:buttonList)
			{
				button = (ImageButton) findViewById(i);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
						Intent intent = new Intent(actionName);
						startActivity(intent);
						//setContentView(R.layout.loading);
					}
				});
			}

		};

	}