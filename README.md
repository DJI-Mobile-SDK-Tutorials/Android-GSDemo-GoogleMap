#### This tutorial is now deprecated, please refer to <https://developer.dji.com/mobile-sdk/documentation/android-tutorials/GSDemo-Google-Map.html> in DJI Developer Website.

# Creating a MapView and Waypoint Application

<!-- toc -->

*If you come across any mistakes or bugs in this tutorial, please let us know using a Github issue, a post on the DJI forum, or commenting in the Gitbook. Please feel free to send us Github pull request and help us fix any issues. However, all pull requests related to document must follow the [document style](https://github.com/dji-sdk/Mobile-SDK-Tutorial/issues/19)*

---

In this tutorial, you will learn how to implement the DJIWaypoint Mission feature and get familiar with the usages of DJIMissionManager. 

Also you will know how to setup the DJI PC Simulator, upgrade your Inspire 1, Phantom 3 Professional and Phantom 3 Advanced's firmware to the lastest version, and how to test the Waypoint Mission API with DJI PC Simulator too. So let's get started!

You can download the demo project from this **Github Page**.

> Note: In this tutorial, we will use Inspire 1 for testing, use Android Studio 2.1.1 for developing the demo application, and use the [Google Map API](https://developers.google.com/maps/) for navigating.

## Using The DJI PC Simulator

### Introduction

The DJI PC Simulator is a flight simulator designed for SDK developers. The simulator creates a virtual 3D environment and provides data analysis from flight data transmitted to the PC via the UDP protocol.

**Supported Operating Systems**: Windows 7, Windows 8, and Windows 8.1

**Supported DJI Platforms**: Matrice 100, Inspire 1, Phantom 3 Professional, and Phantom 3 Advanced

### Installing and Setup the DJI PC Simulator

First, you should download the DJI PC Simulator and WIN Driver from here: <https://developer.dji.com/mobile-sdk/downloads> :

- DJI PC Simulator Installer & User Manual V1.0
- WIN Driver Installer

You must install the driver before running the simulator. Since the simulator only supports Windows, you should find a PC or install a Virtual Manchine(Like VMWare or Parallels Desktop) to run Windows on your Mac. Now, double click the **DJI_ WIN_Driver _Installer.exe** file to install it. If a dialog box saying "Please power on MC and connect it to PC via USB!" pops up, just ignore it, click **"YES"**, and follow the rest of the instructions to install the driver. 

Then, double click the **DJISimulator-Installer.exe** file and follow the instructions to install the program. 

### Using the DJI PC Simulator

**1**. The **Simulator Config** window will appear once you launch the DJI PC Simulator. Set the **Latitude** and **Longitude** values based on your preference. The **SN** represents the connected aircraft's SN number. If you do not see the **SN**, your aircraft is not connected properly to the PC or there is an issues with your firmware.

 ![Config](./Images/simulator_config.png)

---
**Note**: 

- The aircraft will not take off if the latitude and longitude values are near a [No Fly Zone](http://flysafe.dji.com/no-fly).

- Select **show log window** under the Log Settings tab to display the log window, as seen below:

 ![showLog](./Images/showLog.png)

---

**2**. Connect the aircraft to your PC via the Micro USB cable, turn on the remote controller and finally turn on the aircraft. Click **Display Simulator**, and you will see the screenshot below(it will show whatever aircraft you are using). In this case you see the Inspire 1:

 ![display](./Images/display.png)

---
**Note**: 

- Do **NOT** launch the DJI Pilot app when the DJI PC Simulator is running.

- Do **NOT** mount the propellers on the aircraft when the DJI PC Simulator is running, in case the motors start by accident. That would be extremely dangerous.

---

**3**. Start the simulation by clicking **Start Simulation**. You can use the remote controller to change the course of the aircraft by swithing the control mode to **P** or bring it back home with the **Return-to-Home** button on the remote controller.

Please check the data on the left down corner of view, World X, Y, Z represents the North-South, East- West, and Up-Down axes (take the North, East and Up directions as positive). 

**4**. In order to change the view angle, left-click and drag, and to scroll, zoom in and zoom out.

 ![zoomIn](./Images/zoomIn.png)
  
---
>**Note**: 
> You can also see the flight trace in the simulator when the aircraft is flying. Right click on the simulator window and select **setup** as shown below:
>
>![simulatorSetup](./Images/simulatorSetup.png)
> 
>Then select **show trace** under the simulator UI Config tab to display the flight trace, as shown below:
>
>![showTrace](./Images/showTrace.png)
>
>Here is the result:
>
>![flightTrace](./Images/flightTrace.png)
>
---

**5**. Click **Stop Simulation** to stop the simulation. Close the simulator, and turn off the aircraft, and turn off the remote controller after use. 

**Important**: If you want to stop midway through a Waypoint Mission, you should click **Stop Simulation** and stop the mission on the device you are running it from, otherwise the simulator may run the previous waypoint mission when you start it again.

For more info about DJI PC Simulator, please check the **DJI PC Simulator user manual.pdf** file, which is included in the download along with the simulator.

## Upgrading Your Aircraft's Firmware

It's important to make sure your aircraft's firmware supports the DJI Mobile SDK before going through the following steps. Please download the latest firmware version from here based on the aircraft you have: <https://developer.dji.com/mobile-sdk/downloads/> :

- Phantom 3 Professional Firmware
- Phantom 3 Advanced Firmware
- Inspire 1 Firmware

It's not necessary to upgrade the Remote Controller's firmware, just put the downloaded **bin** file in the SD Card, insert it to your aircraft's camera, and restart it to upgrade. It may take 10 ~ 30 minutes to finish the upgrade.

For the Inspire 1, you can check the upgrade status of the firmware by identifying the sound pattern from the aircraft:

- Upgrading: D D D D ... (fast) 
- Upgrade Success: D DD ...
- Upgrade Failed or Error: long beep sound

For the Phantom 3, you can check upgrade status of the firmware by looking at the camera status indicator: 

- Upgrading: camera status indicator will blink green and red
- Upgrade Success: camera sattus indicator will stop blinking 
- Upgrade Failed or Error: camera status indicator will show solid red

Regardless of what aircraft you are using, you can check whether or not the firmware was upgraded by checking the **txt** file generated during the upgrade process. 
  
## Implementing the UI of Application

We can use the map view to display waypoints and show the flight route of the aircraft when waypoint mission is being executed. Here, we take Google Map for an example.

### Configurating Android API Key

#### 1. Create the project

 Open Android Studio and select **File -> New -> New Project** to create a new project, named "GSDemo". Enter the company domain and package name (Here we use "com.dji.GSDemo.GoogleMap") you want and press Next. Set the mimimum SDK version as `API 19: Android 4.4 (KitKat)` for "Phone and Tablet" and press Next. Then select "Empty Activity" and press Next. Lastly, leave the Activity Name as "MainActivity", and the Layout Name as "activity_main", Press "Finish" to create the project.

#### 2. Setting Up Google Play Services

You can learn to install and set up the Google Play services SDK by referring to the setup guide presented in: <https://developers.google.com/android/guides/setup>

The **Google Maps Android API** is included in the Google Play Services Package. You can install the Google Play services SDK by using **Android SDK Manager** as shown below:

![installGooglePlayService](./Images/installGooglePlayServices.png)

After Google Play Services Package is installed. Open **AndroidManifest.xml** file of your app and add the following element as a child of the **\<application>** element:

~~~xml
	<meta-data android:name="com.google.android.gms.version"
		android:value="@integer/google_play_services_version" />
~~~

You can now start using the Google Maps Android APIs to develop your app.

#### 3. Generating SHA-1 Key

We can use Android Studio to generate a SHA-1 Key easily. Click on the **Gradle** tap on the right side of Android Studio. Select the project and navigate to **Tasks -> android -> signingReport**. 

![signingReport](./Images/signingReport.png)

Then double click the **signingReport** and check the Console area of Android Studio, you can find the SHA-1 key easily:

![SHA1](./Images/SHA-1Key.png)

#### 4. Applying for an Android API Key

Now, let's go to [Google Developer Console](https://console.developers.google.com/apis) to apply for an Android API Key. Login with your Google account and select the **Credentials** tap on the left side of **API Manager**. You will see the following screenshot here:

![CreateCredentials](./Images/createCredentials.png)

 Press the **Create credentials** button and select **API Key -> Android Key** to create an Android Key. Follow the screenshot below to fill in the content with your generated "SHA-1 Key" in the above steps:
 
 ![AndroidKey](./Images/createAndroidAPIKeys.png)
 
> Note: The "Package name" should be the same to your Android project's Package name.
 
 Then press "create" and you can get your Android API Key like this:
 
 ![AndroidAPIKey](./Images/androidAPIKeys.png)

 Finally, we should enable the **Google Maps Android API** as a service for your project too. Please tap on the **Overview** tab on the left side and select the "Google Maps Android API" item. Then press "Enable" button to enable the API as you seen below:
 
 ![EnableMapsAPI](./Images/enableMapsAPI.png)
 
#### 5. Adding Android API Key

Open the AndroidManifest.xml file, add the following elements as childs of **\<application>** element and substitute your Android API Key for "YOUR _ API_KEY" in the **value** attribute as shown below:

~~~xml
	<meta-data
		android:name="com.google.android.geo.API_KEY"
		android:value="YOUR_API_KEY" />

~~~

The first element sets the key "com.google.android.geo.API_KEY" to the value of your API key. The second one set the google play services version number.
 
Moreover, specify the permissions of your application needs, by adding **\<uses-permission>** elements as children of the **\<manifest>** element in the "AndroidManifest.xml" file. 

~~~xml
 <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_CONFIGURATION" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
~~~

Finally, we need to specify requirement for OpenGL ES version 2 by adding the following element inside the "manifest" element:

~~~xml
   <uses-feature
        android:glEsVersion="0x00020000"
        android:required="true" />
~~~

For more details of description on the permissions, refer to <https://developers.google.com/maps/documentation/android/config>.

#### 6. Adding Multidex Support with Gradle

In order to use Google Play Service in our project, we need to add Multidex support to avoid the the 64K limit with Gradle.

Modify the module-level `build.gradle` file configuration to include the support library and enable multidex output in both **defaultConfig** and **dependencies** parts, as shown in the following code snippet:

~~~xml
android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

    defaultConfig {
        ...
        minSdkVersion 19
        targetSdkVersion 23
        ...
        
        // Enabling multidex support.
        multiDexEnabled true
    }
    ...
}

dependencies {
  ...
  compile 'com.google.android.gms:play-services:8.4.0'
  compile 'com.android.support:multidex:1.0.1'
}
~~~

In the code above, we declare the "compileSdkVersion", "buildToolsVersion", "minSdkVersion" and "targetSdkVersion". Furthermore, we add the `compile 'com.google.android.gms:play-services:8.4.0'` to the "dependencies" to support google play service.

Then select **Tools->Android->Sync Project with Gradle Files** to sync the gradle files.

For more details about configuring your App for Multidex with Gradle, please check this link: <http://developer.android.com/tools/building/multidex.html>

### Importing the SDK

Unzip the Android SDK package downloaded from [DJI Developer Website](http://developer.dji.com/mobile-sdk/downloads/). Go to **File -> New -> Import Module**, enter the "API Library" folder location of the downloaded Android SDK package in the "Source directory" field. A "dJISDKLib" name will show in the "Module name" field. Press Next and Finish button to finish the settings.

 ![importSDK](./Images/importsSDK.png)

Next, double click on the "build.gradle(Module: app)" file to open it and add the `compile project(':dJISDKLIB')` at the bottom of **dependencies** part:

~~~xml
dependencies {
    ...
    compile 'com.google.android.gms:play-services:8.4.0'
    compile 'com.android.support:multidex:1.0.1'
    compile project(':dJISDKLIB')
}
~~~

Like we do before, select the **Tools -> Android -> Sync Project with Gradle Files** on the top bar and wait for Gradle project sync finish.

Now, let's right click on the 'app' module in the project navigator and click "Open Module Settings" to open the Project Struture window. Navigate to the "Dependencies" tab, you should find the "dJISDKLIB" appear in the list. Your SDK environmental setup should be ready now!

 ![dependencies](./Images/dependencies.png)
 
### Building the Layouts of MainActivity

#### 1. Creating DJIDemoApplication Class 

   Right-click on the package `com.dji.GSDemo.GoogleMap` in the project navigator and choose **New -> Java Class**, Type in "DJIDemoApplication" in the Name field and select "Class" as Kind field content.
   
   Next, Replace the code of the "DJIDemoApplication.java" file with the following:
   
~~~java
package com.dji.GSDemo.GoogleMap;
import android.app.Application;

public class DJIDemoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
~~~

   Here, we override the `onCreate()` method. and implement the `attachBaseContext()` method to install the multiDex. We can do some settings when the application is created here.
   
#### 2. Creating the MainActivity

##### Implementing the MainActivity Layout

Open the **activity_main.xml** layout file and replace the code with the following:

~~~xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#FFFFFF"
    tools:context="com.dji.GSDemo.GoogleMap.MainActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <TextView
            android:id="@+id/ConnectStatusTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="GSDemo"
            android:gravity="center"
            android:textColor="#000000"
            android:textSize="21sp"
            />
    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:id="@+id/locate"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Locate"
            android:layout_weight="1"/>
        <Button
            android:id="@+id/add"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Add"
            android:layout_weight="1"/>
        <Button
            android:id="@+id/clear"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Clear"
            android:layout_weight="1"/>
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <Button
            android:id="@+id/config"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Config"
            android:layout_weight="0.9"/>
        <Button
            android:id="@+id/prepare"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Prepare"
            android:layout_weight="0.9"/>
        <Button
            android:id="@+id/start"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Start"
            android:layout_weight="1"/>
        <Button
            android:id="@+id/stop"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Stop"
            android:layout_weight="1"/>
    </LinearLayout>

    <fragment
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        class="com.google.android.gms.maps.SupportMapFragment" />

</LinearLayout>
~~~

  In the xml file, we implement the following UIs:
  
1. Create a LinearLayout to show a TextView with "GSDemo" title and put it on the top.

2. Create two lines of Buttons: "LOCATE", "ADD", "CLEAR", "CONFIG", "PREPARE", "START" and "STOP", place them horizontally.

3. Lastly, we create a map view fragment and place it at the bottom.
  
Next, copy the "aircraft.png" and "ic_launcher.png" image files from this Github sample project to the **drawable** folders inside the **res** folder.
    
Furthermore, open the AndroidManifest.xml file and update the ".MainActivity" activity element with several attributes as shown below:
  
~~~xml
<activity
            android:name=".MainActivity"
            android:configChanges="orientation|screenSize"
            android:label="@string/title_activity_mainactivity"
            android:screenOrientation="landscape"
            android:theme="@android:style/Theme.NoTitleBar.Fullscreen" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
</activity>
~~~
   
  Moreover, open the "strings.xml" file in **values** folder and add the following string content:
  
~~~xml
    <string name="title_activity_demo">GSDemoActivity</string>
    <string name="google_app_id">Google_App_ID_String</string>
~~~
 
  Please enter your "Google APP ID" in the "Google_ App_ ID_String" value part.
  
  Now, if you check the "activity_main.xml" file, you should see the preview screenshot of MainActivity as shown below:
   
![MainActivity](./Images/mainActivity.png)

  Lastly, let's create a new xml file named "dialog_waypointsetting.xml" in the layout folder by right-clicking on the "layout" folder and select **New->XML->Layout XML File**. Then replace the code with the same file in Github Sample Project, since the content is too much, we don't show them all here.

This xml file will help to setup a textView to enter "Altitude" and create three RadioButton Groups for selecting **Speed**, **Action After Finished** and **Heading**.

  Now, if you check the dialog_waypointsetting.xml file, you can see the preview screenshot of Waypoint Configuration Dialog as shown below:
   
![MainActivity](./Images/waypointConfig.png)

##### Working on the MainActivity Class

Let's come back to the MainActivity.java class, and replace the code with the following, remember to import the related classes as Android Studio suggested:

~~~java
public class MainActivity extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback {

    protected static final String TAG = "MainActivity";
    private GoogleMap gMap;
    private Button locate, add, clear;
    private Button config, prepare, start, stop;

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /**
     * @Description : RETURN BTN RESPONSE FUNCTION
     */
    public void onReturn(View view){
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void initUI() {
        locate = (Button) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        config = (Button) findViewById(R.id.config);
        prepare = (Button) findViewById(R.id.prepare);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        config.setOnClickListener(this);
        prepare.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
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
        
        setContentView(R.layout.activity_main);
        initUI();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                // TODO Auto-generated method stub
                Log.d(TAG, "Select Speed finish");
            }
        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d(TAG, "Select action action");
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d(TAG, "Select heading finish");
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
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
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.config:{
                showSettingDialog();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO Auto-generated method stub
        // Initializing Amap object
        if (gMap == null) {
            gMap = googleMap;
            setUpMap();
        }

        LatLng Shenzhen = new LatLng(22.5500, 114.1000);
        gMap.addMarker(new MarkerOptions().position(Shenzhen).title("Marker in Shenzhen"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(Shenzhen));
    }

    private void setUpMap() {
        gMap.setOnMapClickListener(this);// add the listener for click for amap object
    }

    @Override
    public void onMapClick(LatLng point) {
    }
}
~~~

In the code shown above, we implement the following features:

**1.** Create a GoogleMap variable and 7 Button member variables for the UI. Then create the `initUI()` method to init the 7 Button variables and implement their `setOnClickListener` method and pass "this" as parameter.

**2.** In the `onCreate()` method, we request several permissions at runtime to ensure the SDK works well when the compile and target SDK version is higher than 22(Like Android Marshmallow 6.0 device and API 23).

**3.** Then invoke `initUI()` method and create "SupportMapFragment" variable to call the `OnMapReady()` method asynchronously.

**4.** Implement the `showSettingDialog` method to show the **Waypoint Configuration** alert dialog and override the `onClick()` method to show the configuration dialog when press the **Config** button.

**5.** Lastly, we override the `onMapReady()` method to initialize the `gMap` and invoke `setUpMap()` method to implement the `setOnMapClickListener()` method of `gMap` variable. Then add a marker of Shenzhen, China here for example. So when the Google map is loaded, you will see a red pin tag on Shenzhen, China.

For more details, please check the Github Sample Project. If you are not familiar with adding a Google map to your application, please follow this guide <https://developers.google.com/maps/documentation/android-api/utility/setup#library-dependency>. 

We have gone through a long process to setup the UI of the application. Now, let's build and run the project and install it in your Android device to test it. Here we use Nexus 5 for testing. If everything goes well, you should see the following gif animation of the application:

![p4MissionsUIDemo](./Images/GSDemoAni.gif)

### Registering your Application

#### 1. Modifying AndroidManifest file

After you finish the above steps, let's register our application with the **App Key** you apply from DJI Developer Website. If you are not familiar with the App Key, please check [Creating an DJI App Tutorial](http://developer.dji.com/mobile-sdk/get-started/Register-Download).

**1.** Let's open the AndroidManifest.xml file and add the following elements to it:

~~~xml
    <uses-feature
        android:name="android.hardware.usb.host"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.usb.accessory"
        android:required="true" />
~~~

Here, because not all Android-powered devices are guaranteed to support the USB accessory and host APIs, include two <uses-feature> elements that declares that your application uses the "android.hardware.usb.accessory" and "android.hardware.usb.host" feature.

Then add the following elements above the **MainActivity** activity element:

~~~xml
    <!-- DJI SDK -->

    <uses-library android:name="com.android.future.usb.accessory" />
    <meta-data
        android:name="com.dji.sdk.API_KEY"
        android:value="Please enter your App Key here." />
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY" />
     <meta-data
     android:name="com.google.android.gms.version"
     android:value="@integer/google_play_services_version" />

    <activity
        android:name="dji.sdk.SDKManager.DJIAoaControllerActivity"
        android:theme="@android:style/Theme.Translucent" >
        <intent-filter>
            <action android:name="android.hardware.usb.action.USB_ACCESSORY_ATTACHED" />
        </intent-filter>
        
        <meta-data
          android:name="android.hardware.usb.action.USB_ACCESSORY_ATTACHED"
            android:resource="@xml/accessory_filter" />
    </activity>
    <service android:name="dji.sdk.SDKManager.DJIGlobalService" >
    </service>

    <!-- DJI SDK -->
~~~

In the code above, we enter the **App Key** of the application in the value part of `android:name="com.dji.sdk.API_KEY"` attribute. For more details of the AndroidManifest.xml file, please check the Github source code of the demo project.

#### 2. Implementing DJIDemoApplication Class

After you finish the steps above, open the DJIDemoApplication.java file and replace the code with the same file in the Github Source Code, here we explain the important parts of it:

~~~java
@Override
public void onCreate() {
    super.onCreate();
    mHandler = new Handler(Looper.getMainLooper());
    DJISDKManager.getInstance().initSDKManager(this, mDJISDKManagerCallback);
}
    
private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {
    @Override
    public void onGetRegisteredResult(DJIError error) {
        Log.d(TAG, error == null ? "Success" : error.getDescription());
        if(error == DJISDKError.REGISTRATION_SUCCESS) {
            DJISDKManager.getInstance().startConnectionToProduct();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "register sdk fails, check network is available", Toast.LENGTH_LONG).show();
                }
            });
        }
        Log.e("TAG", error.toString());
    }

    @Override
    public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {
        mProduct = newProduct;
        if(mProduct != null) {
            mProduct.setDJIBaseProductListener(mDJIBaseProductListener);
        }
        notifyStatusChange();
    }
};
~~~

  Here, we implement several features:
  
1. We override the `onCreate()` method to initialize the DJISDKManager.
2. Implement the two interface methods of DJISDKManagerCallback. You can use the `onGetRegisteredResult()` method to check the Application registration status and show text message here. Using the `onProductChanged()` method, we can check the product connection status and invoke the `notifyStatusChange()` method to notify status changes.

Now let's build and run the project and install it to your Android device. If everything goes well, you should see the "success" textView like the following screenshot when you register the app successfully.

![registerSuccess](./Images/registerSuccess.png)

> **Important:** Please check if the "armeabi-v7a", "arm64-v8a" and "x86" lib folders has been added to your jnLibs folder in **dJISDKLib** successfully before testing resgistering the app. 
> 
> ![armeabi](./Images/armeabi.png)

## Implementing the Waypoint Mission

### Locating Aircraft on Google Map

Before we implementing the waypoint mission feature, we should show the aircraft's location on Google Map and try to zoom in automatically to view the surrounding area of the aircraft.

Let's open MainActivity.java file and declare the following variables first:

~~~java
private double droneLocationLat = 181, droneLocationLng = 181;
private Marker droneMarker = null;
private DJIFlightController mFlightController;
~~~

Then, since we need to detect the product connection status, we should register a BroadcastReceiver in the `onCreate()` method and override the `onReceive()` method of it as shown below:

~~~java

@Override
protected void onDestroy(){
    super.onDestroy();
    unregisterReceiver(mReceiver);
}

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        initUI();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    
protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };
~~~

The `onReceive()` method will be invoked when the DJI Product connection status change, we can us it to update our aircraft's location.

Next, let's implement the `initFlightController()` method and invoke it inside the `onProductConnectionChange()` method:

~~~java
private void onProductConnectionChange()
{
    initFlightController();
}

 private void initFlightController() {

        DJIBaseProduct product = DJIDemoApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof DJIAircraft) {
                mFlightController = ((DJIAircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setUpdateSystemStateCallback(new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {

                @Override
                public void onResult(DJIFlightControllerDataType.DJIFlightControllerCurrentState state) {
                    droneLocationLat = state.getAircraftLocation().getLatitude();
                    droneLocationLng = state.getAircraftLocation().getLongitude();
                    updateDroneLocation();

                }
            });
        }
    }
~~~

In the code above, we firstly check the product connection status with the help of `isConnected()` method of DJIBaseProduct. Then initialize `mFlightController` variable and override the `onResult()` method to invoke `updateDroneLocation` method. By using the `onResult()` method, you can get the flight controller current state from the parameter.

Furthermore, let's implement the `updateDroneLocation()` method and invoke it in `onClick()` method's locate button click action:

~~~java
public static boolean checkGpsCoordinates(double latitude, double longitude) {
    return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
}
    
private void updateDroneLocation(){

    LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
    //Create MarkerOptions object
    final MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(pos);
    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if (droneMarker != null) {
                droneMarker.remove();
            }

            if (checkGpsCoordinates(droneLocationLat, droneLocationLng)) {
                droneMarker = gMap.addMarker(markerOptions);
            }
        }
    });
}

@Override
public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
        case R.id.locate:{
            updateDroneLocation();
            cameraUpdate();
            break;
        }
        case R.id.config:{
            showSettingDialog();
            break;
        }
        default:
            break;
    }
}
~~~

In the `updateDroneLocation()` method, we add the drone location marker on Google map.

Finally, let's implement the `camearUpdate()` method to move camera and zoom in Google Map to the drone's location:

~~~java
private void cameraUpdate(){
    LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
    float zoomlevel = (float) 18.0;
    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
    gMap.moveCamera(cu);
}
~~~

Now, let's connect the aircraft to your PC or Virtual Machine running Windows via a Micro USB cable, and then power on the aircraft and the remote controller. Click Display Simulator. You can type in your current location's latitude and longitude data in the Simulator Config, if you would like. 

![simulatorPreview](./Images/simulator_preview.png)

Next, build and run the project and install it in your Android device and connect it to the remote controller using USB cable. Go to the DJI PC Simulator on your PC and press the Start Simulation button. If you check the application now, a tiny red aircraft will be shown on the map. If you cannot find the aircraft, press the "LOCATE" button to zoom in to the center of the aircraft on the Map. Here is a gif animation for you to check:

![locateAircraft](./Images/locateAircraft.gif)

### Adding Waypoint Markers

Since you can see the aircraft clearly on the Google map now, you can add `Marker` on the map to show the waypoints of the Waypoint Mission. Let's continue to declare the `mMarkers` variable first:

~~~java
private boolean isAdd = false;
private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
~~~

Then, implement the `onMapClick()` and `markWaypoint()` methods as shown below:

~~~java
private void setResultToToast(final String string){
    MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    });
}
    
@Override
public void onMapClick(LatLng point) {
    if (isAdd == true){
        markWaypoint(point);       
    }else{
        setResultToToast("Cannot add waypoint");
    }
}
    
private void markWaypoint(LatLng point){
    //Create MarkerOptions object
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(point);
    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
    Marker marker = gMap.addMarker(markerOptions);
    mMarkers.put(mMarkers.size(), marker);
}
~~~

Here, the `onMapClick()` method will be invoked when user tap on the Map View. When user tap on different position of the Map View, we will create a `MarkerOptions` object and assign the "LatLng" object to it, then invoke "gMap"'s `addMarker()` method by passing the markerOptions parameter to add the waypoint markers on the Google map.

Finally, let's implement the `onClick()` and `enableDisableAdd()` methods to implement the **ADD** and **CLEAR** actions as shown below:

~~~java
 @Override
 public void onClick(View v) {
    switch (v.getId()) {
        case R.id.add:{
            enableDisableAdd();
            break;
        }
        case R.id.clear:{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gMap.clear();
                }
            });
            break;
        }
        case R.id.config:{
            showSettingDialog();
            break;
        }
        default:
            break;
    }
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
~~~

Now, let's try to build and run your application on an Android device and try to add waypoints on the Google map. If everything goes well, you should see the following gif animation:

![addWaypointsAni](./Images/addWaypoints.gif)

### Implementing Waypoint Missions

#### Configurating Waypoint Mission

Before we prepare a Waypoint Mission, we should provide a way for user to configure it, like setting the flying altitude, speed, heading, etc. So let's declare several variables as shown below firstly:

~~~java
private float altitude = 100.0f;
private float mSpeed = 10.0f;
private DJIWaypointMission.DJIWaypointMissionFinishedAction mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.NoAction;
private DJIWaypointMission.DJIWaypointMissionHeadingMode mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;

private DJIWaypointMission mWaypointMission;
private DJIMissionManager mMissionManager;
~~~

Here we declare the `altitude`, `mSpeed`, `mFinishedAction` and `mHeadingMode` variable and intialize them with default value. Also, we declare the DJIWaypointMission and DJIMissionManager objects for setting up missions.

Next, replace the code of `showSettingDialog()` method with the followings:

~~~java
private void showSettingDialog(){
    LinearLayout wayPointSettings = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

    final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
    RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
    RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
    RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);

    speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            Log.d(TAG, "Select finish action");
            if (checkedId == R.id.finishNone){
                mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.NoAction;
            } else if (checkedId == R.id.finishGoHome){
                mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome;
            } else if (checkedId == R.id.finishAutoLanding){
                mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.AutoLand;
            } else if (checkedId == R.id.finishToFirst){
                mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoFirstWaypoint;
            }
        }
    });

    heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d(TAG, "Select heading");
            if (checkedId == R.id.headingNext) {
                mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;
            } else if (checkedId == R.id.headingInitDirec) {
                mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.UsingInitialDirection;
            } else if (checkedId == R.id.headingRC) {
                mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.ControlByRemoteController;
            } else if (checkedId == R.id.headingWP) {
                mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.UsingWaypointHeading;
            }
        }
    });

    new AlertDialog.Builder(this)
            .setTitle("")
            .setView(wayPointSettings)
            .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id) {
                    String altitudeString = wpAltitude_TV.getText().toString();
                    altitude = Integer.parseInt(nulltoIntegerDefault(altitudeString));
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

String nulltoIntegerDefault(String value){
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
~~~ 

Here, we implement the `setOnCheckedChangeListener()` method of "RadioGroup" class and pass different values to the `mSpeed`, `mFinishedAction` and `mHeadingMode` variables based on the item user select. 

For the finished action of DJIWaypointMission, we provide several enum values here:

- **AutoLand**

   The aircraft will land automatically at the last waypoint. 

- **ContinuewUntilEnd**

  If the user attempts to pull the aircraft back along the flight path as the mission is being executed, the aircarft will move towards the previous waypoint and will continue to do so until there are no more waypoint to move back to or the user has stopped attempting to move the aircraft back. 
  
- **GoFirstWaypoint**

  The aircraft will go back to its first waypoint and hover in position. 
  
- **GoHome**

  The aicraft will go home when the mission is complete. 

- **NoAction**

  No further action will be taken on completion of mission. 
  
For the heading mode of DJIWaypointMission, we provide these enum values here:

- **Auto**

  Aircraft's heading will always be in the direction of flight. 
  
- **ControlByRemoteController**

  Aircraft's heading will be controlled by the remote controller. 
  
- **TowardPointOfInterest**

  Aircraft's heading will always toward point of interest. 
  
- **UsingInitialDirection**

  Aircraft's heading will be set to the initial take-off heading. 
  
- **UsingWaypointHeading**

Aircraft's heading will be set to the previous waypoint's heading while travelling between waypoints. 
  
Now, let's continue to implement the `configWayPointMission()` method as shown below:
  
~~~java
private void configWayPointMission(){

    if (mWaypointMission != null){
        mWaypointMission.finishedAction = mFinishedAction;
        mWaypointMission.headingMode = mHeadingMode;
        mWaypointMission.autoFlightSpeed = mSpeed;

        if (mWaypointMission.waypointsList.size() > 0){
            for (int i=0; i< mWaypointMission.waypointsList.size(); i++){
                mWaypointMission.getWaypointAtIndex(i).altitude = altitude;
            }
            setResultToToast("Set Waypoint altitude success");
        }
   }
}
~~~

  In the code above, we check if `mWaypointMission` is null and set its `finishedAction`, `headingMode` and `autoFlightSpeed` variables of DJIWaypointMission.   Then we use a for loop to set the DJIWaypoint's altitude of DJIWaypointMission's waypointsList. 
    
#### Prepare Waypoint Mission

  Now, let's initialize the `mMissionManager` and `mWaypointMission` variables by implementing the `initMissionManager()` method as shown below:

~~~java
private void initMissionManager() {
    DJIBaseProduct product = DJIDemoApplication.getProductInstance();

    if (product == null || !product.isConnected()) {
        setResultToToast("Product Not Connected");
        mMissionManager = null;
        return;
    } else {

        setResultToToast("Product Connected");
        mMissionManager = product.getMissionManager();
        mMissionManager.setMissionProgressStatusCallback(this);
        mMissionManager.setMissionExecutionFinishedCallback(this);
    }

    mWaypointMission = new DJIWaypointMission();
}

@Override
public void missionProgressStatus(DJIMission.DJIMissionProgressStatus progressStatus) {

}

@Override
public void onResult(DJIError error) {
    setResultToToast("Execution finished: " + (error == null ? "Success" : error.getDescription()));

}
~~~

Here, we check the product connection status first and invoke DJIBaseProduct's `getMissionManager()` method to initialize `mMissionmanager` variable. Next, invoke the `setMissionProgressStatusCallback()` and `setMissionExecutionFinishedCallback()` methods of DJIMissionManager and implement the two callback methods of DJIMissionManager. We should also implement the `DJIMissionManager.MissionProgressStatusCallback` and `DJIBaseComponent.DJICompletionCallback` interfaces for the MainActivity class on top.

We can get the mission execution status from the `missionProgressStatus()` callback, and check the mission execution result from the `onResult()` callback method.

Moreover, we should invoke the `initMissionManager()` method in the following two methods:

~~~java
@Override
protected void onResume(){
    super.onResume();
    initFlightController();
    initMissionManager();
}

private void onProductConnectionChange()
{
    initFlightController();
    initMissionManager();
}
~~~

When user resume the application and the product connection change, we should both call the `iniMissionManager()` to do initialization work.

Furthermore, let's implement the prepare mission action and addWaypoint action of DJIWaypointMission as shown below:

~~~java
@Override
public void onMapClick(LatLng point) {
    if (isAdd){
        markWaypoint(point);
        DJIWaypoint mWaypoint = new DJIWaypoint(point.latitude, point.longitude, altitude);
        //Add waypoints to Waypoint arraylist;
        if (mWaypointMission != null) {
            mWaypointMission.addWaypoint(mWaypoint);
            setResultToToast("AddWaypoint");
        }
    }else{
        setResultToToast("Cannot add waypoint");
    }
}

private void prepareWayPointMission(){

    if (mMissionManager != null && mWaypointMission != null) {

        DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler() {
            @Override
            public void onProgress(DJIMission.DJIProgressType type, float progress) {
            }
        };

        mMissionManager.prepareMission(mWaypointMission, progressHandler, new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast(error == null ? "Success" : error.getDescription());
            }
        });
    }
}   
~~~

Actually, we can get the mission preparation progress by overriding the `onProgress()` method of DJIMissionProgressHandler. Lastly, let's add the `R.id.prepare` case checking in the `onClick()` method:

~~~java
case R.id.prepare:{
    prepareWayPointMission();
    break;
}
~~~

#### Start and Stop Mission
  
Once the mission finish preparation, we can invoke the `startMissionExecution()` and `stopMissionExecution()` methods of DJIMissionManager to implement the start and stop mission feature as shown below:

~~~java
private void startWaypointMission(){

    if (mMissionManager != null) {
        mMissionManager.startMissionExecution(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Start: " + (error == null ? "Success" : error.getDescription()));
            }
        });
    }
}

private void stopWaypointMission(){

    if (mMissionManager != null) {
        mMissionManager.stopMissionExecution(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Stop: " + (error == null ? "Success" : error.getDescription()));
            }
        });

        if (mWaypointMission != null){
            mWaypointMission.removeAllWaypoints();
        }
    }
}
~~~

Lastly, let's improve the `onClick()` method to improve the **clear** button action and implement the **start** and **stop** button actions:

~~~java
@Override
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.locate:{
            updateDroneLocation();
            cameraUpdate(); // Locate the drone's place
            break;
        }
        case R.id.add:{
            enableDisableAdd();
            break;
        }
        case R.id.clear:{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gMap.clear();
                }
            });
            if (mWaypointMission != null){
                mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
            }
            break;
        }
        case R.id.config:{
            showSettingDialog();
            break;
        }
        case R.id.prepare:{
            prepareWayPointMission();
            break;
        }
        case R.id.start:{
            startWaypointMission();
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
~~~

## Test Waypoint Mission with DJI Simulator  

You've come a long way in this tutorial, and it's time to test the whole application.

**Important**: Make sure the battery level of your aircraft is more than 10%, otherwise the waypoint mission may fail!

Build and run the project to install the application into your android device. After that, please connect the aircraft to your PC or Virtual Machine running Windows via a Micro USB cable. Then, power on the remote controller and the aircraft, in that order.

Next, press the **Display Simulator** button in the DJI PC Simulator and feel free to type in your current location's latitude and longitude data into the simulator.

![simulatorPreview](./Images/simulator_preview.png)

Then connect your android device to the remote controller using USB cable and run the application. Go back to the DJI PC Simulator on your PC and press the **Start Simulation** button. A tiny red aircraft will appear on the map in your application,  if you press the **LOCATE** button, the map view will zoom in to the region you are in and will center the aircraft:

![locateAircraft](./Images/locateAircraft.gif)

Next, press the **Add** button and tap on the Map where you want to add waypoints, as shown below:

![addWaypointsAni](./Images/addWaypointsAni.gif)

Once you press the **CONFIG** button, the **Waypoint Configuration** dialog will appear. Modify the settings as you want and press **Finish** button. Then press the **PREPARE** button to prepare the mission.

If prepare mission failed, you may see the following screenshot:

![prepareMissionFail](./Images/prepareMissionFail.png)

**Important**: To fix this problem, please switch the Remote Controller's mode selection to the **F** position and press **PREPARE** button to try again. If the mode selection bar is in the F position when the autopilot is powered on, the user must toggle back and forth between **F** and another position and then try again.

You are required to be in the **F** position when using the DJIWaypoint Mission of DJI Mobile SDK.

![switchFlightMode](./Images/switchFlightModes.png)

If prepare mission success, press the **START** button to start the waypoint mission execution.
  
![prepareMission](./Images/prepareMission.gif)  
  
Now you will should see the aircraft move towards the waypoints you set previously on the map view, as shown below:

![startMission](./Images/startMission.gif)

At the same time, you are able to see the Inspire 1 take off and start to fly in the DJI PC Simulator.

![flyingInSimulator](./Images/takeOff.gif)

When the waypoint mission finishes, an "Execution finished: Success!" message will appear and the Inspire 1 will start to go home!

Also, the remote controller will start beeping, and the **Go Home** button on the remote controller will start to flash a white light. Let's take a look at the DJI PC Simulator now:

![landing](./Images/landing.gif)

The inspire 1 will eventually go home, land, and the beeping from the remote controller will stop. The application will go back to its normal status. If you press the **CLEAR** button, all the waypoints you previously set will be cleared. During the mission, if you'd ever like to stop the DJIWaypoint mission, you can do so by pressing the **STOP** button.
  
## Summary

 In this tutorial, you’ve learned how to setup and use the DJI PC Simulator to test your waypoint mission application, upgrade your aircraft's firmware to the developer version, use the DJI Mobile SDK to create a simple map view, modify annotations of the map view, show the aircraft on the map view by using GPS data from the DJI PC Simulator. Next, you learned how to configure **DJIWaypoint** parameters, how to add waypoints to **DJIWaypointMission**. Moreover, you learned how to use DJIMissionManager to **prepare**, **start** and **stop** missions. 
      
   Congratulations! Now that you've finished the demo project, you can build on what you've learned and start to build your own waypoint mission application. You can improve the method which waypoints are added(such as drawing a line on the map and generating waypoints automatically), play around with the properties of a waypoint (such as heading, etc.), and adding more functionality. In order to make a cool waypoint mission application, you still have a long way to go. Good luck and hope you enjoy this tutorial!


