package com.gokulrajendran.thetruth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TrackServiceDemo extends AppCompatActivity {

    Spinner firstSpinner, twoSpinner;
    RecyclerView theRecycleView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_service_demo);

        firstSpinner = findViewById(R.id.spinner_tsDemo);
        twoSpinner = findViewById(R.id.spinner_name);
        btn = findViewById(R.id.startbtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Tracking Service");
        getSupportActionBar().setSubtitle("Select the Project");

        String[] items = new String[]{"Select Project","HIV Awareness Project", "Adherence Counseling",
                "PrEP Eligibility Review", "AIDs Testing Campaign", "Mental Health Counseling, Pre-Test Counseling"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        firstSpinner.setAdapter(adapter);
        firstSpinner.setSelection(0);

        String[] item = new String[]{"Gokul", "Yoonus", "Ram", "Aem Aem", "Rahmina", "Harry"};
        ArrayAdapter<String> adapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item);
        twoSpinner.setAdapter(adapte);
        twoSpinner.setPrompt("Select Client");



        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    twoSpinner.setVisibility(View.GONE);
                    return;
                } else {
                    twoSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                twoSpinner.setVisibility(View.GONE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(TrackServiceDemo.this).inflate(R.layout.layout_ask_permission, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(TrackServiceDemo.this);
                alert.setView(view1);


                alert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        View view2 = LayoutInflater.from(TrackServiceDemo.this).inflate(R.layout.socialmedia_icons, null);
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(TrackServiceDemo.this);
                        alert2.setView(view2);

                        ImageView what = view2.findViewById(R.id.whatsappicon);
                        ImageView fb = view2.findViewById(R.id.facebookico);

                        what.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(TrackServiceDemo.this, "Starting Services....", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TrackServiceDemo.this, MainActivity.class).putExtra("DEMO", "demo"));
                                finish();
                            }
                        });

                        fb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkOverlayPermission();
                                startService();
                                Window window = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    window = new Window(TrackServiceDemo.this);
                                }
                                window.open();

                                // Create the new Intent using the 'Send' action.
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_TEXT, "Hi! Please give Permission to track the Chat."); // set uri
                                share.setPackage("com.instagram.android");
                                startActivity(share);
                                finish();
                            }
                        });
                        alert2.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                                dialog.dismiss();
                                Toast.makeText(TrackServiceDemo.this, "Session Stopped!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alert2.show();

                    }
                });

                alert.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                        Toast.makeText(TrackServiceDemo.this, "Session Stopped!", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.show();

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // method for starting the service
    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // check if the user has already granted
            // the Draw over other apps permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // start the service based on the android version
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(new Intent(this, ForegroundService.class));
                    } else {
                        startService(new Intent(this, ForegroundService.class));
                    }
                }
            }
        }else{
            startService(new Intent(this, ForegroundService.class));
        }
    }


    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }

    }
}