package com.gkzxhn.xjyyzs.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colorreco.libface.CRFace;
import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.adapter.RecordAdapter;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.entities.BookResult;
import com.gkzxhn.xjyyzs.face.CameraSurfaceActivity;
import com.gkzxhn.xjyyzs.face.Config;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.StringUtils;
import com.gkzxhn.xjyyzs.view.CusTextView;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;
import com.hdos.idCardUartDevice.publicSecurityIDCardLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdx.HdxUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by Xuezhi
 * on 2017/5/24.
 * <p>
 * 人证识别Fragment
 */

public class GuestFragment extends BaseFragment {
    public static Bitmap mSurfaceFace;

    private static final String TAG = "GuestFragment";
    private final int CAMERA_SUFACE_RESULT = 0x10;//摄像头拍照

    /**
     * 身份证识别硬件相关参数
     */
    private byte[] name = new byte[32];
    private byte[] sex = new byte[6];
    private byte[] birth = new byte[18];
    private byte[] nation = new byte[12];
    private byte[] address = new byte[72];
    private byte[] Department = new byte[32];
    private byte[] IDNo = new byte[38];
    private byte[] EffectDate = new byte[18];
    private byte[] ExpireDate = new byte[18];
    private byte[] pErrMsg = new byte[20];
    private byte[] BmpFile = new byte[38556];

    String port = "/dev/ttyS3"; /*串口端口  tq210对应 /dev/s3c2410_serial0、/dev/s3c2410_serial1
                                                                                                        ttyMT2
															加利利串口0对应/dev/ttyS3*/

    private publicSecurityIDCardLib iDCardDevice;


    private String uuid;//身份证号

//    private ProgressDialog current_dialog;//读条dialog

    private SweetAlertDialog apply_dialog;//提示dialog

    @BindView(R.id.tv_id_name)
    TextView tv_id_name;
    @BindView(R.id.tv_id_sex)
    TextView tv_id_sex;
    @BindView(R.id.tv_id_nation)
    TextView tv_id_nation;
    @BindView(R.id.tv_id_birth)
    TextView tv_id_birth;
    @BindView(R.id.tv_id_address)
    TextView tv_id_address;
    @BindView(R.id.tv_id_no)
    TextView tv_id_no;
    @BindView(R.id.tv_id_department)
    TextView tv_id_department;
    @BindView(R.id.tv_id_date)
    TextView tv_id_date;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.tv_duibi)
    TextView tv_duibi;

    @BindView(R.id.tv_clear_record)
    ImageView tv_clear_record;
    @BindView(R.id.bt_start)
    Button bt_start;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerview_record)
    RecyclerView mRecyclerviewRecord;

    //    @BindView(R.id.bt_confirm)
//    Button bt_confirm;
    @BindView(R.id.tv_verfify_pass)
    CusTextView tv_verfify_pass;
    @BindView(R.id.tv_not_data)
    TextView tv_not_data;


    private RecordAdapter mRecordAdapter;


    private ByteBuffer buf_img2;
    private int w_img_2;
    private int extractRes;
    private int h_img_2;
    private float[] feaB = new float[256];
    private Bitmap bm;
    private JSONObject params = new JSONObject();
    private int compareResultCode = 0;
    private float similarity = -1;
    private String relateShip = null;
    private String prisionId = null;
    private Toast mToast;

    @Override
    protected View initView() {
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        View view = View.inflate(context, R.layout.fragment_guest, null);
        ButterKnife.bind(this, view);
        mRecordAdapter = new RecordAdapter(getActivity());
        mRecyclerviewRecord.setAdapter(mRecordAdapter);
        return view;
    }

    private void showToast(String showText) {
        if (mToast != null) {
            mToast.setText(showText);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
    }


    @Override
    protected void initData() {
        iDCardDevice = new publicSecurityIDCardLib();
        HdxUtil.SetIDCARDPower(1);
        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_IDCARD);


        CRFace.getInstance().loadLibrarySys("crface");

        int res = CRFace.getInstance().initSDK("/sdcard/colorreco");//res为1时初始化成功

        if (res == 1) {
            Config.isInitSuccess = true;
        }

        Log.e(TAG, "res = " + res);

        String uuid = StringUtils.getUUID(context);

        Log.e(TAG, "uuid=" + uuid);

//        ActivatorCallback callback=new ActivatorCallback() {
//            @Override
//            public void notifyMsg(int i) {
//
//            }
//        };
//        Activator.CreateActivator(getActivity(),"appkey","model",callback,"mac",true,true);
//        Activator.initSDK();

    }


    @OnClick({R.id.bt_start, R.id.tv_clear_record, R.id.tv_verfify_pass})
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.bt_start:
                    start();
                    break;
                case R.id.tv_clear_record:
                    mRecordAdapter.clear();
                    tv_not_data.setVisibility(View.VISIBLE);
                    mRecyclerviewRecord.setVisibility(View.GONE);
                    break;
                case R.id.tv_verfify_pass:
                    if (tv_verfify_pass.isShown()) {
                        changeVerfifyState(verfifyState.DEFAULT);
                        String confirmText = tv_verfify_pass.getText().toString();
                        if (confirmText.equals(getString(R.string.verify_success))) {
                            if (tv_not_data.isShown()) {
                                tv_not_data.setVisibility(View.GONE);
                                mRecyclerviewRecord.setVisibility(View.VISIBLE);
                            }
                            //验证通过
                            mRecordAdapter.loadItem(params.getString("name"), uuid,
                                    relateShip, prisionId);//记录到来访名单中
                            resetParams();
                        } else if (confirmText.equals(getString(R.string.verify_failed))) {
                            //点击重拍
                            if (compareResultCode != 200) {//上一次请求失败，重新请求
                                compareResultCode = 0;
                                similarity = -1;
                                duibi();
                            }
                            toCameraSurface();
                        }
                    }


                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置参数
     */
    private void resetParams() {
        compareResultCode = 0;
        similarity = -1;
        relateShip = null;
        prisionId = null;
        imageView.setImageResource(R.drawable.id_photo);
        imageView3.setImageResource(R.drawable.id_photo);
        tv_duibi.setText(R.string.compare_result_colon);
        tv_verfify_pass.setVisibility(View.GONE);

        tv_id_name.setText("姓名");
        tv_id_sex.setText("性别");
        tv_id_nation.setText("民族");
        tv_id_birth.setText("生日");
        tv_id_address.setText("地址");
        tv_id_no.setText("身份证号");
        tv_id_department.setText("发卡机构");
        tv_id_date.setText("有效日期");
//
    }

    private void toCameraSurface() {
        Intent intent = new Intent(getActivity(), CameraSurfaceActivity.class);
        intent.putExtra("feaB", feaB);
        startActivityForResult(intent, CAMERA_SUFACE_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bt_start.setEnabled(true);
        if (requestCode == CAMERA_SUFACE_RESULT) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                similarity = data.getFloatExtra("similarity", 0);
                if (mSurfaceFace == null) {
                    imageView3.setImageResource(R.drawable.id_photo);
                } else {
                    imageView3.setImageBitmap(mSurfaceFace);
                }
                if (compareResultCode == 200) {
                    showCompareResult();
                }
            } else {//点击返回不拍照
                progressBar.setVisibility(View.GONE);
                changeVerfifyState(verfifyState.FAILED);
            }
        }
    }

    enum verfifyState {
        DEFAULT, SUCCESS, FAILED
    }

    /**
     * 改变验证是否通过按钮的状态
     *
     * @param state
     */
    private void changeVerfifyState(verfifyState state) {
        bt_start.setEnabled(true);
        switch (state) {
            case DEFAULT:
                tv_verfify_pass.setVisibility(View.GONE);
                break;
            case SUCCESS:
                tv_verfify_pass.setVisibility(View.VISIBLE);
                tv_verfify_pass.setText(R.string.verify_success);
                tv_verfify_pass.setBackgroundResource(R.drawable.verfify_pass_selector);
//                tv_verfify_pass.setTextColor(getResources().getColor(R.color.pass_text_color));
                tv_verfify_pass.setLeftDrawable(getResources().getDrawable(R.drawable.pass, null));
                break;
            case FAILED:
                tv_verfify_pass.setVisibility(View.VISIBLE);
                tv_verfify_pass.setText(R.string.verify_failed);
                tv_verfify_pass.setBackgroundResource(R.drawable.verfify_error_selector);
//                tv_verfify_pass.setTextColor(getResources().getColor(R.color.error_text_color));
                tv_verfify_pass.setLeftDrawable(getResources().getDrawable(R.drawable.error, null));
                break;

        }
    }

    private void showCompareResult() {
        progressBar.setVisibility(View.GONE);
//        int CUSTOM_SIMILARITY= (int) SPUtil.get(getActivity(), Constant.CUSTOM_SIMILARITY,60);
//        if(similarity>=(float) CUSTOM_SIMILARITY/100){
        if (similarity >= 0.3) {
            tv_duibi.setText(getString(R.string.compare_result_colon) + getString(R.string.compare_successed));
            changeVerfifyState(verfifyState.SUCCESS);
        } else {
            tv_duibi.setText(getString(R.string.compare_result_colon) + getString(R.string.compare_failed));
            changeVerfifyState(verfifyState.FAILED);
        }
    }

    /**
     * 识别身份证的方法
     */
    private void start() {
        resetParams();
        bt_start.setEnabled(false);//不可点击
        progressBar.setVisibility(View.VISIBLE);
//        initShowProgressDialog();
        int retval;
        String pkName;
        pkName = getActivity().getPackageName();
        pkName = "/data/data/" + pkName + "/lib/libwlt2bmp.so";
        try {

            retval = iDCardDevice.readBaseMsg(port, pkName, BmpFile, name, sex, nation, birth, address, IDNo, Department,
                    EffectDate, ExpireDate, pErrMsg);
            Log.e(TAG, "retval" + retval);

            if (retval < 0) {
                progressBar.setVisibility(View.GONE);
                //showToastMsgShort("读卡错误，原因：" + new String(pErrMsg, "Unicode"));
                bt_start.setEnabled(true);//点击
//                showGetFailed("读卡错误，原因：" + new String(pErrMsg, "Unicode"));
                showToast("请将身份移开，重新放置感应区");//使用一个Toast,防止弹出多次
            } else {

                uuid = new String(IDNo, "Unicode");
                //        String idNo = "{\"uuid\":\"" + "PQ#&%D&BQ@*&%#*B%P" + "\"}";
                try {
                    params.put("name", new String(name, "Unicode").trim());
                    params.put("sex", new String(sex, "Unicode").trim());
                    params.put("nation", new String(nation, "Unicode").trim());
                    params.put("birthday", new String(birth, "Unicode").trim());
                    params.put("address", new String(address, "Unicode").trim());
                    params.put("uuid", StringUtils.getEncodedUuid(new String(IDNo, "Unicode")));
                    params.put("cardIssuer", new String(Department, "Unicode").trim());
                    params.put("validDate", new String(EffectDate, "Unicode").trim() + "至" + new String(ExpireDate, "Unicode").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                duibi();
                int[] colors = iDCardDevice.convertByteToColor(BmpFile);

                bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);

                //Bitmap bm1=Bitmap.createScaledBitmap(bm, (int)(102*1),(int)(126*1), false); //这里你可以自定义它的大小

                //imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setImageBitmap(bm);
                tv_id_name.setText("姓名：" + new String(name, "Unicode"));
                tv_id_sex.setText("性别：" + new String(sex, "Unicode"));
                tv_id_nation.setText("民族：" + new String(nation, "Unicode"));
                tv_id_birth.setText("生日：" + new String(birth, "Unicode"));
                tv_id_address.setText("地址：" + new String(address, "Unicode"));
                tv_id_no.setText("身份证号：" + new String(IDNo, "Unicode"));
                tv_id_department.setText("发卡机构：" + new String(Department, "Unicode"));
                tv_id_date.setText("有效日期：" + new String(EffectDate, "Unicode") + "至" + new String(ExpireDate, "Unicode"));
//                showGetSuccess();
                face();
                toCameraSurface();
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }


    /**
     * 对比身份证号的方法
     */
    private void duibi() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params.toString());
        RequestMethods.biduiID(body, new Subscriber<BookResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "对比失败 : " + e.getMessage());
                progressBar.setVisibility(View.GONE);
                //TODO
                changeVerfifyState(verfifyState.DEFAULT);
                tv_duibi.setText(getString(R.string.compare_result_colon) + getString(R.string.server_error));
            }

            @Override
            public void onNext(BookResult bookResult) {
                Log.e(TAG, "对比成功 ：" + bookResult);
                compareResultCode = bookResult.getCode();
                relateShip = bookResult.getRelationship();
//                if(Constant.IS_REMOTE_INTERVIEW)
//                    prisionId=bookResult.getPrisonerId();
                if (compareResultCode == 200) {
                    if (similarity >= 0) showCompareResult();
                } else {//TODO
                    progressBar.setVisibility(View.GONE);
                    tv_duibi.setText(getString(R.string.compare_result_colon) + bookResult.getMsg());
                }
            }
        });

    }


//    private void initShowProgressDialog() {
//        if (current_dialog == null) {
//            current_dialog = new ProgressDialog(getActivity());
//            current_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            current_dialog.setCancelable(false);
//            current_dialog.setCanceledOnTouchOutside(false);
//        }
//        current_dialog.show();
//    }

//    /**
//     * 读条结果：成功
//     */
//    private void showGetSuccess() {
//        if (current_dialog.isShowing())
//            current_dialog.dismiss();
//    }

//    /**
//     * 读条结果：失败
//     */
//    private void showGetFailed(String titleText) {
//        if (current_dialog.isShowing())
//            current_dialog.dismiss();
////        if(srl_refresh.isRefreshing())
////            srl_refresh.setRefreshing(false);
//        showToastMsgShort(titleText);
//    }

    /**
     * 抽取身份证头像特征的方法
     */
    private void face() {
        buf_img2 = ByteBuffer.allocate(bm.getWidth() * bm.getHeight() * 4);

        bm.copyPixelsToBuffer(buf_img2);

        w_img_2 = bm.getWidth();

        h_img_2 = bm.getHeight();

        extractRes = CRFace.getInstance().onepassExtractARGB(buf_img2.array(), w_img_2, h_img_2, feaB, 1);

        Log.e(TAG, "feaB = " + java.util.Arrays.toString(feaB));

    }

}
