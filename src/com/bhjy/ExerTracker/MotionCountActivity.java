package com.bhjy.ExerTracker;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MotionCountActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisecount);

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
		// add listener. The listener will be HelloAndroid (this) class
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
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
				case Sensor.TYPE_MAGNETIC_FIELD:
					// assign directions
					float xm=event.values[0];
					float ym=event.values[1];
					float zm=event.values[2];
	
					xMag.setText("X: "+xm);
					yMag.setText("Y: "+ym);
					zMag.setText("Z: "+zm);
					break;
			}
		}
		sensorLock = 0;
	}
}
