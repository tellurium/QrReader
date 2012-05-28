package cn.edu.shu.apps.qrreader.base; 

import cn.edu.shu.apps.qrreader.os.DialogHandler;

public abstract class BaseWithDialogTaskActivity extends BaseActivity implements DialogHandler.Callback {

    protected DialogHandler mDialogHandler;

    protected abstract boolean doInBackground();
    protected abstract void  updateUIAfterTask();

    protected void startNewTaskWithDialog(int dialogTitleId) {
        startNewTaskWithDialog(getString(dialogTitleId));
    }

    protected void startNewTaskWithDialog(String dialogTitle) {
        mDialogHandler = new DialogHandler(this, this);
        mDialogHandler.showDialog(dialogTitle);
        logD("dialog is showing ......");
    }
    
    @Override
    public boolean didTaskStart() {
        logD("task start......");
        return doInBackground();
    }

    @Override
    public void didTaskEnd() {
        logD("task end...");
        updateUIAfterTask();
    }
    
    @Override
    public void didTaskError() {
        logD("task error...");
        // TODO:...
    }

    @Override
    public void didTaskTimerOver() {
        // 
        logD("task time over...");
    }
}
