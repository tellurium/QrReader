package cn.edu.shu.apps.qrreader.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void startActivity(Class nextActivity) {
        startActivity(new Intent(this, nextActivity));
    }

    public void logD(String str) {
        Log.d("The *** "+ this.getClass().getSimpleName(), "Debug info is -----> " + str);        
    }
}
