package com.bhjy.ExerTracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class ExerTrackerPreferences extends PreferenceActivity implements OnPreferenceChangeListener {
	final String motionSensitivityPreference = "motionSensitivityPreference";
	ListPreference prefMotionSensitivity;
	SharedPreferences mPrefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		addPreferencesFromResource(R.xml.preferences);
		PreferenceScreen prefs = getPreferenceScreen();
		
		prefMotionSensitivity = (ListPreference) prefs.findPreference(motionSensitivityPreference);
		prefMotionSensitivity.setOnPreferenceChangeListener(this);
		
	}
	
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference == prefMotionSensitivity) {
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString(motionSensitivityPreference, (String) newValue);
			editor.commit();

			return true;
		}
		return false;
	}
}
