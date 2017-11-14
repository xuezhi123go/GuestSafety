package com.gkzxhn.xjyyzs.face.model;

/**
 * Created by cmlanche on 16/3/16.
 *
 * 一帧图的人脸
 */
public class FrameFaces {

    private FaceRect[] faceRects;

    public float[] getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(float[] landmarks) {
        this.landmarks = landmarks;
    }

    public float[] getFeature() {
        return feature;
    }

    public void setFeature(float[] feature) {
        this.feature = feature;
    }

    private float[] landmarks = new float[68 * 2];
    private float[] feature = new float[256];

    public String getProcess_Type() {
        return process_Type;
    }

    public void setProcess_Type(String process_Type) {
        this.process_Type = process_Type;
    }

    private String process_Type;

    public boolean isFeatureOk() {
        return isFeatureOk;
    }

    public void setFeatureOk(boolean featureOk) {
        isFeatureOk = featureOk;
    }

    private boolean isFeatureOk = false;

    public FrameFaces(FaceRect[] faceRects) {
        this.faceRects = faceRects;
    }

    public FaceRect[] getFaceRects() {
        return faceRects;
    }
    public void setFaceRects(FaceRect[] faceRects) {
        this.faceRects = faceRects;
    }
}
