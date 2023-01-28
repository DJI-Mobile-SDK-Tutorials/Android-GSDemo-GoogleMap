package com.dji.GSDemo.GoogleMap;

import android.util.Log;

import org.asta.isoObject.*;
import org.locationtech.proj4j.ProjCoordinate;

public class IsoDrone extends TestObject{

    public TrajectoryWaypointVector reducedTraj;

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

    public void reducePoints(double epsilon){
        TrajectoryWaypointVector traj = this.getTrajectory();
        this.reducedTraj = douglasPeucker(traj, epsilon);
    }

    TrajectoryWaypointVector getReducedTraj(){
        return this.reducedTraj;
    }

    private static final double distanceBetweenPoints(double vx, double vy, double wx, double wy) {
        return Math.pow(vx - wx, 2) + Math.pow(vy - wy , 2);
    }

    private static final double distanceToSegmentSquared(double px, double py, double vx, double vy, double wx, double wy) {
        final double l2 = distanceBetweenPoints(vx, vy, wx, wy);
        if (l2 == 0)
            return distanceBetweenPoints(px, py, vx, vy);
        final double t = ((px - vx) * (wx - vx) + (py - vy) * (wy - vy)) / l2;
        if (t < 0)
            return distanceBetweenPoints(px, py, vx, vy);
        if (t > 1)
            return distanceBetweenPoints(px, py, wx, wy);
        return distanceBetweenPoints(px, py, (vx + t * (wx - vx)), (vy + t * (wy - vy)));
    }

    private static final double perpendicularDistance(double px, double py, double vx, double vy, double wx, double wy) {
        return Math.sqrt(distanceToSegmentSquared(px, py, vx, vy, wx, wy));
    }

    private static final void douglasPeucker(TrajectoryWaypointVector traj, int s, int e, double epsilon, TrajectoryWaypointVector resultTraj) {
        // Find the point with the maximum distance
        double dmax = 0;
        int index = 0;

        final int start = s;
        final int end = e-1;
        for (int i=start+1; i<end; i++) {
            // Point
            final double px = traj.get(i).getPos().getXCoord_m();
            final double py = traj.get(i).getPos().getYCoord_m();
            // Start
            final double vx = traj.get(start).getPos().getXCoord_m();
            final double vy = traj.get(start).getPos().getYCoord_m();
            // End
            final double wx = traj.get(end).getPos().getXCoord_m();
            final double wy = traj.get(end).getPos().getYCoord_m();
            final double d = perpendicularDistance(px, py, vx, vy, wx, wy);
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, call recursively
        if (dmax > epsilon) {
            douglasPeucker(traj, s, index, epsilon, resultTraj);
            douglasPeucker(traj, index, e, epsilon, resultTraj);
        } else {
            if ((end-start)>0) {
                resultTraj.add(traj.get(start));
                resultTraj.add(traj.get(end));
            } else {
                resultTraj.add(traj.get(start));
            }
        }
    }

    public static final TrajectoryWaypointVector douglasPeucker(TrajectoryWaypointVector traj, double epsilon) {
        final TrajectoryWaypointVector trajResult = new TrajectoryWaypointVector();
        douglasPeucker(traj, 0, traj.size(), epsilon, trajResult);
        return trajResult;
    }
}





