package cn.edu.shu.apps.qrreader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.androidtest.RGBLuminanceSource; 

public class ImageDecoderActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("qrcode-phone-icon.png"));
            LuminanceSource source = new RGBLuminanceSource(bitmap);
            BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bb);

            final TextView tv = new TextView(this);
            tv.setText(result.getText());
            setContentView(tv);
        } catch(Exception e) {
            // ....
        }
    }
}
