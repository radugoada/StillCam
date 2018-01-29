package com.company.radugoada.stillcam;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by radugoada on 1/28/18.
 */

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { //added destructor
        camera.stopPreview(); //stops the camera preview
        camera.release();
    }

    @Override  //surfaceCreated method will be called in ShowCamera()
    public void surfaceCreated(SurfaceHolder holder) {
        //setting parameters for the Camera itself
        Camera.Parameters params = camera.getParameters();

        //change orientation of the device camera
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);

        }

        else{

            params.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(90);

        }

        camera.setParameters(params);
        try{

            camera.setPreviewDisplay(holder);
            camera.startPreview();

        }catch (IOException e)
        {
            e.printStackTrace();
        }



    }
}
