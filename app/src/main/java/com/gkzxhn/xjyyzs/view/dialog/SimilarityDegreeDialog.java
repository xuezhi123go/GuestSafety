package com.gkzxhn.xjyyzs.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.utils.SPUtil;

/**
 * Created by Raleigh.Luo on 17/6/8.
 */

public class SimilarityDegreeDialog extends Dialog {
    private SeekBar mSeekBar;
    private TextView tvValue;
    public SimilarityDegreeDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.similarity_degree_layout);
        mSeekBar= (SeekBar) findViewById(R.id.similarity_degree_layout_seekBar);

        tvValue= (TextView) findViewById(R.id.similarity_degree_layout_tv_value);
        TextView tvConfirm= (TextView) findViewById(R.id.similarity_degree_layout_tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtil.put(getContext(), Constant.CUSTOM_SIMILARITY,mSeekBar.getProgress());
                dismiss();
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int simlary = (int) SPUtil.get(getContext(), Constant.CUSTOM_SIMILARITY, 60);
        mSeekBar.setProgress((int) simlary);
        tvValue.setText(String.valueOf(simlary));
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        WindowManager m = dialogWindow.getWindowManager();

        Display d = m.getDefaultDisplay();
        params.width = d.getWidth()/2;
        //	        params.height=d.getHeight();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(params);
    }

    @Override
    public void show() {
        if(mSeekBar!=null) {
            int simlary = (int) SPUtil.get(getContext(), Constant.CUSTOM_SIMILARITY, 60);
            mSeekBar.setProgress(simlary);
            tvValue.setText(String.valueOf(simlary));
        }
        super.show();
    }
}
