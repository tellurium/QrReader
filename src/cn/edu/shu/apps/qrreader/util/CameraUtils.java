package cn.edu.shu.aps.qrreader.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

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
        return null;
    }

    /**
     * Get back-faceing camera
     */
    public static Camera getBackCameraInstance() {
        return null;
    }

    /**
     * Get camera instance common method
     */
    public static Camera getCameraInstnce() {
        return null;
    }
}
