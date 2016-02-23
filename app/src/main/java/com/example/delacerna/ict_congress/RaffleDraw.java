package com.example.delacerna.ict_congress;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DelaCerna on 2/11/2016.
 */
public class RaffleDraw extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    TextView randomNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raffle_drawin);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(RaffleDraw.this);


        randomNum=(TextView) findViewById(R.id.textView1);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(
                new ShakeDetector.OnShakeListener() {
                    @Override
                    public void onShake() {
                        // Do stuff!
                        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        long pattern[]={0,800,200,1200,300,2000};
                        v.vibrate(pattern,-1);

                        int random = (int)(Math.random() * (6));
                        //temporary data
                        String[] codeNum={"cac42d","db12a4","c2d15s","e21da5","dgh21w","a82s24"};
                        String lucky=codeNum[random];
                        Toast.makeText(RaffleDraw.this, "the lucky number is " + lucky, Toast.LENGTH_SHORT).show();
                        randomNum.setText(lucky);
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.qrscanner) {
            // Handle the camera action
            Intent intent = new Intent(this, CheckQR.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, CheckId.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, CheckList.class);
            startActivity(intent);
        } else if (id == R.id.nav_raffle) {
            Intent intent = new Intent(this, RaffleDraw.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}