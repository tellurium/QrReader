package cn.edu.shu.apps.qrreader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;

public class OperatingActivity extends BaseActivity {
    Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("Hello");

        setContentView(textView);
    }
}
