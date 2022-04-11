package com.dji.GSDemo.GoogleMap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.model.LocationCoordinate2D;
import dji.common.useraccount.UserAccountState;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.useraccount.UserAccountManager;


import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class Waypoint1Activity extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback {

    protected static final String TAG = "GSDemoActivity";

    private GoogleMap gMap;

    private Button locate, add, clear, testcircle;
    private Button config, upload, start, stop;

    private boolean isAdd = false;

    private LatLng startLatLong;
    private double droneLocationLat = 181, droneLocationLng = 181, droneAltitude = 0;
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private Marker droneMarker = null;

    private float altitude = 100.0f;
    private float mSpeed = 10.0f;

    private List<Waypoint> waypointList = new ArrayList<>();

    public static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
    public CRSFactory crsFactory = new CRSFactory();
    public CoordinateReferenceSystem WGS84 = crsFactory.createFromParameters("WGS84","+proj=longlat +datum=WGS84 +no_defs");

    private ArrayList<WaypointSetting> waypointSettings = new ArrayList<>();

    private Double mRot = 0.0;
    private boolean missionUploaded = false;

    @Override
    protected void onResume(){
        super.onResume();
        initFlightController();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(mReceiver);
        removeListener();
        super.onDestroy();
    }

    /**
     * @Description : RETURN Button RESPONSE FUNCTION
     */
    public void onReturn(View view){
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void setResultToToast(final String string){
        Waypoint1Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Waypoint1Activity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        locate = (Button) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        testcircle = (Button) findViewById(R.id.testcircle);
        config = (Button) findViewById(R.id.pauseresume);
        upload = (Button) findViewById(R.id.arm);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        testcircle.setOnClickListener(this);
        config.setOnClickListener(this);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

    }

    private void generateTestCircleCoordinates(LatLng origin, double radius, double altitude, float speed, int nofPoints, boolean headingTowardsCenter){

        double angularStep = 2*Math.PI/nofPoints;
        double currentAngle = 0;
        double currentAngleRot = 0;
        int wpHeading = 0;
        for(int i = 0; i <= nofPoints; i ++){

            if(headingTowardsCenter) {
                currentAngleRot = rotateUnitCircleAngleToDroneYawRad(currentAngle, Math.PI);
            } else currentAngleRot = currentAngle;
            wpHeading = convertToDroneYawRangeDeg((180/Math.PI)*currentAngleRot);

            WaypointSetting wps = new WaypointSetting(coordCartToGeo(origin, new ProjCoordinate(radius*Math.cos(currentAngle), radius*Math.sin(currentAngle), 0)), new ProjCoordinate());
            this.waypointSettings.add(wps);
            this.waypointSettings.get(i).heading = wpHeading;
            this.waypointSettings.get(i).geo.z = altitude;
            this.waypointSettings.get(i).speed = speed;
            currentAngle += angularStep;
        }
        WaypointSetting wps = new WaypointSetting(coordCartToGeo(origin, new ProjCoordinate(0, 0)), new ProjCoordinate());
        wps.heading = 0;
        wps.speed = speed;
        wps.geo.z = altitude;
        this.waypointSettings.add(wps);
    }

    private Double rotateUnitCircleAngleToNorthHeadingRad(double yaw){
        Double yawRot = 0.0;
        if (yaw >= 0 && yaw <= Math.PI/2) yawRot = Math.PI/2 - yaw;
        else if (yaw > Math.PI/2 && yaw <= 2*Math.PI) yawRot = Math.PI/2 + 2*Math.PI - yaw;
        return yawRot;
    }

    private Double rotateUnitCircleAngleToDroneYawRad(double yaw, double rot){
        Double yawRot = 0.0;
        if (yaw >= 0 && yaw <= Math.PI) yawRot = yaw + rot;
        else if (yaw > Math.PI && yaw <= 2 * Math.PI) yawRot = yaw - rot;
        return yawRot;
    }

    private int convertToDroneYawRangeDeg(double yaw){
        int droneYaw = 0;
        if(yaw >= 0 && yaw <= 90) droneYaw = 90 - (int)(yaw);
        else if(yaw > 90 && yaw < 270) droneYaw = (90 - (int) (yaw));
        else if(yaw >= 270) droneYaw = ( 450 - (int)(yaw));
        return droneYaw;
    }

    private String buildOriginProjString(double latitude, double longitude){
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("+proj=tmerc +lat_0=" + latitude + " +lon_0=" + longitude + " +k=0.9996 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
        return stringBuffer.toString();
    }

    private ProjCoordinate coordCartToGeo(LatLng origin, ProjCoordinate xyz){
        String projStr = buildOriginProjString(origin.latitude, origin.longitude);
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateReferenceSystem UTM = crsFactory.createFromParameters("UTM", projStr);
        CoordinateTransform utmToWgs = ctFactory.createTransform(UTM, WGS84);
        ProjCoordinate result = new ProjCoordinate();
        utmToWgs.transform(new ProjCoordinate(xyz.x, xyz.y), result);
        return result;
    }

    private ProjCoordinate coordGeoToCart(LatLng origin, ProjCoordinate llh){
        String projStr = buildOriginProjString(origin.latitude, origin.longitude);
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateReferenceSystem UTM = crsFactory.createFromParameters("UTM", projStr);
        CoordinateTransform wgsToUtm = ctFactory.createTransform(WGS84, UTM);
        ProjCoordinate result = new ProjCoordinate();
        wgsToUtm.transform(new ProjCoordinate(llh.y, llh.x), result);
        result.z = llh.z;
        return result;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);

        }

        setContentView(R.layout.activity_waypoint1);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        initUI();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addListener();

    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };

    private void onProductConnectionChange()
    {
        initFlightController();
        loginAccount();
    }

    private void loginAccount(){

        UserAccountManager.getInstance().logIntoDJIUserAccount(this,
                new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
                    @Override
                    public void onSuccess(final UserAccountState userAccountState) {
                        Log.e(TAG, "Login Success");
                    }
                    @Override
                    public void onFailure(DJIError error) {
                        setResultToToast("Login Error:"
                                + error.getDescription());
                    }
                });
    }

    private void initFlightController() {

        BaseProduct product = DJIDemoApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {

                @Override
                public void onUpdate(FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                    droneAltitude = djiFlightControllerCurrentState.getAircraftLocation().getAltitude();
                    updateDroneLocationData();
                }
            });
        }
    }

    //Add Listener for WaypointMissionOperator
    private void addListener() {
        if (getWaypointMissionOperator() != null){
            getWaypointMissionOperator().addListener(eventNotificationListener);
        }
    }

    private void removeListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().removeListener(eventNotificationListener);
        }
    }

    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
        @Override
        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {

        }

        @Override
        public void onUploadUpdate(WaypointMissionUploadEvent uploadEvent) {

        }

        @Override
        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {

        }

        @Override
        public void onExecutionStart() {

        }

        @Override
        public void onExecutionFinish(@Nullable final DJIError error) {
            setResultToToast("Execution finished: " + (error == null ? "Success!" : error.getDescription()));
        }
    };

    public WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            if (DJISDKManager.getInstance().getMissionControl() != null){
                instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
            }
        }
        return instance;
    }

    private void setUpMap() {
        gMap.setOnMapClickListener(this);// add the listener for click for amap object

    }

    @Override
    public void onMapClick(LatLng point) {
        if (isAdd == true){
            markWaypoint(point);
            Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (waypointMissionBuilder != null) {
                waypointList.add(mWaypoint);
                //waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                waypointMissionBuilder.waypointCount(waypointList.size());
            }else
            {
                waypointMissionBuilder = new WaypointMission.Builder();
                waypointList.add(mWaypoint);
                //waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                waypointMissionBuilder.waypointCount(waypointList.size());
            }
        }else{
            //setResultToToast("Cannot Add Waypoint");
        }
    }

    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    // Update the drone location based on states from MCU.
    private void updateDroneLocationData(){

        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        //Create MarkerOptions object
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        if(missionUploaded) {
            ProjCoordinate pc = coordGeoToCart(startLatLong, new ProjCoordinate(droneLocationLat, droneLocationLng));
            try {
                //Mark the drone on map
                mRot = Math.acos(pc.x / (Math.sqrt(Math.pow(pc.x, 2) + Math.pow(pc.y, 2))));
                if((pc.x < 0 && pc.y < 0) || (pc.x > 0 && pc.y < 0)) {
                    mRot = 2*Math.PI - mRot;
                }
                mRot = rotateUnitCircleAngleToNorthHeadingRad(mRot);
                mRot = mRot * 180 / Math.PI;
                markerOptions.rotation(mRot.floatValue()).icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));;

                //Check
                ProjCoordinate fp = coordGeoToCart(startLatLong, new ProjCoordinate(waypointSettings.get(1).geo.y, waypointSettings.get(1).geo.x));

                Double radlim = 0.5;
                Double xlim = (radlim*Math.cos(0));
                Double ylim =  (radlim*Math.sin(Math.PI/2));
                Button button = (Button)findViewById(R.id.pauseresume);
                if(pc.x > fp.x - xlim  &&  pc.x < fp.x + xlim && pc.y > fp.y - ylim  &&  pc.y < fp.y + ylim && button.getText().equals("Arming")){
                   pauseWaypointMission();
                   button.setText("Armed");
                }

            } catch (Exception e) {

            }
        } else markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (droneMarker != null) {
                    droneMarker.remove();
                }

                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    droneMarker = gMap.addMarker(markerOptions);
                }
            }
        });
    }

    private void markWaypoint(LatLng point){
        //Create MarkerOptions object
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = gMap.addMarker(markerOptions);
        mMarkers.put(mMarkers.size(), marker);
    }

    private void deployTestCircle(){
        waypointMissionBuilder = new WaypointMission.Builder();
        for (int i = 0; i < this.waypointSettings.size(); i ++)
        {
            Waypoint wp = new Waypoint();
            wp.coordinate = new LocationCoordinate2D(this.waypointSettings.get(i).geo.y, this.waypointSettings.get(i).geo.x);
            wp.altitude = (float)this.waypointSettings.get(i).geo.z + i*0.0f;
            wp.speed = (float)this.waypointSettings.get(i).speed + i*0.0f;
            try {
                wp.heading = this.waypointSettings.get(i).heading;
            } catch (Exception e){
                setResultToToast("e = "+ e.getCause() + ", " + e.getMessage());
            }
            //setResultToToast("wp.heading = "+ waypointSettings.get(i).geo.y + ", " + waypointSettings.get(i).geo.x + ", " + waypointSettings.get(i).geo.z);
            waypointList.add(wp);
            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        }
        setResultToToast("Number of waypoints " + this.waypointSettings.size());
        mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
        mSpeed = 5.0f;
        mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
        altitude = (float)this.waypointSettings.get(0).geo.z;
        configWayPointMission();
        startLatLong = new LatLng(droneLocationLat, droneLocationLng);
        uploadWayPointMission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate:{
                updateDroneLocationData();
                cameraUpdate(); // Locate the drone's place
                break;
            }
            case R.id.add:{
                break;
            }
            case R.id.clear: {
                setResultToToast("Clear map!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gMap.clear();
                    }

                });
                waypointList.clear();
                waypointMissionBuilder.waypointCount(0);
                updateDroneLocationData();
                Button button = (Button)findViewById(R.id.pauseresume);
                button.setText("Pause");
                break;
            }
            case R.id.testcircle:{
                this.waypointSettings.clear();
                generateTestCircleCoordinates(new LatLng(droneLocationLat, droneLocationLng), 10, 1, 1,19, true);
                for(int i = 0; i < this.waypointSettings.size(); i ++){
                    LatLng mpoint = new LatLng(this.waypointSettings.get(i).geo.y, this.waypointSettings.get(i).geo.x);
                    markWaypoint(mpoint);
                }
                deployTestCircle();
                break;
            }
            case R.id.pauseresume:{
                //showSettingDialog();
                Button button = (Button)findViewById(R.id.pauseresume);
                if(button.getText().equals("Pause")){
                    pauseWaypointMission();
                    button.setText("Resume");
                } else if(button.getText().equals("Resume")){
                    resumeWaypointMission();
                    button.setText("Pause");
                } else if(button.getText().equals("Armed")){
                    resumeWaypointMission();
                    button.setText("Running");
                }
                break;
            }
            case R.id.arm:{
                //startLatLong = new LatLng(droneLocationLat, droneLocationLng);
                //uploadWayPointMission();
                startWaypointMission();
                Button button = (Button)findViewById(R.id.pauseresume);
                button.setText("Arming");
                break;
            }
            case R.id.start:{
                //startWaypointMission();
                resumeWaypointMission();
                break;
            }
            case R.id.stop:{
                stopWaypointMission();
                break;
            }
            default:
                break;
        }
    }

    private void cameraUpdate(){
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        float zoomlevel = (float) 22.0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
        gMap.moveCamera(cu);

    }

    private void enableDisableAdd(){
        if (isAdd == false) {
            isAdd = true;
            add.setText("Exit");
        }else{
            isAdd = false;
            add.setText("Add");
        }
    }

    private void showSettingDialog(){
        LinearLayout wayPointSettings = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
        RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
        RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);

        speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lowSpeed){
                    mSpeed = 3.0f;
                } else if (checkedId == R.id.MidSpeed){
                    mSpeed = 5.0f;
                } else if (checkedId == R.id.HighSpeed){
                    mSpeed = 10.0f;
                }
            }

        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select finish action");
                if (checkedId == R.id.finishNone){
                    mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
                } else if (checkedId == R.id.finishGoHome){
                    mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
                } else if (checkedId == R.id.finishAutoLanding){
                    mFinishedAction = WaypointMissionFinishedAction.AUTO_LAND;
                } else if (checkedId == R.id.finishToFirst){
                    mFinishedAction = WaypointMissionFinishedAction.GO_FIRST_WAYPOINT;
                }
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select heading");

                if (checkedId == R.id.headingNext) {
                    mHeadingMode = WaypointMissionHeadingMode.AUTO;
                } else if (checkedId == R.id.headingInitDirec) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_INITIAL_DIRECTION;
                } else if (checkedId == R.id.headingRC) {
                    mHeadingMode = WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER;
                } else if (checkedId == R.id.headingWP) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
                }
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                        String altitudeString = wpAltitude_TV.getText().toString();
                        altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));
                        Log.e(TAG,"altitude "+altitude);
                        Log.e(TAG,"speed "+mSpeed);
                        Log.e(TAG, "mFinishedAction "+mFinishedAction);
                        Log.e(TAG, "mHeadingMode "+mHeadingMode);
                        configWayPointMission();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .create()
                .show();
    }

    String nulltoIntegerDefalt(String value){
        if(!isIntValue(value)) value="0";
        return value;
    }

    boolean isIntValue(String val)
    {
        try {
            val=val.replace(" ","");
            Integer.parseInt(val);
        } catch (Exception e) {return false;}
        return true;
    }

    private void configWayPointMission(){

        if (waypointMissionBuilder == null){

            waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.CURVED);

        }else
        {
            waypointMissionBuilder.finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.CURVED);

        }

        if (waypointMissionBuilder.getWaypointList().size() > 0){

           // for (int i=0; i< waypointMissionBuilder.getWaypointList().size(); i++){
            //    waypointMissionBuilder.getWaypointList().get(i).altitude = altitude;
           // }

           // setResultToToast("Set Waypoint altitude successfully");
        }

        DJIError error = getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
        if (error == null) {
            setResultToToast("loadWaypoint succeeded");
        } else {
            setResultToToast("loadWaypoint failed " + error.getDescription() + " " + error.toString());
        }
    }

    private void uploadWayPointMission(){

        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    setResultToToast("Mission upload successfully!");
                    missionUploaded = true;
                } else {
                    missionUploaded = false;
                    setResultToToast("Mission upload failed, error: " + error.getDescription() + " retrying...");
                    getWaypointMissionOperator().retryUploadMission(null);
                }
            }
        });

    }

    private void startWaypointMission(){

        getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });
    }

    private void pauseWaypointMission(){

        getWaypointMissionOperator().pauseMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission paused: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });
    }

    private void resumeWaypointMission(){

        getWaypointMissionOperator().resumeMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission resumed: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });
    }

    private void stopWaypointMission(){

        getWaypointMissionOperator().stopMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
            setUpMap();
        }

        //LatLng shenzhen = new LatLng(22.5362, 113.9454);
        LatLng astazero = new LatLng(57.777521, 12.781442);
        gMap.addMarker(new MarkerOptions().position(astazero).title("Marker at AstaZero"));
        cameraUpdate(); // Locate the drone's place
    }

}
