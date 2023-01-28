package com.dji.GSDemo.GoogleMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

import dji.common.camera.SystemState;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

public class MainActivity extends AppCompatActivity {

    private View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.btn_waypoint1:
                startActivity(MainActivity.this, Waypoint1Activity.class);
                break;
            case R.id.btn_waypoint2:
                startActivity(MainActivity.this, Waypoint2Activity.class);
                break;
            case R.id.btn_maestro:
               TextView mText = (TextView)findViewById(R.id.textConnectedStatus);
               mText.setText("Listening...");
               Button mButton = (Button)findViewById(R.id.btn_waypoint1);
               mButton.setEnabled(true);
               BaseProduct product = DJIDemoApplication.getProductInstance();
               FlightController flightController = ((Aircraft) product).getFlightController();
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_waypoint1).setOnClickListener(clickListener);
        findViewById(R.id.btn_waypoint2).setOnClickListener(clickListener);
        findViewById(R.id.btn_maestro).setOnClickListener(clickListener);

        Button mButton = (Button)findViewById(R.id.btn_waypoint1);
        mButton.setEnabled(false);
        mButton = (Button) findViewById(R.id.btn_waypoint2);
        mButton.setEnabled(false);
        mButton.setVisibility(View.GONE);

    }

    public static void startActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
