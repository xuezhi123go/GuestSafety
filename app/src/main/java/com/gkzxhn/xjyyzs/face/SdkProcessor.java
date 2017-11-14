package com.gkzxhn.xjyyzs.face;

import android.util.Log;

import com.colorreco.libface.CRFace;
import com.gkzxhn.xjyyzs.face.model.FaceRect;
import com.gkzxhn.xjyyzs.face.model.FrameFaces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mae on 2017/5/9.
 */

public class SdkProcessor {
    private static final String TAG = "SdkProcessor";

    private LinkedBlockingQueue<byte[]> mFrameBuffer = new LinkedBlockingQueue<byte[]>(1);
    private byte[] mdata;
    private int mW;
    private int mH;
    private int mType;
    private boolean isExtract;
    private boolean isExtractOk;

    private ExecutorService executorService;

    private int[] faces = null;

    public SdkProcessor(SdkCallback cb) {

        mCb = cb;


        if (mCb != null) {
            Log.e(TAG, "这里mCb就不是空了");
        } else {
            Log.e(TAG, "这里mCb怎么还是空啊");
        }

        //Log.e(TAG,"这里到底执行没有啊");

        lacunch();
    }

    public void lacunch() {

        /*Log.e(TAG, "执行lacunch方法");*/

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(mFaceProcessRunnable);
    }

    public static interface SdkCallback {
        public void get(final FrameFaces faces);
    }

    private SdkCallback mCb;
    private Lock mLock = new ReentrantLock();

    public void setData(byte[] data, int w, int h, int t, boolean isExtract, boolean isPersist) {
        if (!isPersist && mFrameBuffer.isEmpty()) {
            mFrameBuffer.add(data);
            mLock.lock();
            mW = w;
            mH = h;
            mType = t;
            this.isExtract = isExtract;
            mLock.unlock();
        } else if (isPersist) {
            while (!mFrameBuffer.isEmpty()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mFrameBuffer.add(data);
            mLock.lock();
            mW = w;
            mH = h;
            mType = t;
            this.isExtract = isExtract;
            mLock.unlock();
            Log.v("TAG", "persist insert ok:width:" + mW + "height:" + mH);
        }
    }

    private Runnable mFaceProcessRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    mdata = mFrameBuffer.take();
                    mLock.lock();
                    final int process_type = mType;
                    final boolean is_extract = isExtract;
                    final int width = mW;
                    final int height = mH;
                    mLock.unlock();
                    float feature[] = new float[256];
                    faces = null;
                    isExtractOk = false;
                    if (process_type == 0) {
                        continue;
                    }

                    if (process_type == 1) {

                        faces = CRFace.getInstance().detectYUV(mdata, width, height, 1, null);

                        Log.v(TAG, "detected: " + faces[0]);

                        if (faces != null && faces[0] > 0 && is_extract) {
                            int tmpBox[] = new int[4];

                            tmpBox[0] = faces[1];
                            tmpBox[1] = faces[2];
                            tmpBox[2] = faces[3];
                            tmpBox[3] = faces[4];

                            long time1 = System.currentTimeMillis();
                            int res = CRFace.getInstance().extractYUV(mdata, width, height, tmpBox, feature);
                            long time2 = System.currentTimeMillis();

                            long time = time2 - time1;

                            Log.e(TAG, "时间是：" + time);

                            if (res == 256) {
                                isExtractOk = true;
                            }

                        }
                    }

                    FrameFaces frameFaces = new FrameFaces(null);

                    if (faces != null && faces[0] > 0) {
                        int count = faces[0];
                        int step = 5;
                        FaceRect[] faceRects = new FaceRect[count];
                        for (int i = 0; i < count; i++) {
                            FaceRect faceRect = new FaceRect(faces[i * step + 1], faces[i * step + 2], faces[i * step + 3], faces[i * step + 4]);
                            faceRects[i] = faceRect;
                        }
                        frameFaces.setFaceRects(faceRects);
                        if (is_extract && isExtractOk) {
                            frameFaces.setFeature(feature);
                            frameFaces.setFeatureOk(true);
                        } else {
                            frameFaces.setFeatureOk(false);
                        }
                    }

                    if (process_type == 1) {
                        if (is_extract)
                            frameFaces.setProcess_Type("match");
                        else frameFaces.setProcess_Type("detect");
                    } else if (process_type == 2) {
                        frameFaces.setProcess_Type("register");
                    }


                    if (mCb != null && faces[0] > 0) {
                        mCb.get(frameFaces);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public float match(float[] feaA, float[] feab) {

        return CRFace.getInstance().match(feaA, feab);

    }
}
