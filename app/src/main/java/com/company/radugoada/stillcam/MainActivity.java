package com.company.radugoada.stillcam;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


public class MainActivity extends AppCompatActivity {

    Camera camera; //initializing Camera Hardware with given variable
    FrameLayout frameLayout;
    ShowCamera showCamera;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);

        //Open the Rear Camera
        camera = Camera.open(); //camera object
        showCamera = new ShowCamera(this, camera); //now the class from ShowCamera will be initiated
        frameLayout.addView(showCamera);

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

                }catch (IOException e) //solve the error exception
                {
                    e.printStackTrace();
                }
            }
        }
    };

    //new private method for the output image file
    private File getOuputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))
        {
            return null;
        }
        else{
            //creates new file into directory that we accessed from the device storage
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");

            if(!folder_gui.exists())
            {
                folder_gui.mkdirs();
            }

            File outputFile = new File(folder_gui, "temp.jpg"); //setting name and extension for output image file
            return outputFile;
        }
    }

    public void captureImage(View v) //capture image public method
    {

        if(camera != null) //we need to set the condition first
        {
            camera.takePicture(null, null, mPictureCallback); //when ever we click on the Snap button, the takePicture method will be called
        }
    }
}
