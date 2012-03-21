package cn.edu.shu.apps.qrreader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatOneDAndQrCodeReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.androidtest.RGBLuminanceSource; 

public class ImageDecoderActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView tv = new TextView(this);
        tv.setText("Ok.Image 1: " + decodeImage("qrcode-phone-icon.png") +
                ", image 2: " + decodeImage("test2.jpeg") + 
                ", image 3: " + decodeImage("test3.jpg"));

        setContentView(tv);
    }

    public String decodeImage(String filename) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(filename));
            LuminanceSource source = new RGBLuminanceSource(bitmap);
            BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatOneDAndQrCodeReader();
            Result result = reader.decode(bb);

            return result.getText();
        } catch(Exception e) {
            // ....
        }
        return null;
    }
}
