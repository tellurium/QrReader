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
}
