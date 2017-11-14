package com.gkzxhn.xjyyzs.face.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.gkzxhn.xjyyzs.face.model.FaceRect;
import com.gkzxhn.xjyyzs.face.model.FrameFaces;


public class TestImageView extends View {

    private final static String TAG = "FaceImageView";

    private FrameFaces frameFaces = null;
    private int mWidthBmp;
    private int mHeightBmp;

    Paint rectPaint;
    Paint clearPaint;
    private Paint originPaint;
    private boolean isCleared = true;
    private boolean isUsingFront = true;

    public TestImageView(Context context) {
        super(context);

       // this.setWillNotDraw(false);

        initView();
    }

    public TestImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

       // this.setWillNotDraw(false);

        initView();
    }

    private void initView() {
        rectPaint = new Paint();
        rectPaint.setColor(Color.BLUE);
        rectPaint.setStrokeWidth(3);
        rectPaint.setStyle(Paint.Style.STROKE);

        originPaint = new Paint();
        originPaint.setColor(Color.GREEN);
        originPaint.setStrokeWidth(4);
        originPaint.setStyle(Paint.Style.STROKE);

        clearPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.e(TAG,"test的onDraw方法");

        if (frameFaces != null && frameFaces.getFaceRects() != null) {
            if (isUsingFront) {
                for (int i = 0; i < frameFaces.getFaceRects().length; ++i) {
                    FaceRect faceRect = frameFaces.getFaceRects()[i];
                    // 注意这里的坑，相机输入的是垂直的图片，而且坐标系是右上角的，而这里是横屏处理的
                    float w = (float) faceRect.getWidth() * canvas.getWidth() / mHeightBmp;
                    float h = (float) faceRect.getHeight() * canvas.getHeight() / mWidthBmp;
                    float x = (float) (canvas.getWidth() - canvas.getWidth() * faceRect.getLeft() / mHeightBmp - w);
                    float y = (float) faceRect.getTop() * canvas.getHeight() / mWidthBmp;
                    canvas.drawRect(x, y, x + w, y + h, rectPaint);
                }
            } else {
                for (int i = 0; i < frameFaces.getFaceRects().length; i++) {
                    FaceRect faceRect = frameFaces.getFaceRects()[i];
                    float w = (float) faceRect.getWidth() * canvas.getWidth() / mHeightBmp;
                    float h = (float) faceRect.getHeight() * canvas.getHeight() / mWidthBmp;
                    float x = (float) canvas.getWidth() * faceRect.getLeft() / mHeightBmp;
                    float y = (float) faceRect.getTop() * canvas.getHeight() / mWidthBmp;
                    canvas.drawRect(x, y, x + w, y + h, rectPaint);
                }
            }

            isCleared = false;
        } else {
            if (!isCleared) {
                clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPaint(clearPaint);
            }
        }

    }

    public FaceRect[] getFaceRect() {
        return frameFaces.getFaceRects();
    }

    public void setPreviewSize(boolean isFrontCamera, int width, int height, boolean islandscape) {
        isUsingFront = isFrontCamera;
        if (islandscape) {
            mWidthBmp = height;
            mHeightBmp = width;
        } else {
            mWidthBmp = width;
            mHeightBmp = height;
        }
    }

    public void setFaceRect(FrameFaces frameFaces) {
        this.frameFaces = frameFaces;
        this.invalidate();
    }

    public void update() {
        this.invalidate();
    }

    public void clearRect() {
    }
}
