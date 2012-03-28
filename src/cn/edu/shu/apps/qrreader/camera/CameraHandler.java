package cn.edu.shu.apps.qrreader.camera;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.List;

import cn.edu.shu.apps.qrreader.view.CameraPreview;

public class CameraHandler {
    private Camera mCamera;
    private CameraHandler.Callback mCallback;
    private CameraPreview mPreview;
    private List<Size> mSupportedPreviewSize;

    /**
     * Set a callback to respond some events
     */
    public interface Callback {
        public void onFailToOpenCamera();            
        public void onFailToDisplayPreview();
        public void requestPreviewLayout();
        public SurfaceHolder getSurfaceHolder();
    }

    public CameraHandler(CameraHandler.Callback callback, CameraPreview preview) {
        mCallback = callback;
        mPreview = preview;
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) return ;

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            mSupportedPreviewSize = mCamera.getParameters().getSupportedPreviewSizes();
            mCallback.requestPreviewLayout();

            try {
                mCamera.setPreviewDisplay(mCallback.getSurfaceHolder());
            } catch(Exception e) {
                if (mCallback != null) mCallback.onFailToDisplayPreview();
                e.printStackTrace();
            }

            mCamera.startPreview();
        }
    }

    public boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open();
            qOpened = (mCamera != null);
        } catch(Exception e) {
            if (mCallback != null) mCallback.onFailToOpenCamera();
            e.printStackTrace();
        }

        return qOpened;
    }

    public void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
