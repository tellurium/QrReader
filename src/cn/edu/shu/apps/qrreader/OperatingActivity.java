package cn.edu.shu.apps.qrreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.shu.apps.qrreader.base.BaseActivity;

public class OperatingActivity extends BaseActivity {
    Bitmap mBitmap;
    String mResult;
    Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        mUri = (Uri) i.getParcelableExtra(MainActivity.RESULT_BITMAP_URI_KEY);

        mResult = i.getStringExtra(MainActivity.RESULT_STRING_KEY);

        setContentView(R.layout.operating_layout);        

        ImageView imageView = (ImageView) findViewById(R.id.result_image);
        TextView textView = (TextView) findViewById(R.id.result_string);

        textView.setText(mResult);
        mBitmap = Utils.getBitmapFromUri(mUri, this);
        imageView.setImageBitmap(mBitmap);
    }
}
