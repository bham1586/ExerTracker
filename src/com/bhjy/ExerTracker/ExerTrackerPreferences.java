package com.bhjy.ExerTracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ExerTrackerPreferences extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences mPrefs = getPreferenceManager().getSharedPreferences();
		addPreferencesFromResource(R.xml.preferences);
	}
}
