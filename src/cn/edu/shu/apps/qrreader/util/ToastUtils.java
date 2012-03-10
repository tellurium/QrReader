package cn.edu.shu.apps.qrreader.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showShortToast(Context context, int messageId) {
        Toast toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showShortToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
