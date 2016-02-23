package com.example.delacerna.ict_congress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Created by DelaCerna on 1/24/2016.
 */
public class CheckId extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText idnum, server1, server2;
    Button btncheck, btnset;
    private String[] items;
    int counter;
    private String USER_AGENT;
    String myUrl;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String res;
    BufferedReader reader = null;
    StringBuilder stringBuilder,tok,tok2;
    private String result;
    JSONArray student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkidin);
        idnum = (EditText) findViewById(R.id.editText);
        btncheck = (Button) findViewById(R.id.button);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        btncheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String idnumber = idnum.getText().toString();

                                    URL url = null;
                                    if(!idnumber.equals("")){
                                    try {
                                        // create the HttpURLConnection
                                        url = new URL("http://"+MainActivity.ipaddress+"/ictComplete/");
                                        Toast.makeText(CheckId.this, "Checking", Toast.LENGTH_SHORT).show();
                                        //url = new URL("http://localhost/abella/checker.php?student=sample,%20user");
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("GET");
                                        connection.setReadTimeout(4 * 1000);
                                        connection.connect();

                                        // read the output from the server
                                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"), 8);
                                        StringBuilder sb = new StringBuilder();
                                        String line = null;
                                        while ((line = reader.readLine()) != null)
                                        {
                                            sb.append(line + "\n");
                                        }
                                        result = sb.toString();
                                        //Toast.makeText(CheckId.this, result, Toast.LENGTH_SHORT).show();
                                        String  qr = "", lname = "", fname = "", id = "", campus = "" , course = "";
                                        try {

                                            JSONObject jo = new JSONObject(result);
                                            student = jo.getJSONArray("student");
                                            int counter=0;
                                            for (int i = 0; i < student.length(); i++) {
                                                JSONObject c = student.getJSONObject(i);
                                                id = c.getString("idno");
                                                counter++;
                                                if(idnumber.equals(id)) {
                                                    counter=0;
                                                    qr = c.getString("ticketNo");
                                                    lname = c.getString("familyName");
                                                    fname = c.getString("firstName");
                                                    campus = c.getString("campus");
                                                    course = c.getString("course");
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckId.this);
                                                    alertDialogBuilder.setTitle("Student Information!");
                                                    alertDialogBuilder
                                                            .setMessage("Ticket #:"+qr +"\nID: " + id + "\nName: " + lname + ", " + fname + "\nCourse: " + course + "\nCampus: " + campus)
                                                            //.setMessage("Ticket #:"+qr)
                                                                    .setCancelable(false)
                                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    idnum.setText("");
                                                                }
                                                            });
                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                    MainActivity m = new MainActivity();
                                                    m.readwriteFile(id);
                                                    break;
                                                }
                                            }
                                            if(counter!=0){
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckId.this);
                                                alertDialogBuilder.setTitle("Error!");
                                                alertDialogBuilder
                                                        .setMessage("ID not found!")
                                                                //.setMessage("Ticket #:"+qr)
                                                        .setCancelable(false)
                                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                idnum.setText("");
                                                            }
                                                        });
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();
                                            }
                                        }catch(Exception e){}

                                    } catch (Exception e) {
                                    }
                                    }
                            else{
                                        Toast.makeText(CheckId.this, "Input an ID number!", Toast.LENGTH_SHORT).show();
                                    }

                        }

                    }

        );

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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

                //setIP();
            Toast.makeText(CheckId.this, "There is no settings to set!", Toast.LENGTH_SHORT).show();
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
