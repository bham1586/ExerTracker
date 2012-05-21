package com.bhjy.ExerTracker;

import java.util.List;

import com.bhjy.ExerTracker.R.id;
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
        
        exercisesDB = new ExercisesDataSource(this);
        exercisesDB.open();
        createButtonListeners();
        
		allExercises = exercisesDB.getAllExercises();
		        
    }
    
    private void createButtonListeners(){

		int[] buttonList = {R.id.trackPullupsBtn, R.id.trackPushupsBtn, R.id.trackSitupsBtn, R.id.trackSquatsBtn, R.id.progressIcon, R.id.recordsIcon};

		ImageButton button;
		
		for(final int i:buttonList)
		{
			button = (ImageButton) findViewById(i);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
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
					else if(i == R.id.progressIcon) {
						actionName = "com.bhjy.ExerTracker.ShowProgressActivity";
					}
					else if(i == R.id.recordsIcon) {
						actionName = "com.bhjy.ExerTracker.ShowRecordsActivity";
					}
					
					Intent intent = new Intent(actionName);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});			
		}		
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