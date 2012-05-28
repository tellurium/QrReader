package cn.edu.shu.apps.qrreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import cn.edu.shu.apps.qrreader.base.BaseWithDialogTaskActivity;
import cn.edu.shu.apps.qrreader.util.ToastUtils;

public class OperatingActivity extends BaseWithDialogTaskActivity implements View.OnClickListener {
    private static final String SERVER_URL = "http://smalldatareceiverofhuairenpp.cloudfoundry.com/DecodedDataReceiver?isClient=true&data=";

    private Bitmap mBitmap;
    private String mResult;
    private String mRequestResult;
    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.operating_layout);    

        Intent i = getIntent();
        mUri = (Uri) i.getParcelableExtra(MainActivity.RESULT_BITMAP_URI_KEY);

        mResult = i.getStringExtra(MainActivity.RESULT_STRING_KEY);

        setContentView(R.layout.operating_layout);        

        ImageView imageView = (ImageView) findViewById(R.id.result_image);
        TextView textView = (TextView) findViewById(R.id.result_string);
        Button backButton = (Button) findViewById(R.id.operate_back_button);
        backButton.setOnClickListener(this);
        Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(this);

        textView.setText(mResult);
        mBitmap = Utils.getBitmapFromUri(mUri, this);
        imageView.setImageBitmap(mBitmap);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.operate_back_button:
                this.finish();
                break;
            case R.id.upload_button:
                uploadData();
                break;
        }
    }

    @Override
    public boolean doInBackground() {
        try {
            URL url = new URL(SERVER_URL + mResult);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), "UTF-8")); 
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            mRequestResult = sb.toString();
            if (mRequestResult != null && mRequestResult.length() > 0) return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateUIAfterTask() {
        if (mRequestResult == null || !mRequestResult.equals("success")) {
            ToastUtils.showShortToast(this, "上传失败");
        } else {
            ToastUtils.showShortToast(this, "上传成功");    
        }
    }

    @Override
    public void didTaskTimerOver() {
        logD("task time over...");
        ToastUtils.showShortToast(this, "上传超时");
    }
    
    private void uploadData() {
        startNewTaskWithDialog("正在上传中....");
    }
}
