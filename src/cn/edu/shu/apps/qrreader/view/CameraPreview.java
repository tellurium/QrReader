package cn.edu.shu.apps.qrreader.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;

import cn.edu.shu.apps.qrreader.R;
import cn.edu.shu.apps.qrreader.util.ToastUtils;

public class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;

    public interface Callback {

    }

    public CameraPreview(Context context) {
        super(context);
        
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        //
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /* try { */
        /*     mCamera = Camera.open(); */
        /* } catch(Exception e) { */
        /*     mCamera.release(); */
        /*     return ; */
        /* } */

        /* try { */
        /*     Camera.Parameters parameters = mCamera.getParameters(); */
        /*     parameters.setPictureFormat(PixelFormat.JPEG); */
        /*     parameters.setPreviewSize(320, 240); */
        /*     parameters.setFocusMode("auto"); */
        /*     parameters.setPictureSize(320, 960); */
        /*     mCamera.setParameters(parameters); */
        /*     mCamera.setPreviewDisplay(mHolder); */
        /* } catch(IOException e) { */
        /*     mCamera.release(); */
        /*     mCamera = null; */
        /* } */
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /* if (mCamera == null)  return ; */
        /* if (mHolder.getSurface() == null)  return ; */

        /* try { */
        /*     mCamera.stopPreview(); */
        /* } catch(Exception e) { */
        /*     e.printStackTrace(); */
        /* } */
        /*  */
        /* try { */
        /*     mCamera.startPreview(); */
        /* } catch(Exception e) { */
        /*     e.printStackTrace(); */
        /* } */
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        /* if (mCamera == null)  return ; */

        /* mCamera.stopPreview(); */
        /* mCamera.release(); */
        /* mCamera = null; */
    }

}
