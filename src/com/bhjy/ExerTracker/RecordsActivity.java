package com.bhjy.ExerTracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Exercise;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class RecordsActivity extends Activity {
    
    private ExercisesDataSource exercisesDB;
    private SetsDataSource setsDB;
    private List<Exercise> allExercises;
    private List<Set> allSets; 
    SimpleDateFormat displayableDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout
        setContentView(R.layout.records);
        
        //connect to the database
        exercisesDB = new ExercisesDataSource(this);
        setsDB = new SetsDataSource(this);
        exercisesDB.open();
        setsDB.open();
        allExercises = exercisesDB.getAllExercises();
        
        //set up the buttons
        createButtonListeners();
        
        Set set;
        set = setsDB.getMaxRepsInDay(allExercises.get(0).getId());
        displaySingleRecord(R.id.MostPullupsInDay, set);
        set = setsDB.getMaxRepsInSet(allExercises.get(0).getId());
        displaySingleRecord(R.id.MostPullupsInSet, set);
        set = setsDB.getMaxRepsInDay(allExercises.get(1).getId());
        displaySingleRecord(R.id.MostPushupsInDay, set);
        set = setsDB.getMaxRepsInSet(allExercises.get(1).getId());
        displaySingleRecord(R.id.MostPushupsInSet, set);
        set = setsDB.getMaxRepsInDay(allExercises.get(2).getId());
        displaySingleRecord(R.id.MostSitupsInDay, set);
        set = setsDB.getMaxRepsInSet(allExercises.get(2).getId());
        displaySingleRecord(R.id.MostSitupsInSet, set);
        set = setsDB.getMaxRepsInDay(allExercises.get(3).getId());
        displaySingleRecord(R.id.MostSquatsInDay, set);
        set = setsDB.getMaxRepsInSet(allExercises.get(3).getId());
        displaySingleRecord(R.id.MostSquatsInSet, set);
        
        //TextView tv;
        //tv = (TextView) this.findViewById(R.id.MostPullupsInDay);
        //tv.setText("    " + String.valueOf(set.getReps()) + " reps on " + setsDB.convertDateToString(set.getStartTime()).substring(0, 10) + "\n");
        
        /*
        for(final Exercise e:allExercises)
		{
        	allSets = setsDB.getAllSets(e.getId());
        	Date lastDate = new Date();
        	Date dateOfMostRepsInDay = new Date();
        	Date dateOfMostRepsInSet = new Date();
        	int repsInDay = 0;
        	int maxRepsInDay = 0;
        	int maxRepsInSet = 0;
        	for(final Set set:allSets) {
        		if(lastDate.getDate() != set.getStartTime().getDate()) {
        			//check if last date was a record
        			
        			//set lastdate to this date
        			
        			//reset repsInDay
        			
        		}
        		//check if reps in set is greater than record
        		
        		//add reps to current day
        		
        	}
        	//display results in the text views
            tv = (TextView) this.findViewById(R.id.MostPullupsInDay);
            tv.setText(maxReps "");
		}
		*/
    }
    
    private void displaySingleRecord(int viewId, Set set) {
    	 TextView tv;
         tv = (TextView) this.findViewById(viewId);
         Log.d("ExerTracker", String.valueOf(set.getStartTime()));
         tv.setText("    " + String.valueOf(set.getReps()) + " reps on " + convertDateForDisplay(set.getStartTime()) + "\n");
    }
    
    public String convertDateForDisplay(Date dateTime) {
		String date = displayableDateFormat.format(dateTime);
		return date;
	}
    
    private void createButtonListeners(){

		ImageButton button;
		
		
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
		
		//create the tracker icon that goes to the main screen
		button = (ImageButton) findViewById(R.id.trackerIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ExerTrackerActivity.class);
				startActivity(intent);
			}
		});
		
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
		//exercisesDB.open();
		//allExercises = exercisesDB.getAllExercises();
		//exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
		super.onResume();
	}

	@Override
	protected void onPause() {
		//exercisesDB.close();
		super.onPause();
	}
	
}