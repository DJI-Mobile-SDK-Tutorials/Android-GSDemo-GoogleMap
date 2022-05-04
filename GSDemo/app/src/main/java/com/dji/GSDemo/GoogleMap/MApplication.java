package com.dji.GSDemo.GoogleMap;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.secneo.sdk.Helper;

import org.asta.isoObject.CartesianPosition;

public class MApplication extends Application {
    static {
        try {
            System.loadLibrary("TCPUDPSocket");
            System.loadLibrary("isoObject_wrap");
        }catch(Exception e)
        {
            Log.wtf("Error", e);
        }



    }


    private DJIDemoApplication fpvDemoApplication;
    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this);
        if (fpvDemoApplication == null) {
            fpvDemoApplication = new DJIDemoApplication();
            fpvDemoApplication.setContext(this);
        }
    }





    @Override
    public void onCreate() {

        super.onCreate();
       // Task droneTask = new Task();
       // droneTask.run();


        fpvDemoApplication.onCreate();

    }
}



class Task implements Runnable {
    @Override
    public void run() {
        IsoDrone drone = new IsoDrone("192.168.75.127");
        double test = 0.01;


         while(true) {
            try {
               Thread.sleep(100);
               Log.wtf("Name", drone.getName());
               Log.wtf("State", drone.getCurrentStateName());
               Log.wtf("IPv4", Utils.getIPAddress(true)); // IPv4
                CartesianPosition dronePos = new CartesianPosition();
                dronePos.setXCoord_m(test);
                dronePos.setYCoord_m(20);
                dronePos.setZCoord_m(30);
                dronePos.setHeading_rad(0);
                test += 0.1;
                dronePos.setIsPositionValid(true);
                dronePos.setIsHeadingValid(true);
                drone.setPosition(dronePos);

//
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
         }
    }
}


/*
class AsyncTaskRunner extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        IsoDrone drone = new IsoDrone("192.168.21.87");
        Log.wtf("IPv4", Utils.getIPAddress(true)); // IPv4
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onProgressUpdate(String... text) {
    }
}
*/


