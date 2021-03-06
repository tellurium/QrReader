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

    // 通知摄像头进行一次取景
    public void callPreviewFrame(Camera.PreviewCallback previewCallback) {
        if (isPreviewing) mCamera.setOneShotPreviewCallback(previewCallback);
    }

    // 通知摄像头自动对焦并取景
    public void callAutoFocusAndTakePicture(final Camera.ShutterCallback shutter, 
            final Camera.PictureCallback raw, final Camera.PictureCallback jpeg) {
        if (mCamera != null || isPreviewing) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        // do nothing
                    }
                };
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        mCamera.takePicture(shutterCallback, raw, jpeg);          
                    }
                }
            });
        }
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
            configureCamera(mCamera);
            qOpened = (mCamera != null);
        } catch(Exception e) {
            if (mCallback != null) mCallback.onFailToOpenCamera();
            e.printStackTrace();
        }

        return qOpened;
    }

    public void configureCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();    
        parameters.setPreviewSize(480, 320);//设置尺寸    
        parameters.setPictureFormat(android.graphics.PixelFormat.JPEG);  
        camera.setParameters(parameters); 
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
