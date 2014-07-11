package com.tuxkids.batterynotification;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class General extends PreferenceActivity {
	boolean low, full, charge, discharge;
	CheckBoxPreference cb1, cb2, cb3, cb4;
		
	// boolean to fix always playing tone
	boolean a, b, c, d,sambut;

	
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_screen);
		//not visible for cyantux users
		//setContentView(R.layout.ad_layout);
		
		//set sambutan just show once when first application created
		SharedPreferences prefs = getSharedPreferences("sambutan", MODE_PRIVATE);
		Editor mEditor = prefs.edit();
		boolean sambutan= prefs.getBoolean("sambutan",false);
		if (sambutan == false){
			sambutan();
			sambut=true;
			mEditor.putBoolean("sambutan",sambut);
			mEditor.commit();
		}
		//moveTaskToBack(true);		
	}

	//First Notif
	public void sambutan(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true)
					.setTitle("Tips")
					.setMessage("[*]Press back button when you will close this app\n"+
								"[*]Need Restart Your Phone for Run Application Service")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					
			AlertDialog alert = builder.create();
			alert.show();
		
	}

	
	// mendapatkan status checkbox
	public void getChecked() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		cb1 = (CheckBoxPreference) findPreference("preflow");
		cb2 = (CheckBoxPreference) findPreference("preffull");
		cb3 = (CheckBoxPreference) findPreference("prefcharge");
		cb4 = (CheckBoxPreference) findPreference("prefdischarge");

		low = sharedPrefs.getBoolean("preflow", false);
		full = sharedPrefs.getBoolean("preffull", false);
		charge = sharedPrefs.getBoolean("prefcharge", false);
		discharge = sharedPrefs.getBoolean("prefdischarge", false);

	}
}
