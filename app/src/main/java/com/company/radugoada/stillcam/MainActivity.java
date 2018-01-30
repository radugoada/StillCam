package com.company.radugoada.stillcam;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/*
 MIT License

 Copyright (c) 2018 Radu Goadă

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 */


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    Camera camera; //create Camera Hardware variable with given variable
    Camera.Parameters parameters;
    FrameLayout frameLayout; //create variable Layout for camera view
    ShowCamera showCamera; //calling the Java class with showCamera given input variable
    ImageButton imageButton; //create flash on/off image button variable
    boolean isflash = false; //initialize boolean parameters for flashlight state ON/OFF
    boolean ison = false;

    private TextView yawText; //create objects for calling the sensor
    private Sensor mySensor;
    private SensorManager SManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);
        getSupportActionBar().hide();  //hides the app title bar

        //Create our Sensor manager
        SManager =(SensorManager)getSystemService(SENSOR_SERVICE);
        //Accelerometer sensor initialize
        mySensor = SManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Register Sensor Listener
        SManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        //Assign TextView to get Data from it
        yawText=(TextView)findViewById(R.id.yawText);

        //Open the Rear Camera
        camera = Camera.open(); //camera object
        showCamera = new ShowCamera(this, camera); //now the class from ShowCamera will be initiated
        frameLayout.addView(showCamera);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        yawText.setTextColor(Color.rgb(148, 198, 47));
        yawText.setText("Rotation: " + event.values + " degrees");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use currently
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) { //onPictureTaken method will be called and it will get the image from the Device Camera


            File picture_file = getOuputMediaFile(); //creating new variable to save the captured image

            if(picture_file == null) // adding statement condition
            {
                return;
            }
            else {

                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();
                    camera.startPreview();
                    Toast.makeText(MainActivity.this, "Saved to: "+picture_file, Toast.LENGTH_LONG).show(); //Display text where the final image is saved directory path

                }catch (IOException e) //solve the error exception
                {
                    e.printStackTrace();
                }
            }
        }
    };


    //created private object and method for the output image file
    private File getOuputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))
        {
            return null;
        }
        else {
            //creates new file directory into the device external SD card storage
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "StillCam Images");

            if (!folder_gui.exists()) {
                folder_gui.mkdirs();
            }

            File outputFile = new File(folder_gui, "stillImage.jpg"); //setting name and extension for output image file
            return outputFile;
        }
    }


/*    public void flashOn()
    {

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isflash)
                {
                    if(!ison)
                    {
                        imageButton.setImageResource(R.drawable.flashon);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        // camera.startPreview();
                        ison = true;
                    }
                    else
                    {
                        imageButton.setImageResource(R.drawable.flashoff);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        //  camera.stopPreview();
                        ison = false;

                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error: ");
                    builder.setMessage("Flashlight is not supported on your device!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

    }
    */


    public void captureImage(View v) //Capture Image from camera public method
    {
        //initialize image button variable for flashlight
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
        {
            parameters = camera.getParameters();
            isflash = true;
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isflash)
                {
                    if(!ison)
                    {
                        imageButton.setImageResource(R.drawable.flashon);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        ison = true;
                    }
                    else
                    {
                        imageButton.setImageResource(R.drawable.flashoff);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        ison = false;

                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error: ");
                    builder.setMessage("Flashlight is not supported on your device!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });


        if(camera != null) //we need to set the condition first
        {
            camera.takePicture(null, null, mPictureCallback); //when ever we click on the Snap button, the takePicture method will be called
        }

        //play Snap shutter sound
        final MediaPlayer snapSound = MediaPlayer.create(this, R.raw.camera_shutter);
        Button button = (Button)this.findViewById(R.id.button);
        snapSound.start();

    }

}
