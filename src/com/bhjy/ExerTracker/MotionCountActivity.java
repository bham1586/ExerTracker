package com.bhjy.ExerTracker;

import java.util.Date;
import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MotionCountActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
    private SetsDataSource setsDB;
    private List<Set> setList;
    private ListView setListView;
	final String TAG = "Exercise Tracker";
	
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
	
	int sensorLock = 0;
	
	int repCount = 0;
	
	long exercise_id;
	Date startTime;
	long duration = 0;
	long weight = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisecount);
		Bundle bundle = this.getIntent().getExtras();
		exercise_id = bundle.getLong("exercise_id");
		
		
		accel=(TextView)findViewById(R.id.accel); // create acceleration object
		
		
		sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		
		final ImageButton startButton = (ImageButton) findViewById(R.id.startBtn);
		final ImageButton stopButton = (ImageButton) findViewById(R.id.stopBtn);
		stopButton.setVisibility(View.INVISIBLE);
		
		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startButton.setVisibility(View.INVISIBLE);
				stopButton.setVisibility(View.VISIBLE);
				startTracking();
			}
		});
		
		stopButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startButton.setVisibility(View.VISIBLE);
				stopButton.setVisibility(View.INVISIBLE);
				stopTracking();
			}
		});
		
		
		setsDB = new SetsDataSource(this);
        setsDB.open();
        
		setListView = (ListView) this.findViewById(R.id.listView1);
		setList = setsDB.getAllSets(exercise_id);
		setListView.setAdapter(new ArrayAdapter<Set>(this, android.R.layout.simple_list_item_1, setList));
        
	}

	public void startTracking() {
		// add listener. The listener will be HelloAndroid (this) class
		startTime = new Date();
		repCount = 0;
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);

		accel.setText("Rep Count = "+repCount);
	}
	
	public void stopTracking() {
		// unregister the listener
		sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		
		//save the set to the database
		
        setsDB.createSet(exercise_id, repCount, startTime, duration, weight, "");
        setList = setsDB.getAllSets(exercise_id);
		setListView.setAdapter(new ArrayAdapter<Set>(this, android.R.layout.simple_list_item_1, setList));
        
		accel.setText(R.string.prompt);
	}
	
	public void onAccuracyChanged(Sensor sensor,int accuracy){
		
	}

	public void onSensorChanged(SensorEvent event){
		if(sensorLock == 0) {
			sensorLock = 1;
			// check sensor type
			switch (event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
	
					
					lastAccel = totalAccel;
					// assign directions
					ax=event.values[0];
					ay=event.values[1];
					az=event.values[2];
					totalAccel = (float)Math.sqrt(ax*ax + ay*ay + az*az);
	
					
					if(Math.abs(totalAccel - gravity) > threshhold) {
						if(totalAccel > gravity && aboveGravity == 0) {
							//going up
							aboveGravity = 1;
							//Log.d(TAG, "Reached Bottom: accel = "+totalAccel);
						}
						else if(totalAccel < gravity && aboveGravity == 1) {
							//going down
							aboveGravity = 0;
							repCount++;
							//Log.d(TAG, "Reached Top: accel = "+totalAccel);
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
			setsDB.open();
			setList = setsDB.getAllSets(exercise_id);
			setListView.setAdapter(new ArrayAdapter<Set>(this, android.R.layout.simple_list_item_1, setList));
			super.onResume();
		}

		@Override
		protected void onPause() {
			setsDB.close();
			super.onPause();
		}
}
