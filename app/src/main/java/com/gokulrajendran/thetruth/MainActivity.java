package com.gokulrajendran.thetruth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Button Demo, play, screenshot, scanText;
    private FirebaseVisionImage firebaseEye;
    private TextView showText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("Beta v1.0");
        getSupportActionBar().setTitle("The Truth App");
        // Buttons
        Demo = findViewById(R.id.DemoBtn);
        play = findViewById(R.id.PlayBtn);
        screenshot = findViewById(R.id.show_screenshot_btn);
        scanText = findViewById(R.id.textRecoBTN);
        showText = findViewById(R.id.showExtractText);


        byte[] byteArray = getIntent().getByteArrayExtra("image");
        String demo = getIntent().getStringExtra("DEMO");
        if (byteArray != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            doTheScreenshot(bmp);
        }
        if (demo != null){
            Dialog builder = new Dialog(MainActivity.this);
            builder.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });

            ZoomageView imageView = new ZoomageView(MainActivity.this);
            imageView.setImageResource(R.drawable.yoonusscreenshot);
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            builder.setCanceledOnTouchOutside(true);
            builder.show();
        }

        Demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DemoActivity.class));
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOverlayPermission();
                startService();
                Window window = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    window = new Window(MainActivity.this);
                }
                window.open();

                // Create the new Intent using the 'Send' action.
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hi! Please give Permission to track the Chat."); // set uri
                share.setPackage("com.instagram.android");
                startActivity(share);

            }
        });
        //
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(MainActivity.this);
                builder.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                ZoomageView imageView = new ZoomageView(MainActivity.this);
                imageView.setImageResource(R.drawable.yoonusscreenshot);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

                builder.setCanceledOnTouchOutside(true);
                builder.show();

            }
        });
        //
        scanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Machine Learning.. Scanning");
                progressDialog.show();

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.yoonusscreenshot);
                firebaseEye = FirebaseVisionImage.fromBitmap(largeIcon);
                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                Task<FirebaseVisionText> result =
                        detector.processImage(firebaseEye)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        // Task completed successfully
                                        //.
                                        if (progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                        showText.setText(firebaseVisionText.getText().trim());
                                        Toast.makeText(MainActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                if (progressDialog.isShowing()){
                                                    progressDialog.dismiss();
                                                }
                                                showText.setText(e.getMessage().trim());
                                                Toast.makeText(MainActivity.this, "Went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        });
            }
        });
        //
    }

    private void doTheScreenshot (Bitmap ravi){

        Bitmap bmp = ravi;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseEye = FirebaseVisionImage.fromBitmap(bmp);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result = detector.processImage(firebaseEye)
            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    // Task completed successfully
                    //.
                    showText.setText(firebaseVisionText.getText().trim());
                    Toast.makeText(MainActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                            showText.setText(e.getMessage().trim());
                            Toast.makeText(MainActivity.this, "Went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
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

    // check for permission again when user grants it from
    // the device settings, and start the service
    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}