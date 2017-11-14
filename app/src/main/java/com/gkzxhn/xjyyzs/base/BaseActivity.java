package com.gkzxhn.xjyyzs.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.app.AppManager;
import com.gkzxhn.xjyyzs.utils.ToastUtil;


/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:Activity基类、所有Activity须继承此类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private View ly_title_bar;
    private RelativeLayout rl_content;// 标题以下内容布局
    protected Toolbar tool_bar; // toolbar
    private TextView tv_title;// 标题
    private ImageButton ib_back;// 返回
//    protected Button bt_logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.root_view);

        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉信息栏

        ly_title_bar = findViewById(R.id.ly_title_bar);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
//        bt_logout = (Button) findViewById(R.id.bt_logout);


        View view = initView();// 初始化view
        if(rl_content.getChildCount() != 0) {
            rl_content.removeAllViews();
        }

        rl_content.addView(view);

        //全沉浸式布局
        /*getWindow().getDecorView().setSystemUiVisibility(
                 View.SYSTEM_UI_FLAG_IMMERSIVE
               // |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
               // |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                //|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//不挡布局
               |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//挡布局
               // |View.SYSTEM_UI_FLAG_FULLSCREEN
               // |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                );*/

        initData();// 初始化数据操作
    }


    @Override
    protected void onDestroy() {
        AppManager.getInstance().finishActivity(this);
        super.onDestroy();
    }

    /**
     * 子类必须重写
     * @return
     */
    protected abstract View initView();

    protected abstract void initData();

    /**
     * 隐藏标题栏
     */
    protected void removeTitleBar(){
        ly_title_bar.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     * @param title
     */
    protected void setTitleText(String title){
        tv_title.setText(title);
    }

    /**
     * 设置标题
     * @param title_ResID
     */
    public void setTitleText(int title_ResID){
        tv_title.setText(title_ResID);
    }

    /**
     * 显示返回按钮
     */
    protected void setBackVisibility(){
        ib_back.setVisibility(View.VISIBLE);
    }

    /**
     * 设置返回按钮监听
     * @param listener
     */
    protected void setBackPressListener(View.OnClickListener listener){
        ib_back.setOnClickListener(listener);
    }

    /**
     * short toast
     * @param msg
     */
    protected void showToastShortMsg(String msg){
        ToastUtil.showShortToast(this, msg);
    }

    /**
     * long toast
     * @param msg
     */
    protected void showToastLongMsg(String msg){
        ToastUtil.showLongToast(this, msg);
    }


    /**
     * 设置
     */

}
