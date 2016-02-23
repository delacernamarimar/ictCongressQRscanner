package com.example.delacerna.ict_congress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by DelaCerna on 1/24/2016.
 */
public class CheckList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int counter;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<String> al= new ArrayList<String>();
    CheckQR cq= new CheckQR();
    String file = "mydata";
    String data;
    BufferedReader br = null;
    String response = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklistin);
        String[] items = new String[0];
        //ArrayAdapter
        lv= (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    items = stringBuilder.append(receiveString).toString().toString().split("\\,");
                }
                for(int i = 0; i<items.length;i++){
                    adapter.add(items[i]);
                }
                inputStream.close();
                adapter.notifyDataSetChanged();
                //ret = stringBuilder.toString();
                //adapter.add(ret);
            }
        }
        catch (Exception e) {
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckList.this);
            alertDialogBuilder.setTitle("CAUTION");
            alertDialogBuilder
                    .setMessage("Want to delete "+MainActivity.counterList+"?")
                    .setCancelable(false)
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                                InputStream inputStream = openFileInput("config.txt");
                                outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_APPEND));
                                inputStream = openFileInput("config.txt");
                                MainActivity.counterList=0;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            CheckList.this.finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
