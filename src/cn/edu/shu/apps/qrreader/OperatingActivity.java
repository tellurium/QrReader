package cn.edu.shu.apps.qrreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;

public class OperatingActivity extends BaseActivity {
    Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mBitmap = (Bitmap) b.getParcelable("result_bitmap");

        android.widget.ImageView imageView = new android.widget.ImageView(this);
        imageView.setImageBitmap(mBitmap);

        TextView textView = new TextView(this);
        textView.setText(b.getString("result_string"));
        setContentView(textView);
    }
}
