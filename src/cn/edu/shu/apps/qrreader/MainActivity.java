package cn.edu.shu.apps.qrreader;

/**
 * 控制主界面的Activity,负责:
 * 1.摄像头管理
 * 2.对解析图片线程的管理(启动,关闭)
 * 3.摄像头回调函数的处理
 * 4.SurfaceView回调处理 
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;
import cn.edu.shu.apps.qrreader.camera.CameraHandler;

public class MainActivity extends BaseActivity implements CameraHandler.Callback, 
       SurfaceHolder.Callback, Camera.PreviewCallback, Camera.PictureCallback, Handler.Callback, View.OnClickListener {
    public static final String RESULT_BITMAP_URI_KEY= "result_bitmap_uri";
    public static final String RESULT_STRING_KEY = "result_string";

    public static final int PREVIEW_MODE = 1;
    public static final int PICTURE_MODE = 2;

    // 摄像头管理类
    private CameraHandler mCameraHandler;
    // 线程管理类
    private Handler mHandler;

    private String mResult;
    private Uri mUri;
    private boolean isSuccessed = false;

    // 视图元素
    private Button startButton;
    private Button backButton;
    // 专门用于显示摄像头取景的视图类
    private SurfaceView mSurfaceView;

    /**
     * Decode runnable
     * 主要的解析线程
     */
    private Runnable mRunnable = new Runnable () {
        @Override
        public void run() {
            logD("inside the runnable....");
            mCameraHandler.callPreviewFrame(MainActivity.this);
            mHandler.postDelayed(this, 300);
        }
    }; 
    /**
     * Activity LifeCycle Callback
     * 生命周期回调
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.capture_layout);
        
        mSurfaceView = (SurfaceView) findViewById(R.id.main_surface_view);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(this);


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
            isSuccessed = false;
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
     * View OnClick Event
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.start_button:
                if (mCameraHandler.isPreviewing()) {
                    logD("start preview...");
                    startDecode(PICTURE_MODE);
                    // startDecode(PREVIEW_MODE);
                }
                break;
        }
    }

    /**
     * Handler Callback
     */
    @Override
    public boolean handleMessage(Message message) {
        logD("Handling message");
        switch (message.what) {
            case R.id.success:
                if (isSuccessed) return true;
                isSuccessed = true;
                
                logD("Get success message");

                stopDecodePreview();
                Intent i = new Intent(this, OperatingActivity.class);
                i.putExtra(RESULT_BITMAP_URI_KEY, mUri);
                i.putExtra(RESULT_STRING_KEY, mResult);
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
        logD("----- onPreviewFrame -----");

        Utils.ResultSet resultSet = Utils.decodePreviewData(data, this);
        if (resultSet == null) return ;

        mResult = resultSet.getResultStr();
        mUri = resultSet.getResultUri();

        Message message = Message.obtain(mHandler, R.id.success, null);
        mHandler.sendMessage(message);
    }

    /**
     * Camera PictureCallback
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        logD(" ----- on Picture Taken -----");
        Utils.ResultSet resultSet = Utils.decodePictureData(data, this);
        if (resultSet == null) return ;

        mResult = resultSet.getResultStr();
        mUri = resultSet.getResultUri();
        Intent i = new Intent(this, OperatingActivity.class);
        i.putExtra(RESULT_BITMAP_URI_KEY, mUri);
        i.putExtra(RESULT_STRING_KEY, mResult);
        startActivity(i);

        try {
            Thread.sleep(20);
        } catch(Exception e) {
            // do nothing
        }
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
    private void startDecode(int mode) {
        switch(mode) {
            case PREVIEW_MODE:
                logD("In preview mode");
                startDecodePreview();
                break;
            case PICTURE_MODE:
                logD("In picture mode");
                mCameraHandler.callAutoFocusAndTakePicture(null, null, this);
                break;
        }
    }

    private void startDecodePreview() {
        logD("start Decode preview");
        mHandler.post(mRunnable);
    }

    private void stopDecodePreview() {
        logD("stop Decode preview");
        mHandler.removeCallbacks(mRunnable);
    }
}
