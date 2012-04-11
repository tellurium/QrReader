package cn.edu.shu.apps.qrreader;

import android.content.Context;
import android.hardware.Camera;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatOneDAndQrCodeReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.androidtest.RGBLuminanceSource; 

public class Utils {

    public static ResultSet decodePreviewData(byte[] data, Context context) {
        try {
            // LuminanceSource source = new RGBLuminanceSource(bitmap);
            // BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, 
                    480, 320, 0, 0, 480, 320, false);

            BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source)); 
            Reader reader = new MultiFormatOneDAndQrCodeReader();
            Result result = reader.decode(bb);
            String resultStr = result.getText();
            if (resultStr != null) {
                Bitmap resultImage = source.renderCroppedGreyscaleBitmap();
                Uri uri = saveBitmapInTmpDir(resultImage, context);
                return new ResultSet(resultStr, uri);
            } else {
                return null;
            }
        } catch(Exception e) {
            // .....
        }
        return null;
    }

    public static class ResultSet {
        private String resultStr;
        private Uri uri;

        public ResultSet(String resultStr, Uri uri) {
            this.resultStr = resultStr;
            this.uri = uri;
        }

        public String getResultStr() {
            return resultStr;
        }

        public Uri getResultUri() {
            return uri;
        }
    }

    // some bitmap util methods
    public static Uri saveBitmapInTmpDir(Bitmap bitmap, Context context) {
        File saveDir = context.getCacheDir();
        if (saveDir != null) {
            File tmpFile = new File(saveDir, System.nanoTime() + ".jpg");
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tmpFile));
            } catch(IOException e) {
                // do nothing...
            }
            android.util.Log.d("<--- Utils --->", "uri is " + Uri.fromFile(tmpFile).toString());
            return Uri.fromFile(tmpFile);
        } else {
            return null;
        }
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        Bitmap bmp = null;

        try {
            bmp = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bmpFactoryOptions);
        } catch(Exception e) {
            android.util.Log.d("<--- Utils --->", "getBitmapFromUri is wrong");
            e.printStackTrace();
        }
        return bmp;
    }
}
