package com.example.simplemediaplayer;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "SimpleMediaPlayer";
    private static final String MP4_FILE = "/video/a1.mp4";
    private SurfaceHolder holder1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //layout_height
        mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        //mPreview.setSecure(true);
        holder1 = mPreview.getHolder();
        holder1.addCallback(this);


        Log.i(TAG, "Created");
    }

    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "Enter");
        MediaPlayer mp = new MediaPlayer();

        if(paramSurfaceHolder==holder1) {
            Log.i(TAG, "holder1");
            String mediaPath;

            mp.setDisplay(paramSurfaceHolder);
            mp.setVolume((float) 0.5, (float) 0.5);

            mediaPath = System.getenv("EXTERNAL_STORAGE") + MP4_FILE;
            TextView tv = (TextView) findViewById(R.id.textView);
            File file = new File(mediaPath);
            if (!file.exists()) {
                tv.setText("Store a1.mp4 to " + mediaPath);
                Log.i(TAG, "A video file is not exist");
            } else {
                tv.setText("");
                Log.i(TAG, "A video file is exist");
                mediaPlay(mp, mediaPath);
            }
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


