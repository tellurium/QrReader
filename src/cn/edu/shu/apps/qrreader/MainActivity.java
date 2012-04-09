package cn.edu.shu.apps.qrreader;

import android.content.Intent;
import android.content.res.Configuration;
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
    private Handler mHandler;

    private String mResult;
    /**
     * Decode runnable
     */
    private Runnable mRunnable = new Runnable () {
        @Override
        public void run() {
            logD("inside the runnable....");
            mCameraHandler.callPreviewFrame(MainActivity.this);
            mHandler.postDelayed(this, 500);
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
        mHandler = new Handler(this);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // when configuration changed, we do nothing, avoid to restart and relayout this activity
        logD("inside configchanged");
        logD("new Configuration is " + newConfig.toString());
        return ;
    }

    /**
     * Handler Callback
     */
    public boolean handleMessage(Message message) {
        logD("Handling message");
        switch (message.what) {
            case R.id.success:
                logD("Get success message");

                // FIXME:..
                Intent i = new Intent(this, OperatingActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("result_bitmap", (Bitmap)message.obj);
                b.putString("result_string", mResult);
                i.putExtras(b);
                startActivity(i);
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

        Bitmap bitmap = null;
        String result = Utils.decodePreviewData(data, bitmap);
        if (result == null) return ;
        mResult = result;
        Message message = Message.obtain(mHandler, R.id.success, bitmap);
        mHandler.sendMessage(message);
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
        mHandler.post(mRunnable);
    }

    private void stopDecodePreview() {
        logD("stop Decode preview");
        mHandler.removeCallbacks(mRunnable);
    }
}
