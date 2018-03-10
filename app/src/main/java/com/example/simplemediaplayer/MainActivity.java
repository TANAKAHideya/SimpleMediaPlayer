package com.example.simplemediaplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "SimpleMediaPlayer";

    private static final String MP4_FILE = "a1.mp4";
    private static final String VIDEO_PATH = System.getenv("EXTERNAL_STORAGE") + "/video/";
    String mediaPath =  VIDEO_PATH + MP4_FILE;

    private SurfaceHolder holder1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        /* No window title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SurfaceView mPreview;
        setContentView(R.layout.activity_main);

		/* Set Full screen flag */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* add FLAG_KEEP_SCREEN_ON */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /* Support Transparent color */
        //getWindow().setFormat(PixelFormat.TRANSPARENT);
        //getWindow().setFormat(PixelFormat.RGBX_8888);

        if (new File(mediaPath).exists()) {
            Log.i(TAG, "A video file is exist");

            mPreview = (SurfaceView) findViewById(R.id.surfaceView);
            //mPreview.setSecure(true);

            holder1 = mPreview.getHolder();
            holder1.addCallback(this);
        } else {
            Log.i(TAG, "A video file is not exist");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Put \"" + MP4_FILE + "\" to " + VIDEO_PATH + ", and Execute again.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { appEnd(); }
                    });
            builder.show();
        }
    }

    public void appEnd(){
        this.finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "Enter");
        MediaPlayer mp = new MediaPlayer();

        if(paramSurfaceHolder==holder1) {
            Log.i(TAG, "holder1");

            mp.setDisplay(paramSurfaceHolder);
            mp.setVolume((float) 0.5, (float) 0.5);
            mediaPlay(mp, mediaPath);
        }
    }

    public void mediaPlay(MediaPlayer mp,String mediaPath) {
        try {
            mp.setLooping(true);
            mp.setDataSource(mediaPath);
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException|IllegalStateException|IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
                               int paramInt2, int paramInt3) {
        Log.i(TAG, "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "surfaceDestroyed()");
    }
}
