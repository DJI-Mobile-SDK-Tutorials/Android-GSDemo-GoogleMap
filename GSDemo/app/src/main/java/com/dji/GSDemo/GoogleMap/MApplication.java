package com.dji.GSDemo.GoogleMap;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.secneo.sdk.Helper;

import org.asta.isoObject.CartesianPosition;
import org.locationtech.proj4j.ProjCoordinate;

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
        //Task droneTask = new Task();
        //droneTask.run();


        fpvDemoApplication.onCreate();

    }
}



class Task implements Runnable {
    @Override
    public void run() {
        IsoDrone drone = new IsoDrone("192.168.75.127");
        double test = 0.01;
        String lastDroneState = "";

         while(true) {
            try {
               Thread.sleep(100);
               Log.wtf("Name", drone.getName());
               Log.wtf("State", drone.getCurrentStateName());
               Log.wtf("IPv4", Utils.getIPAddress(true)); // IPv4

                //Log.wtf("Lat: ", String.valueOf(drone.getOrigin().getLatitude_deg()));
                //Log.wtf("Log: ", String.valueOf(drone.getOrigin().getLongitude_deg()));
                //Log.wtf("alt: ", String.valueOf(drone.getOrigin().getAltitude_m()));

                if (drone.getCurrentStateName().equals("Armed") || (drone.getCurrentStateName().equals("Running"))) {

                    CartesianPosition dronePos = new CartesianPosition();
                    dronePos.setXCoord_m(test);
                    dronePos.setYCoord_m(20);
                    dronePos.setZCoord_m(30);
                    dronePos.setHeading_rad(0);
                    test += 0.1;
                    dronePos.setIsPositionValid(true);
                    dronePos.setIsHeadingValid(true);
                    drone.setPosition(dronePos);
                }

                if (drone.getCurrentStateName().equals("Init") && lastDroneState != "Init") {
                    Log.wtf("Error", "Init");

                    lastDroneState = "Init";
                } else if (drone.getCurrentStateName().equals("PreArming") && lastDroneState != "PreArming") {
                    Log.wtf("Error", "PreArming");
                    lastDroneState = "PreArming";

                } else if (drone.getCurrentStateName().equals("Armed") && lastDroneState != "Armed") {

                    Log.wtf("Error", "Armed");
                    lastDroneState = "Armed";
                } else if (drone.getCurrentStateName().equals("Disarmed") && lastDroneState != "Disarmed") {

                    Log.wtf("Error", "Disarmed");
                    lastDroneState = "Disarmed";
                } else if (drone.getCurrentStateName().equals("PreRunning") && lastDroneState != "PreRunning") {
                    Log.wtf("Error", "PreRunning");
                    lastDroneState = "PreRunning";
                } else if (drone.getCurrentStateName().equals("Running") && lastDroneState != "Running") {

                    Log.wtf("Error", "Running");
                    lastDroneState = "Running";
                } else if (drone.getCurrentStateName().equals("NormalStop") && lastDroneState != "NormalStop") {
                    Log.wtf("Error", "NormalStop");
                    lastDroneState = "NormalStop";
                } else if (drone.getCurrentStateName().equals("EmergencyStop") && lastDroneState != "EmergencyStop") {
                    Log.wtf("Error", "EmergencyStop");
                    lastDroneState = "EmergencyStop";
                }

                //Log.wtf("TrajName: ", drone.getTrajectory());

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


