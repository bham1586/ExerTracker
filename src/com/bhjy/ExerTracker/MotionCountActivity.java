package com.bhjy.ExerTracker;

import java.util.Date;
import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Advertisement;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MotionCountActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
    private SetsDataSource setsDB;
    private List<Set> setList;
    private ListView setListView;
	final String TAG = "Exercise Tracker";

    private Advertisement advert = new Advertisement();
    
	TextView accel; // declare Z axis object

	float ax = 0;
	float ay = 0;
	float az = 0;
	
	float totalAccel = 0;
	float lastAccel = 0;
	float threshhold = 1;
	double gravity = 9.8;
	int aboveGravity = 0;
	int direction = 0;
	int sensitivity = 3;
	
	int sensorLock = 0;
	
	int repCount = 0;
	
	long exercise_id;
	Date startTime;
	long duration = 0;
	long weight = 0;
	
	SharedPreferences mPrefs;
	final String showMotionHelpScreenPref = "showMotionHelpScreen";
	final String motionSensitivityPreference = "motionSensitivityPreference";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//set the layout
		setContentView(R.layout.exercisecount);
		//get the exercise ID
		Bundle bundle = this.getIntent().getExtras();
		exercise_id = bundle.getLong("exercise_id");
		
		//get the text view that will display the rep count
		accel=(TextView)findViewById(R.id.accel); // create acceleration object
		
		//set up the sensor manager
		sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		
		//set up the buttons
		createButtonListeners();
		
		//connect to the database
		setsDB = new SetsDataSource(this);
        setsDB.open();
        
        //set up the list of sets from today
		setListView = (ListView) this.findViewById(R.id.listView1);
		displaySetsFromToday();
		
		//find out if the motion help screen should be shown
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean showMotionHelpScreen = mPrefs.getBoolean(showMotionHelpScreenPref, true);
		if(showMotionHelpScreen) {
			//display the motion help screen
			new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle(R.string.motionHelpTitle).setMessage(R.string.motionHelpMessage)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
			//set the screen to not show again
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putBoolean(showMotionHelpScreenPref, false);
			editor.commit();
		}
		
		//get the sensitivity setting
		sensitivity = Integer.valueOf(mPrefs.getString(motionSensitivityPreference, "3"));
		threshhold = (float) (1.6 - (float) sensitivity / 5);
        //Log.d("ExerTracker", String.valueOf(sensitivity) + " " + String.valueOf(threshhold));
        advert.setUpAds(this);
	}
	
	private void createButtonListeners(){
		final ImageButton startButton = (ImageButton) findViewById(R.id.startBtn);
		final ImageButton doneButton = (ImageButton) findViewById(R.id.doneBtn);
		doneButton.setVisibility(View.INVISIBLE);
		
		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startButton.setVisibility(View.INVISIBLE);
				doneButton.setVisibility(View.VISIBLE);
				startTracking();
			}
		});
		
		doneButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startButton.setVisibility(View.VISIBLE);
				doneButton.setVisibility(View.INVISIBLE);
				stopTracking();
			}
		});
		
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
		
		final Button settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowPreferencesActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
			}
		});

	};

	public void startTracking() {
		//set the startTime and set the reps to 0
		startTime = new Date();
		repCount = 0;
		
		//register the accelerometer listener. This is how the motion detection will start
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		
		//display the rep count (0)
		accel.setText("Rep Count = "+repCount);
	}
	
	public void stopTracking() {
		// unregister the listener
		sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		
		//save the set to the database
		if(repCount > 0) {
	        setsDB.createSet(exercise_id, repCount, startTime, duration, weight, "");
			displaySetsFromToday();
		}
        
		//remove the rep count display and tell the user to press start when ready
		accel.setText(R.string.prompt);
	}
	
	public void onAccuracyChanged(Sensor sensor,int accuracy){
		
	}

	public void onSensorChanged(SensorEvent event){
		//need to create a lock so this function doesn't run multiple instances simultaneously
		if(sensorLock == 0) {
			sensorLock = 1;
			// check sensor type
			switch (event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					//the accelerometer has changed
					//save the previous acceleration
					lastAccel = totalAccel;
					
					//get the acceleration in each direction, then compute the total acceleration
					ax=event.values[0];
					ay=event.values[1];
					az=event.values[2];
					totalAccel = (float)Math.sqrt(ax*ax + ay*ay + az*az);
	
					//check if the acceleration is above the threshold
					if(Math.abs(totalAccel - gravity) > threshhold) {
						if(totalAccel > gravity && aboveGravity == 0) {
							//the acceleration is much greater than gravity, so the device is moving in the 
							//opposite direction of gravity (going up)
							aboveGravity = 1;
						}
						else if(totalAccel < gravity && aboveGravity == 1) {
							//the acceleration is much less than gravity, so the device is moving in the 
							//same direction as gravity (going down)
							aboveGravity = 0;
							repCount++;
							//show the rep count on the screen
							accel.setText("Rep Count = "+repCount);
						}
					}
					break;
			}
		}
		sensorLock = 0;
	}
	
	 @Override
		protected void onResume() {
		 	//open the database
			setsDB.open();
			//update the list of today's sets
			displaySetsFromToday();
			super.onResume();
			//get the sensitivity setting
			sensitivity = Integer.valueOf(mPrefs.getString(motionSensitivityPreference, "3"));
			threshhold = (float) (1.6 - (float) sensitivity / 5);
		}

		@Override
		protected void onPause() {
			// unregister the listener
			sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			//close the database
			setsDB.close();
			super.onPause();
		}
		
		protected void displaySetsFromToday() {
			//this function gets all sets of this exercise today, then displays it in the listView
			setList = setsDB.getAllSetsToday(exercise_id);
			setListView.setAdapter(new ArrayAdapter<Set>(this, android.R.layout.simple_list_item_1, setList));
		}
		

		@Override
		public void onDestroy() {
			advert.destroy();
			super.onDestroy();
		}
}
