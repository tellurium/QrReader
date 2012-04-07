package cn.edu.shu.apps.qrreader;

import android.hardware.Camera;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatOneDAndQrCodeReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.androidtest.RGBLuminanceSource; 

public class Utils {

    public static String decodePreviewData(byte[] data, Bitmap bitmap) {
        try {
            // LuminanceSource source = new RGBLuminanceSource(bitmap);
            // BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, 
                    480, 320, 0, 0, 480, 320, false);
            bitmap = source.renderCroppedGreyscaleBitmap();
            BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source)); 
            Reader reader = new MultiFormatOneDAndQrCodeReader();
            Result result = reader.decode(bb);
            return result.getText();
        } catch(Exception e) {
            // .....
        }

        return null;
    }

}
