package cn.edu.shu.apps.qrreader.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtils {

    public static void showMessageDialog(Context context, int titleId, int messageId) {
        Dialog d = new AlertDialog.Builder(context)
            .setTitle(titleId)
            .setPositiveButton(android.R.string.ok, null)
            .setMessage(messageId)
            .create();
        d.show();
    }

    public static void showAlert(Context context, int titleId, int messageId, 
            int positiveButtonStrId, DialogInterface.OnClickListener positiveListener, 
            int negativeButtonStrId, DialogInterface.OnClickListener negativeListener) {
        Dialog d = new AlertDialog.Builder(context)
            .setTitle(titleId)
            .setMessage(messageId)
            .setCancelable(false)
            .setPositiveButton(positiveButtonStrId, positiveListener)
            .setNegativeButton(negativeButtonStrId, negativeListener)
            .create();
        d.show();
    }
}
