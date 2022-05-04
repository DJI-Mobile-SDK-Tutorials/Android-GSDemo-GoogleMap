package com.dji.GSDemo.GoogleMap;

import android.util.Log;

import org.asta.isoObject.*;

public class IsoDrone extends TestObject{
    IsoDrone(int ip) {
        super(ip);
        System.out.println("Drone init...int");

    }

    IsoDrone(String ip) {
        super(ip);
        System.out.println("Drone init...ip");

    }
    IsoDrone() {
        super();
        System.out.println("Drone init...");

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
