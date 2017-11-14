package com.gkzxhn.xjyyzs.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

import com.colorreco.libface.CRFace;
import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.face.camera.CameraInterface;
import com.gkzxhn.xjyyzs.face.camera.CameraSurfaceView;
import com.gkzxhn.xjyyzs.face.camera.FrameCallback;
import com.gkzxhn.xjyyzs.face.model.FrameFaces;
import com.gkzxhn.xjyyzs.face.view.TestImageView;
import com.gkzxhn.xjyyzs.fragments.GuestFragment;
import com.gkzxhn.xjyyzs.utils.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraSurfaceActivity extends BaseActivity implements FrameCallback, SdkProcessor.SdkCallback {

    private static final String TAG = "CameraSurfaceActivity";

    @BindView(R.id.camera_view)
    CameraSurfaceView surfaceView;

    @BindView(R.id.faceview)
    TestImageView faceImageView;

    private Handler _uihandler;
    private Map<String, float[]> fea_db;
    private SdkProcessor mSdk;
    private Lock mLock;
    private int _sdk_do_what = 0;
    private boolean push_register_data = false;
    private ByteBuffer buf_img1;
    private int w_img_1;
    private int h_img_1;
    private String is_last_person = "";
    private String buf_img1_path_id;

    private Intent intent;

    private float[] feaB;//用于存放身份证头像抽取到的人脸特征

    private Bundle savedInstanceState;

    private int counter = 0;

    int i = 0;
    private byte[] mSurfaceData = null;//surface全景

    @Override
    protected View initView() {

        View view = View.inflate(this, R.layout.activity_camera_surface, null);

        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    protected void initData() {


        savedInstanceState = getIntent().getExtras();

        feaB = savedInstanceState.getFloatArray("feaB");


        CRFace.getInstance().loadLibrarySys("crface");

        int res = CRFace.getInstance().initSDK("/sdcard/colorreco");//res为1时初始化成功

        if (res == 1) {
            Config.isInitSuccess = true;
        }

        Log.e(TAG, "res = " + res);


        removeTitleBar();

        surfaceView.setmCb(this);

        _uihandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        fea_db = new HashMap<String, float[]>();

        mSdk = new SdkProcessor(this);

        mLock = new ReentrantLock();

    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public void get(final FrameFaces faces) {
        final int tmp_w = CameraInterface.getInstance().getPreviewWidth();
        final int tmp_h = CameraInterface.getInstance().getPreviewHeight();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceImageView.setPreviewSize(false, tmp_w, tmp_h, true);
                faceImageView.setFaceRect(faces);
            }
        });

        //Log.e(TAG, "get方法");

        if (feaB != null && faces.getProcess_Type().equals("match")) {

            // Log.e(TAG,"进了if了");

            //faces.setFeature(feature);


            float similarity = CRFace.getInstance().match(feaB, faces.getFeature());

            //Log.e(TAG,"feature = "+java.util.Arrays.toString(faces.getFaceRects()));
            Log.e(TAG, "similarity = " + similarity);
            if (mSurfaceData != null) {
                YuvImage localYuvImage = new YuvImage(mSurfaceData, 17, CameraInterface.getInstance().getPreviewWidth(), CameraInterface.getInstance().getPreviewHeight(), null);
                ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
                //把摄像头回调数据转成YUV，再按图像尺寸压缩成JPEG，从输出流中转成数组
//                Rect rect=new Rect(0, 0, CameraInterface.getInstance().getPreviewWidth(), CameraInterface.getInstance().getPreviewHeight());
                localYuvImage.compressToJpeg(faces.getFaceRects()[0].toRect(), 80, localByteArrayOutputStream);
                byte[] mParamArrayOfByte = localByteArrayOutputStream.toByteArray();
                //生成Bitmap
                BitmapFactory.Options localOptions = new BitmapFactory.Options();
                localOptions.inPreferredConfig = Bitmap.Config.RGB_565;  //构造位图生成的参数，必须为565。类名+enum
                GuestFragment.mSurfaceFace = BitmapFactory.decodeByteArray(mParamArrayOfByte, 0, mParamArrayOfByte.length, localOptions);
            }
//            //similarity  -1到1  (similarity+1)/(1-(-1))=百分比
//            int customSimilarity= (int) SPUtil.get(this, Constant.CUSTOM_SIMILARITY,60);
//            float compareSimilarity=(2*customSimilarity/100)-1;// －1到1的范围值
            if (similarity >= 0.3) {
                Log.e(TAG, "人脸比对成功");
                Intent intent = new Intent();
                intent.putExtra("similarity", similarity);
                setResult(RESULT_OK, intent);
                finish();
                //这里设置如果对比成功则显示成功并关闭当前activity
            } else if (similarity < 0.3) {
                Log.e(TAG, "失败");
                i++;
                if (i >= 5) {
                    Intent intent = new Intent();
                    intent.putExtra("similarity", similarity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        } else {
            Log.e(TAG, "没进if");
        }

    }


    @Override
    public void onDecodeFrame(byte[] data) {
        mSurfaceData = data;
        if (counter > 1000000) {
            counter = 0;
        }

        if (mSdk != null) {

            boolean isExtract = false;
            if (counter++ % 5 == 0) {
                isExtract = true;
                ;
            }
            mSdk.setData(data, CameraInterface.getInstance().getPreviewWidth(), CameraInterface.getInstance().getPreviewHeight(), 1, isExtract, false);

        }


    }


    private void processImg(Bitmap bm, String id) {
        buf_img1 = ByteBuffer.allocate(bm.getWidth() * bm.getHeight() * 4);
        bm.copyPixelsToBuffer(buf_img1);
        w_img_1 = bm.getWidth();
        h_img_1 = bm.getHeight();
        buf_img1_path_id = id;
        match();
    }

    private void match() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLock.lock();
                _sdk_do_what = 2;
                mLock.unlock();
            }
        }).start();
    }

    private int fea_db_insert(String id, float[] fea) {
        mLock.lock();
        if (!fea_db.containsKey(id)) {
            fea_db.put(id, fea.clone());
            mLock.unlock();
            return 1;
        } else {
            mLock.unlock();
            return 0;
        }
    }
}
