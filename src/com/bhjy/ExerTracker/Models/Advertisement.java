package com.bhjy.ExerTracker.Models;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.bhjy.ExerTracker.R;
import com.google.ads.*;


public class Advertisement {
	
	final String MY_AD_UNIT_ID = "a14fbd671e08f8a";
	private AdView adView;
	
	public void setUpAds(Activity activity) {
    	// Create the adView
        adView = new AdView(activity, AdSize.BANNER, MY_AD_UNIT_ID);

        // Lookup your LinearLayout assuming it’s been given
        // the attribute android:id="@+id/mainLayout"
        RelativeLayout layout = (RelativeLayout)activity.findViewById(R.id.mainLayout);

        // Add the adView to it
        layout.addView(adView);

        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
    }
	
	 public void destroy() {
		 if (adView != null) {
			 adView.destroy();
		 }
	 }
}
