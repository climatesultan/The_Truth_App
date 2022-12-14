package com.gokulrajendran.thetruth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class DemoActivity extends AppCompatActivity {

    Button trackSer, addCilent, myArea;
    LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        getSupportActionBar().hide();

        trackSer = findViewById(R.id.trackServiceBtn);
        addCilent = findViewById(R.id.addClientBtn);
        myArea = findViewById(R.id.myAreaBtn);
        lin = findViewById(R.id.lin1);


        trackSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoActivity.this, TrackServiceDemo.class));
            }
        });

        addCilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoActivity.this, AddClientDemo.class));
            }
        });

        myArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoActivity.this, MyAreaDemo.class));
            }
        });

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DemoActivity.this, ProfileActivity.class));
            }
        });
    }
}