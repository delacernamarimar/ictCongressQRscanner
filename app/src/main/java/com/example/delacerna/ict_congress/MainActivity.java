package com.example.delacerna.ict_congress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btnLog;
    EditText  editIP;
    static String ipaddress;
    static int counterList;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        editIP= (EditText) findViewById(R.id.editText5);
        btnLog= (Button) findViewById(R.id.button3);
        btnLog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // editIP.setText("192.168.1.3");
                        ipaddress = editIP.getText().toString();
                        //ip from router
                        if(ipaddress.equals("192.168.1.3")) {
                            Toast.makeText(MainActivity.this, "You are now logged!", Toast.LENGTH_SHORT).show();
                            editIP.setVisibility(View.GONE);
                            btnLog.setVisibility(View.GONE);
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "IP is not correct!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

    public void readwriteFile(String idNo){

        if (counterList==0) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(idNo + ",");
                outputStreamWriter.close();
                counterList++;
            }catch(Exception e){}
        } else {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_APPEND));
                InputStream inputStream = openFileInput("config.txt");

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        items = stringBuilder.append(receiveString).toString().toString().split("\\,");
                    }
                    int count = 0;
                    for (int i = 0; i < items.length; i++) {
                        if (items[i].equals(idNo)) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        MainActivity.counterList++;
                        outputStreamWriter.write(idNo + ",");
                        outputStreamWriter.close();
                    }
                    Toast.makeText(MainActivity.this, "Write.", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){}
        }
    }

}
