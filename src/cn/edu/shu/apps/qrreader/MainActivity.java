package cn.edu.shu.apps.qrreader;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;
import cn.edu.shu.apps.qrreader.view.CameraPreview;

public class MainActivity extends BaseActivity implements Camera.PictureCallback {
    private CameraPreview mCameraPreview;
    private Bitmap mBitmap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        FrameLayout fl = new FrameLayout(this);
        mCameraPreview = new CameraPreview(this);
        fl.addView(mCameraPreview);

        TextView tv = new TextView(this);
        tv.setText("Please press camera button....");
        fl.addView(tv);

        setContentView(fl);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        android.util.Log.d("MainActivity:onPictureTaken()", " -----> Picture has been taken");
        return ;
    }
}
