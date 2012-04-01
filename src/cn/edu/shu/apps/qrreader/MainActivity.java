package cn.edu.shu.apps.qrreader;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;
import cn.edu.shu.apps.qrreader.camera.CameraHandler;

public class MainActivity extends BaseActivity implements CameraHandler.Callback, SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private CameraHandler mCameraHandler;
    private Bitmap mBitmap;
    
    /**
     * Activity LifeCycle Callback
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        FrameLayout fl = new FrameLayout(this);
        mSurfaceView = new SurfaceView(this);
        fl.addView(mSurfaceView);

        mCameraHandler = new CameraHandler(this);
        
        setContentView(fl);

        logD("onCreate ...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraHandler.safeCameraOpen()) {
            logD("inside onResume...");
            mCameraHandler.startCamera();
        }
    }

    @Override
    protected void onPause() {
        logD("inside onPause...");
        mCameraHandler.stopPreviewAndFreeCamera();
        super.onPause();
    }

    /**
     * CameraHanlder Callback
     */
    @Override
    public SurfaceHolder getSurfaceHolder() {

        SurfaceHolder holder = null;
        if (mSurfaceView != null) {
            logD("Want to get the Surface holder.....");   
            holder = mSurfaceView.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        return holder;
    }

    @Override
    public void onFailToOpenCamera() {
        logD("Fail to open the camera.....");   
    }

    @Override
    public void onFailToDisplayPreview() {
        logD("Fail to displat preview.....");   
    }

    @Override
    public void requestPreviewLayout() {
        logD("Send a request to layout.....");   
    }

    /**
     * SurfaceHolder Callback
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        logD("Surface Created");   
        if (mCameraHandler.safeCameraOpen()) {
            mCameraHandler.startCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        logD("Surface Changed");   
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraHandler.stopPreviewAndFreeCamera();
        logD("Surface Destroyed");   
    }
}
