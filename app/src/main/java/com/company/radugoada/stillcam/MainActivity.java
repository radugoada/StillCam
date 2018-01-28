package com.company.radugoada.stillcam;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    Camera camera;
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
}
