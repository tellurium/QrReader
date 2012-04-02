package cn.edu.shu.apps.qrreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;
import cn.edu.shu.apps.qrreader.camera.CameraHandler;

public class MainActivity extends BaseActivity implements CameraHandler.Callback, 
       SurfaceHolder.Callback, Camera.PreviewCallback, Handler.Callback {
    private SurfaceView mSurfaceView;
    private CameraHandler mCameraHandler;
    private Handler mHander;

    /**
     * Decode runnable
     */
    private Runnable mRunnable = new Runnable () {
        @Override
        public void run() {
            logD("inside the runnable....");
            mCameraHandler.callPreiviewFrame(MainActivity.this);
            mHander.postDelayed(this, 1500);
        }
    };
    
    /**
     * Activity LifeCycle Callback
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.capture_layout);
        
        mSurfaceView = (SurfaceView) findViewById(R.id.main_surface_view);

        mCameraHandler = new CameraHandler(this);
        mHander = new Handler(this);

        logD("onCreate ...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraHandler.safeCameraOpen()) {
            logD("inside onResume...");
            mCameraHandler.startCamera();
        }
        if (mCameraHandler.isPreviewing()) {
            logD("start preview...");
            startDecodePreview();
        }
    }

    @Override
    protected void onPause() {
        logD("inside onPause...");
        mCameraHandler.stopPreviewAndFreeCamera();
        stopDecodePreview();
        super.onPause();
    }

    /**
     * Handler Callback
     */
    public boolean handleMessage(Message message) {
        logD("Handling message");
        switch (message.what) {
            case R.id.success:
                logD("Get success message");
                startActivity(OperatingActivity.class);
                break;
            default:
                break;
        }
        return true;
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
     * Camera PreviewCallbak
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        logD("-----onPreviewFrame-----");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        String result = Utils.decodePreviewData(bitmap);
        if (result == null) return ;
        Message message = Message.obtain(mHander, R.id.success, bitmap);
        mHander.sendMessage(message);
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

    /**
     * Private methods
     */
    private void startDecodePreview() {
        logD("start Decode preview");
        mHander.post(mRunnable);
    }

    private void stopDecodePreview() {
        logD("stop Decode preview");
        mHander.removeCallbacks(mRunnable);
    }
}
