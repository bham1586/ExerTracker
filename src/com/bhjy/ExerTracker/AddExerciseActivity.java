package com.bhjy.ExerTracker;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Models.Exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExerciseActivity extends Activity {

    private ExercisesDataSource exercisesDB;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.addexercise);
    	
    	//open the database
    	exercisesDB = new ExercisesDataSource(this);
		exercisesDB.open();
        
    	final EditText editTextName = (EditText) this.findViewById(R.id.editTextName);
    	final EditText editTextDescription = (EditText) this.findViewById(R.id.editTextDescription);
    	
    	Button buttonSubmit = (Button) this.findViewById(R.id.buttonSubmit);
    	buttonSubmit.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = editTextName.getText().toString();
				String description = editTextDescription.getText().toString();
				
		        //save the exercise
				Exercise newExercise = exercisesDB.createExercise(name, description);
				
				Intent intent = new Intent();
	            setResult(RESULT_OK, intent);
	            finish();
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
	}
}
