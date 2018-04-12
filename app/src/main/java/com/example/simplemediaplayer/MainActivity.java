package com.example.simplemediaplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "SimpleMediaPlayer";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=999;

    private static final String MP4_FILE = "a1.mp4";
    private static final String VIDEO_PATH = System.getenv("EXTERNAL_STORAGE") + "/video/";
    String mediaPath =  VIDEO_PATH + MP4_FILE;

    private static boolean mpstarted=false;
    private static boolean haveafile=false;
    private static boolean havepermission=false;
    private SurfaceHolder holder1;
    private void appEnd(){
        Log.i(TAG, "appEnd");
        this.finish();
    }

    MediaPlayer mp = new MediaPlayer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        /* No window title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

		/* Set Full screen flag */
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* add FLAG_KEEP_SCREEN_ON */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /* Support Transparent color */
        //getWindow().setFormat(PixelFormat.TRANSPARENT);
        //getWindow().setFormat(PixelFormat.RGBX_8888);

        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        //mPreview.setSecure(true);

        if (new File(mediaPath).exists()) {
            Log.i(TAG, "A video file is exist");
            haveafile=true;
        } else {
            Log.i(TAG, "A video file is not exist");
            new AlertDialog.Builder(this)
                    .setMessage("Put \"" + MP4_FILE + "\" to " + VIDEO_PATH + ", and Execute again.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { appEnd(); }
                    }).show();
        }

        holder1 = mPreview.getHolder();
        holder1.addCallback(this);

        // Assume thisActivity is the current activity
        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setMessage("To use this app, need to get permission to read external storage")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { requestPermission();}
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermission();
            }
        } else {
            havepermission=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    havepermission=true;
                    mediaPlay(mp, mediaPath);
                } else {
                    Log.i(TAG, "not allowed");
                    appEnd();
                }
            }
        }
    }

    private void requestPermission(){
        Log.i(TAG, "requestPermission");
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void mediaPlay(MediaPlayer mp,String mediaPath) {
        if (mpstarted==false && havepermission==true && haveafile==true) {
            mpstarted=true;

            try {
                mp.setVolume((float) 0.5, (float) 0.5);
                mp.setLooping(true);
                mp.setDisplay(holder1);
                mp.setDataSource(mediaPath);
                mp.prepare();
                mp.start();
            } catch (IllegalArgumentException|IllegalStateException|IOException e) {
                e.printStackTrace();
                appEnd();
            }
        } else if (mpstarted==false) {
            Log.i(TAG, "Not confirmed permission");
        } else {
            Log.i(TAG, "MP is already started");
            try {
                mp.setVolume((float) 0.5, (float) 0.5);
                mp.setLooping(true);
                mp.setDisplay(holder1);
                mp.setDataSource(mediaPath);
                mp.prepare();
                mp.start();
            } catch (IllegalArgumentException|IllegalStateException|IOException e) {
                e.printStackTrace();
                appEnd();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "surfaceCreated()");
        if(paramSurfaceHolder==holder1) {
            Log.i(TAG, "holder1");
        }
//        mediaPlay(mp, mediaPath);
    }

    @Override
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
                               int paramInt2, int paramInt3) {
        Log.i(TAG, "surfaceChanged()");
        mediaPlay(mp, mediaPath);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "surfaceDestroyed()");
    }
}
