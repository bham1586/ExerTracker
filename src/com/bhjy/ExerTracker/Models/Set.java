package com.bhjy.ExerTracker.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Set {
	private long id;
	private long reps;
	private Date startTime;
	private long duration;
	private long weight;
	private String comments;
	private Exercise exercise;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getReps() {
		return reps;
	}

	public void setReps(long reps) {
		this.reps = reps;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm aa");
		return String.valueOf(reps) + " reps at " + myFormat.format(startTime);
	}
}
