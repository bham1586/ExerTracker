package com.bhjy.ExerTracker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Exercise;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ProgressActivity extends Activity {
    
    private ExercisesDataSource exercisesDB;
    private SetsDataSource setsDB;
    private List<Exercise> allExercises;
    private ListView exerciseList;
    private List<Set> allSets; 
    private List<List<Set>> allSetsAllExercises;
    private GraphicalView mChartView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout
        setContentView(R.layout.progress);
        
        //connect to the database
        exercisesDB = new ExercisesDataSource(this);
        setsDB = new SetsDataSource(this);
        
        //set up the buttons
        createButtonListeners();
        
        //add functionality here
        //
        //
        if(mChartView == null) {
        	LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
        	
        	//mmChartView = ChartFactory.getTimeChartView(this, getMyData(), getMyRenderer(), null);
        	mChartView = ChartFactory.getTimeChartView(this, getMyData(), getMyRenderer(), null);
        	layout.addView(mChartView);
        }
        else {
        	mChartView.repaint();
        }        
        
        
    }
    
    private void createButtonListeners(){

		ImageButton button;
		
		/* DON'T MAKE THIS BUTTON CLICKABLE WHEN ON THIS SCREEN
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
		*/
		
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
	};
	
    @Override
	protected void onResume() {
		exercisesDB.open();
		setsDB.open();
		//allExercises = exercisesDB.getAllExercises();
		//exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
		super.onResume();
		
		if (mChartView != null) {
    		mChartView.repaint();
		}
	}

	@Override
	protected void onPause() {
		exercisesDB.close();
		setsDB.close();
		super.onPause();
	}
	
	
	public XYMultipleSeriesDataset getMyData() {
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset(); 
		XYSeries dataSeries;
		
		//dataSeries.add(new GregorianCalendar(2011, 5, 9, 18, 15, 1).getTimeInMillis(), 0);
		
		exercisesDB.open();
		setsDB.open();
		
		allExercises = exercisesDB.getAllExercises();
		Calendar cal = Calendar.getInstance();
		//Fetch all sets, for every exercise
		for(Exercise e:allExercises){
			Log.d("ExerTracker", "Creating series for " + e.getName());
			dataSeries = new XYSeries(e.getName());
			//allSetsAllExercises.add(setsDB.getAllSets(e.getId()));	
			allSets = setsDB.getTotalRepsByDate(e.getId());
			for(Set set: allSets) {
				Log.d("ExerTracker", "adding " + String.valueOf(set.getReps()) + " reps for " + set.getStartTime());
				cal.setTime(set.getStartTime());
				dataSeries.add(cal.getTimeInMillis(), set.getReps());
			}
			myData.addSeries(dataSeries);
		}
			
		
		/*
		dataSeries.add(0, 2);
		dataSeries.add(1, 1);
		dataSeries.add(2, 4);
		dataSeries.add(3, 3);
		dataSeries.add(4, 2);
		dataSeries.add(5, 6);
		myData.addSeries(dataSeries);
		
		XYSeries dataSeries2 = new XYSeries ("Pushups");
		dataSeries2.add(0, 1);
		dataSeries2.add(1, 1);
		dataSeries2.add(2, 2);
		dataSeries2.add(3, 1);
		dataSeries2.add(4, 2);
		dataSeries2.add(5, 4); 
		myData.addSeries(dataSeries2);
		*/

		exercisesDB.close();
		setsDB.close();
		return myData;
	}
	
	public XYMultipleSeriesRenderer getMyRenderer() {
		XYMultipleSeriesRenderer myRenderer = new XYMultipleSeriesRenderer();
		
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.BLUE);
		r.setLineWidth(10);
		r.setPointStyle(PointStyle.SQUARE);  //  CIRCLE, DIAMOND, POINT, TRIANGLE, X
		r.setFillPoints(true);
		r.setFillBelowLine(false);
		
		myRenderer.addSeriesRenderer(r);
		
		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2.setColor(Color.RED);
		r2.setLineWidth(10);
		r2.setPointStyle(PointStyle.DIAMOND);  //  CIRCLE, DIAMOND, POINT, TRIANGLE, X
		r2.setFillPoints(true);
		r2.setFillBelowLine(false);
		
		myRenderer.addSeriesRenderer(r2);
		
		XYSeriesRenderer r3 = new XYSeriesRenderer();
		r3.setColor(Color.WHITE);
		r3.setLineWidth(10);
		r3.setPointStyle(PointStyle.CIRCLE);  //  CIRCLE, DIAMOND, POINT, TRIANGLE, X
		r3.setFillPoints(true);
		r3.setFillBelowLine(false);
		
		myRenderer.addSeriesRenderer(r3);
		
		XYSeriesRenderer r4 = new XYSeriesRenderer();
		r4.setColor(Color.GREEN);
		r4.setLineWidth(10);
		r4.setPointStyle(PointStyle.X);  //  CIRCLE, DIAMOND, POINT, TRIANGLE, X
		r4.setFillPoints(true);
		r4.setFillBelowLine(false);
		
		myRenderer.addSeriesRenderer(r4);
		
		return myRenderer;			    
    }
	
}