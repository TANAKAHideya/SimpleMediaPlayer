package com.example.simplemediaplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import com.example.android.apis.R;
//import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
//import android.widget.Toast;
import android.widget.VideoView;
import android.view.SurfaceHolder;

import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "SimpleMediaPlayer";
    private static final String MP4_FILE = "video/a1.mp4";

    private SurfaceHolder	holder1;
    private PowerManager.WakeLock lock;

    @SuppressWarnings({"deprecation", "deprecation"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView mPreview;

		/* Set Full screen flag */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* Support Transparent color */
        //getWindow().setFormat(PixelFormat.TRANSPARENT);
        getWindow().setFormat(PixelFormat.RGBX_8888);
        /* No window title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        //layout_height
        mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        //mPreview.setSecure(true);
        holder1 = mPreview.getHolder();
        holder1.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder1.addCallback(this);

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        lock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);

        Log.i(TAG, "Create");

    }
    @Override
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        Log.i(TAG, "Enter");

        MediaPlayer mp = new MediaPlayer();
        String mediaPath;

        if(paramSurfaceHolder==holder1){
            Log.i(TAG, "holder1");
            mediaPath = System.getenv("EXTERNAL_STORAGE") + "/" + MP4_FILE;
            mp.setDisplay(paramSurfaceHolder);
            mp.setVolume((float)0.5,(float)0.5);

            lock.acquire();
            mediaPlay(mp,paramSurfaceHolder,mediaPath);
        }
    }

    public void mediaPlay(MediaPlayer mp,SurfaceHolder paramSurfaceHolder,String mediaPath) {
        try {
            mp.setLooping(true);
            mp.setDataSource(mediaPath);
            //mp.setDisplay(paramSurfaceHolder);
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        if(lock.isHeld()) lock.release();
    }
}


