package cn.edu.shu.apps.qrreader.view;

import android.content.Context;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraPreview extends ViewGroup {
    private SurfaceView mSurfaceView;
    private CameraPreview.Callback mCallback;

    public interface Callback {

    }

    public CameraPreview(Context context) {
        super(context);
        
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        
    }

    public SurfaceView getSurfaceView() {
        return this.mSurfaceView;
    }
    
    public void setSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }
}
