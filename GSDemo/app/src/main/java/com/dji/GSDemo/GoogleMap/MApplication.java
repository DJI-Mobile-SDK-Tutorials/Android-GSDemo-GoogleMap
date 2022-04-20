package com.dji.GSDemo.GoogleMap;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.secneo.sdk.Helper;

public class MApplication extends Application {
    static {
        try {
            System.loadLibrary("TCPUDPSocket");
            System.loadLibrary("isoObject_wrap");
        }catch(Exception e)
        {
            Log.wtf("Error", e);
        }

        Task droneTask = new Task();
        droneTask.run();
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
        //fpvDemoApplication.onCreate();

    }
}




class Task implements Runnable {
    @Override
    public void run() {
       IsoDrone drone = new IsoDrone();
    while(true) {
       try {
          Thread.sleep(1000);
          Log.wtf("Name", drone.getName());
          Log.wtf("State", drone.getCurrentStateName());
          Log.wtf("IPv4", Utils.getIPAddress(true)); // IPv4

       } catch (InterruptedException e) {
           e.printStackTrace();
       }
    }
    }
}