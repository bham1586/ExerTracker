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
	
	TextView xCoor; // declare X axis object
	TextView yCoor; // declare Y axis object
	TextView zCoor; // declare Z axis object
	TextView accel; // declare Z axis object
	TextView accuracyText; // declare Z axis object
	TextView xMag; // declare X axis object
	TextView yMag; // declare Y axis object
	TextView zMag; // declare Z axis object

	float ax = 0;
	float ay = 0;
	float az = 0;
	float lastax = 0;
	float lastay = 0;
	float lastaz = 0;
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
		/*
		xCoor=(TextView)findViewById(R.id.xcoor); // create X axis object
		yCoor=(TextView)findViewById(R.id.ycoor); // create Y axis object
		zCoor=(TextView)findViewById(R.id.zcoor); // create Z axis object
		*/
		
		accel=(TextView)findViewById(R.id.accel); // create acceleration object
		
		/*
		accuracyText=(TextView)findViewById(R.id.accuracy); // create acceleration object
		xMag=(TextView)findViewById(R.id.xmag); // create X axis object
		yMag=(TextView)findViewById(R.id.ymag); // create Y axis object
		zMag=(TextView)findViewById(R.id.zmag); // create Z axis object
		*/

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
		
		
		/*
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
		*/
		/*	More sensor speeds (taken from api docs)
			    SENSOR_DELAY_FASTEST get sensor data as fast as possible
			    SENSOR_DELAY_GAME	rate suitable for games
			 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
		 */
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
	
					/*
					lastax=ax;
					lastay=ay;
					lastaz=az;
					*/
					lastAccel = totalAccel;
					// assign directions
					ax=event.values[0];
					ay=event.values[1];
					az=event.values[2];
					totalAccel = (float)Math.sqrt(ax*ax + ay*ay + az*az);
	
					/*
					xCoor.setText("X: "+ax);
					yCoor.setText("Y: "+ay);
					zCoor.setText("Z: "+az);
					accel.setText("Accel: "+totalAccel);
					*/
					if(Math.abs(totalAccel - gravity) > threshhold) {
						if(totalAccel > gravity && aboveGravity == 0) {
							//going up
							aboveGravity = 1;
							Log.d(TAG, "Reached Bottom: accel = "+totalAccel);
						}
						else if(totalAccel < gravity && aboveGravity == 1) {
							//going down
							aboveGravity = 0;
							repCount++;
							Log.d(TAG, "Reached Top: accel = "+totalAccel);
							accel.setText("Rep Count = "+repCount);
						}
					}
					/*
					if(Math.abs(totalAccel - gravity) > threshhold) {
						if(totalAccel > lastAccel) {
							//speeding up
							if(direction == 0) {
								if(totalAccel > gravity)
								Log.d(TAG, "Speeding up: accel = "+totalAccel);
							}
							direction = 1;
						}
						else {
							//slowing down
							if(direction == 1) {
								Log.d(TAG, "Slowing down: accel = "+totalAccel);
							}
							direction = 0;
						}
					}
					*/
					break;
					/*
				case Sensor.TYPE_MAGNETIC_FIELD:
					// assign directions
					float xm=event.values[0];
					float ym=event.values[1];
					float zm=event.values[2];
	
					xMag.setText("X: "+xm);
					yMag.setText("Y: "+ym);
					zMag.setText("Z: "+zm);
					break;
					*/
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
