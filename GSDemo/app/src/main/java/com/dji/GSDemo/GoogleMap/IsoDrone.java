package com.dji.GSDemo.GoogleMap;

import android.util.Log;

import org.asta.isoObject.*;

public class IsoDrone extends TestObject{
    IsoDrone(String ip) {
        super(ip);
    }
    IsoDrone() {
        super();
        System.out.println("Drone init...");

    }

    @Override
    public void onStateChange() {
        System.out.println("State is now " + this.getCurrentStateName());
    }
    @Override
    public void handleAbort() {
        System.out.println("Aborting...");
    }


    @Override
    public String getName() {
        return super.getName();
    }
}
