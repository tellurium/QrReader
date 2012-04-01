package cn.edu.shu.apps.qrreader.camera;

import android.hardware.Camera;
import android.view.SurfaceHolder;

public class CameraHandler {
    private Camera mCamera;
    private CameraHandler.Callback mCallback;
    private boolean isPreviewing;

    /**
     * Set a callback to respond some events
     */
    public interface Callback {
        public void onFailToOpenCamera();            
        public void onFailToDisplayPreview();
        public void requestPreviewLayout();
        public void onPreviewFrame(byte[] data, Camera camera);
        public SurfaceHolder getSurfaceHolder();
    }

    public CameraHandler(CameraHandler.Callback callback) {
        isPreviewing = false;
        mCallback = callback;
    }

    public void callPreiviewFrame(Camera.PreviewCallback previewCallback) {
        if (isPreviewing) mCamera.setOneShotPreviewCallback(previewCallback);
    }

    public void startCamera() {
        if (mCamera != null) {
            mCallback.requestPreviewLayout();

            try {
                mCamera.setPreviewDisplay(mCallback.getSurfaceHolder());
            } catch(Exception e) {
                if (mCallback != null) mCallback.onFailToDisplayPreview();
                e.printStackTrace();
            }

            mCamera.startPreview();
            isPreviewing = true;
        }
    }

    public boolean safeCameraOpen() {
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
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }
    
    public boolean isPreviewing() {
        return isPreviewing;
    }
}
