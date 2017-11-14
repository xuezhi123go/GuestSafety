package com.gkzxhn.xjyyzs.face.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.gkzxhn.xjyyzs.face.util.CamParaUtil;
import com.gkzxhn.xjyyzs.face.util.FileUtil;
import com.gkzxhn.xjyyzs.face.util.ImageUtil;
import com.gkzxhn.xjyyzs.fragments.GuestFragment;

import java.io.IOException;
import java.util.List;


public class CameraInterface {


    private static final String TAG = "CameraInterface";

    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;
    // preview width
    private int pWidth;
    // preview height
    private int pHeight;

    private int currentCameraId;

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    private CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开相机, 默认使用前置相机
     *
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback) {
        if (mCamera == null) {
            currentCameraId = findCamera(true);
            if (currentCameraId == -1) {
                currentCameraId = findCamera(false);

                Log.e(TAG, "寻找相机失败");

            }
            if (currentCameraId == -1) {}
            else {
                mCamera = Camera.open(currentCameraId);
                mCamera.setDisplayOrientation(0);//设置相机旋转度数

                Log.e(TAG, "打开相机了");
            }
            if (callback != null) {
                callback.cameraHasOpened();
            }
        } else {
            doStopCamera();
        }
    }

    /**
     * 寻找指定相机
     *
     * @param isfront
     * @return
     */
    private int findCamera(boolean isfront) {

        Log.e(TAG, "寻找相机");

        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (isfront) {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            } else {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            }

        }
        return -1;
    }

    /**
     * 切换相机
     *
     * @param callback
     */
    public void switchCamera(final SurfaceTexture surface, final float previewRate, final Camera.PreviewCallback callback) {
        if (currentCameraId != -1) {
            doStopCamera();
            if (currentCameraId == findCamera(true)) {
                currentCameraId = findCamera(false);
            } else {
                currentCameraId = findCamera(true);
            }
            if (currentCameraId != -1) {
                mCamera = Camera.open(currentCameraId);
                mCamera.setDisplayOrientation(90);
                if (callback != null) {
                    doStartPreview(surface, previewRate, callback);
                }
            } else {
            }
        } else {
            doOpenCamera(new CamOpenOverCallback() {
                @Override
                public void cameraHasOpened() {
                    doStartPreview(surface, previewRate, callback);
                }
            });
        }
    }

    /**
     * 切换相机
     *
     * @param callback
     */
    public void switchCamera(final SurfaceHolder surface, final float previewRate, final Camera.PreviewCallback callback) {
        if (currentCameraId != -1) {
            if (currentCameraId == findCamera(true)) {
                currentCameraId = findCamera(false);
            } else {
                currentCameraId = findCamera(true);
            }
            if (currentCameraId != -1) {
                doStopCamera();
                mCamera = Camera.open(currentCameraId);
                mCamera.setDisplayOrientation(90);
                if (callback != null) {
                    doStartPreview(surface, previewRate, callback);
                }
            } else {
            }
        } else {
            doOpenCamera(new CamOpenOverCallback() {
                @Override
                public void cameraHasOpened() {
                    doStartPreview(surface, previewRate, callback);
                }
            });
        }
    }

    /**
     * 通过Surfaceview显示相机预览画面
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate, Camera.PreviewCallback callback) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {

            Log.e(TAG, "mCamera不为空");

            try {
                mCamera.setPreviewDisplay(holder);

                Log.e(TAG, "通过surface显示预览画面了");
                mCamera.setPreviewCallback(callback);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }


    }

    /**
     * ʹ��TextureViewԤ��Camera
     *
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate, Camera.PreviewCallback callback) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surface);
                mCamera.setPreviewCallback(callback);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }

    /**
     * ֹͣԤ�����ͷ�Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * ����
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    private void initCamera(float previewRate) {
        if (mCamera != null) {
            try {
                mParams = mCamera.getParameters();
                Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(mParams.getSupportedPreviewSizes(), previewRate, 640);
                Log.d("log", String.valueOf(previewSize.width) + "-" + previewSize.height);
                //			pWidth = 1280;//320
                //		    pHeight = 720;//240
                pWidth = previewSize.width;
                pHeight = previewSize.height;
//			mParams.setPreviewSize(previewSize.width, previewSize.height);
                mParams.setPreviewSize(pWidth, pHeight);

                List<String> focusModes = mParams.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                mCamera.setParameters(mParams);
                mCamera.startPreview();

                isPreviewing = true;
                mPreviwRate = previewRate;

//				mParams = mCamera.getParameters();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    /*Ϊ��ʵ�����յĿ������������ձ�����Ƭ��Ҫ���������ص�����*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //���Ű��µĻص������������ǿ����������Ʋ��š����ꡱ��֮��Ĳ�����Ĭ�ϵľ������ꡣ
    {
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // �����δѹ��ԭ���ݵĻص�,����Ϊnull
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

        }
    };
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //��jpegͼ�����ݵĻص�,����Ҫ��һ���ص�
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //����ͼƬ��sdcard
            if (null != b) {
                //����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation", 90)ʧЧ��
                //ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
//                Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                FileUtil.saveBitmap(b);
            }
            //�ٴν���Ԥ��
            mCamera.startPreview();
            isPreviewing = true;
        }
    };

    public int getPreviewHeight() {
        return this.pHeight;
    }

    public int getPreviewWidth() {
        return this.pWidth;
    }

    /**
     * 显示当前相机运行的信息
     */
    public void showCameraInfo(Context context) {
        String info = "";
        if (isUsingFrontCamera()) {
            info += "Use Front Camera!\n";
        } else {
            info += "Use Back Camera!\n";
        }
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 是否使用前置摄像头
     *
     * @return
     */
    public boolean isUsingFrontCamera() {
        return currentCameraId >= 0 && currentCameraId == findCamera(true);
    }

}
