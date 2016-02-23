package com.example.delacerna.ict_congress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

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
 * Created by DelaCerna on 1/25/2016.
 */
public class CheckQR extends Activity {
    String file = "mydata";
    String data;
    static final int SCANNER_REQUEST_CODE = 123 ;
    private BufferedReader reader;
    private StringBuilder stringBuilder, tok, tok2;
    private  String result;
    private JSONArray student;

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("QR_CODE", "QR_CODE");
        startActivityForResult(intent, SCANNER_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent
            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                setContents(intent.getStringExtra("SCAN_RESULT"));
                String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
                String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");

                URL url = null;

                    String ticket = getContents().substring(44, 50);
                    Toast.makeText(CheckQR.this, ticket, Toast.LENGTH_SHORT).show();
                    try {
                        // create the HttpURLConnection
                        url = new URL("http://"+MainActivity.ipaddress+"/ictComplete/");
                        Toast.makeText(CheckQR.this, "Checking", Toast.LENGTH_SHORT).show();
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
                                qr = c.getString("ticketNo");

                                counter++;
                                if(ticket.equals(qr)) {
                                    counter=0;
                                    id = c.getString("idno");;
                                    lname = c.getString("familyName");
                                    fname = c.getString("firstName");
                                    campus = c.getString("campus");
                                    course = c.getString("course");
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckQR.this);
                                    alertDialogBuilder.setTitle("Student Information!");
                                    alertDialogBuilder
                                            .setMessage("Ticket #:"+qr +"\nID: " + id + "\nName: " + lname + ", " + fname + "\nCourse: " + course + "\nCampus: " + campus)
                                                    //.setMessage("Ticket #:"+qr)
                                            .setCancelable(false)
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                                    intent.putExtra("QR_CODE", "QR_CODE");
                                                    startActivityForResult(intent, SCANNER_REQUEST_CODE);
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckQR.this);
                                alertDialogBuilder.setTitle("Error!");
                                alertDialogBuilder
                                        .setMessage("ID not found!")
                                                //.setMessage("Ticket #:"+qr)
                                        .setCancelable(false)
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                                intent.putExtra("QR_CODE", "QR_CODE");
                                                startActivityForResult(intent, SCANNER_REQUEST_CODE);
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }catch(Exception e){}

                    } catch (Exception e) {
                    }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                super.onBackPressed();
                Toast.makeText(this,"Check cancelled!",Toast.LENGTH_LONG).show();
                 intent = new Intent(this, CheckId.class);
                startActivity(intent);
            }
        } else {
            // Handle other intents
        }

    }

}
