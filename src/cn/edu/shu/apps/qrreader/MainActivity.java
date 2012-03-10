package cn.edu.shu.apps.qrreader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.shu.aps.qrreader.camera.CameraPreview;

public class MainActivity extends Activity implements Camera.PictureCallback {
    private CameraPreview mCameraPreview;
    private Bitmap mBitmap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        return ;
    }
}
