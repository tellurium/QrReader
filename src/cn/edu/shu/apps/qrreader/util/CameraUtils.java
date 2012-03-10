package cn.edu.shu.aps.qrreader.util;

import android.content.Context;

public class CameraUtils {

    /**
     * Detecting camera hardware
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get front-facing camera
     */
    public static Camera getFrontCameraInstance() {
        
    }

    /**
     * Get back-faceing camera
     */
    public static Camera getBackCameraInstance() {
        
    }

    /**
     * Get camera instance common method
     */
    public static Camera getCameraInstnce() {

    }
}
