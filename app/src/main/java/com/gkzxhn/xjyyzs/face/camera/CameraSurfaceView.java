package com.gkzxhn.xjyyzs.face.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gkzxhn.xjyyzs.face.Config;


public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "CameraSurfaceView";

    Context mContext;
    SurfaceHolder mSurfaceHolder;

    public FrameCallback getmCb() {
        return mCb;
    }

    public void setmCb(FrameCallback mCb) {
        this.mCb = mCb;
    }

    private FrameCallback mCb;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent 半透明 transparent͸透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        mSurfaceHolder.addCallback(this);//添加回调
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.e(TAG, "surfaceCreated");

        CameraInterface.getInstance().doOpenCamera(null);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        Log.e(TAG, "surfaceChanged");

        CameraInterface.getInstance().doStartPreview(mSurfaceHolder, 1.333f, this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.e(TAG, "surfaceDestroyed");

        CameraInterface.getInstance().doStopCamera();
    }

    ;

    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

       // Log.e(TAG, "onPreViewFrame");

        if (Config.isInitSuccess) {
            if (mCb != null) {
                mCb.onDecodeFrame(data);
            }
        }
    }

    public void switchCamera() {

        CameraInterface.getInstance().switchCamera(mSurfaceHolder, 1.33f, this);
    }
}
