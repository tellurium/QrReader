package cn.edu.shu.apps.qrreader.os;

/**
 * 一个带有ProgressBar的Handler
 * 在创建的一开始就显示一个ProgressBar,并执行后台操作
 * 在获得数据或者在指定时间内没有完成则会关闭ProgressBar,并显示相应的信息
 * 同时需要设定列表组件的单击事件
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class DialogHandler extends Handler {
    // message constants
    public static final int BACKGROUND_TASK_START = 0x00001;
    public static final int BACKGROUND_TASK_END = 0x00002;
    public static final int BACKGROUND_TASK_ERROR = 0x00003;
    public static final int BACKGROUND_TIME_OVER = 0x00004;

    public interface Callback {
        public boolean didTaskStart();
        public void didTaskEnd();
        public void didTaskError();
        public void didTaskTimerOver();
    }

    private DialogHandler.Callback mDialogCallback;
    private Context mContext;
    private ProgressDialog mDialog;
    // Main thread running in background
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean task_state = false;
                    task_state = mDialogCallback.didTaskStart();
                    int message_id;
                    if (task_state) {
                        message_id = BACKGROUND_TASK_END;
                    } else {
                        message_id = BACKGROUND_TASK_ERROR;
                    }
                    sendMessage(Message.obtain(DialogHandler.this, message_id));
                    android.util.Log.d("DialogHanlder", "main task runnable .....");
                }
            }).start();
        }
    };

    // Another thread add a deadline for background task to avoid endless progress dialog or ANR
    // if you want to set a new value, you should call setDelayTime() before calling showDialog()
    public static final int DEFAULT_DELAY_TIME =  10 * 1000; // default deadline time is 15s
    private int mDelayTime = 0;
    private Runnable mTaskTimerRunnable = new Runnable() {
        @Override
        public void run() {
            // tell the main thread that task error, please update the ui to tell users
            sendMessage(Message.obtain(DialogHandler.this, BACKGROUND_TIME_OVER));
            android.util.Log.d("DialogHanlder", "task timer .....");
        }
    };

    public DialogHandler(Context context, DialogHandler.Callback callback) {
        mContext = context;
        mDialogCallback = callback;
    }

    public void showDialog(String dialogMessage) {
        sendMessage(Message.obtain(this, BACKGROUND_TASK_START, dialogMessage));
        post(mRunnable);
        /* postDelayed(mRunnable, 1 * 1000); */
        int delay_time = getDelayTime();
        if (delay_time == 0) delay_time = DEFAULT_DELAY_TIME;  
        postDelayed(mTaskTimerRunnable, delay_time);
    }

    public void removeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        removeCallbacks(mRunnable);
        removeCallbacks(mTaskTimerRunnable);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case BACKGROUND_TASK_START:
                mDialog = ProgressDialog.show(mContext, "", (String) msg.obj, true, false);
                break;
            case BACKGROUND_TASK_END:
                mDialogCallback.didTaskEnd();
                removeDialog();
                break;
            case BACKGROUND_TASK_ERROR:
                mDialogCallback.didTaskError();
                break;
            case BACKGROUND_TIME_OVER:
                removeDialog();
                mDialogCallback.didTaskTimerOver();
                break;
        }
    }

    public int getDelayTime() {
        return this.mDelayTime;
    }
    
    public void setDelayTime(int delayTime) {
        this.mDelayTime= delayTime;
    }
}
