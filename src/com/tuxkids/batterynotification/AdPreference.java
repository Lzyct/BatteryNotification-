package com.tuxkids.batterynotification;


import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class AdPreference extends Preference {


    public AdPreference(Context context, AttributeSet attrs, int defStyle) {super    (context, attrs, defStyle);}

    public AdPreference(Context context, AttributeSet attrs) {super(context, attrs);}

    public AdPreference(Context context) {super(context);}
    
   

    @Override

    protected View onCreateView(ViewGroup parent) {

        // this will create the linear layout defined in ads_layout.xml

        View view = super.onCreateView(parent);


        // the context is a PreferenceActivity

        Activity activity = (Activity)getContext();


        AdSize adsize = AdSize.SMART_BANNER;
        
        // Create the adView
        DisplayMetrics displayMetrics = activity.getResources()
                .getDisplayMetrics();

        int width  = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        
        if(width >= 728 && height >= 90 ) {
			adsize = AdSize.BANNER;
			System.out.println("728 x 90");
		} else if (width >= 468 && height >= 60 ) {
			adsize = AdSize.BANNER;
			System.out.println("468 x 60");
		} else if (width >= 320 && height >= 50 ) {
			adsize = AdSize.BANNER;
			System.out.println("320 x 50");
		}
        AdView adView = new AdView(activity, adsize, "ca-app-pub-2962932702636730/9760491605");
        ((LinearLayout)view).addView(adView);
        AdRequest request = new AdRequest();
        adView.loadAd(request);  


        // Initiate a generic request to load it with an ad
   


        return view;    

    }

} 

