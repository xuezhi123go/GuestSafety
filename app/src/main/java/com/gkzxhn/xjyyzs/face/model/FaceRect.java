package com.gkzxhn.xjyyzs.face.model;

import android.graphics.Rect;

/**
 * Created by cmlanche on 16/3/16.
 */
public class FaceRect {

    private int left;
    private int top;
    private int width;
    private int height;

    public FaceRect() {
    }

    public FaceRect(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 转换成系统的Rect
     * @return
     */
    public Rect toRect(){
        return new Rect(left, top, left+width, top+height);
    }

    @Override
    public String toString() {
        return "FaceRect{" +
                "left=" + left +
                ", top=" + top +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
