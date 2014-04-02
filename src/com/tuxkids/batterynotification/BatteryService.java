package com.tuxkids.batterynotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BatteryService extends Service{
    private static String TAG = "BatteryNotification+";
    
    General Gl = new General();
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @SuppressWarnings("deprecation")

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent dialogIntent = new Intent(getBaseContext(), General.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.setAction(Intent.ACTION_BATTERY_CHANGED);
        getApplication().startActivity(dialogIntent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "BatteryService destroyed");
    }
}