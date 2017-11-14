package com.gkzxhn.xjyyzs.requests.methods;

import android.content.Context;

import com.gkzxhn.xjyyzs.entities.BookResult;
import com.gkzxhn.xjyyzs.requests.ApiService;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;
import com.gkzxhn.xjyyzs.requests.bean.SearchResultBean;
import com.gkzxhn.xjyyzs.requests.bean.UpdateInfo;
import com.gkzxhn.xjyyzs.utils.SPUtil;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.gkzxhn.xjyyzs.requests.Constant.BASE_NEW_APK;
import static com.gkzxhn.xjyyzs.requests.Constant.URL_HEAD;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/7.
 * description:请求方法封装类
 */

public class RequestMethods {

    /**
     * 登录
     * // * @param body
     *
     * @param subscriber
     */
    public static void login(RequestBody body, Subscriber<LoginResult> subscriber) {


        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .login(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 预约会见
     *
     * @param token
     * @param body
     * @param subscriber
     */
    public static void bookMeeting(String token, RequestBody body, Subscriber<BookResult> subscriber) {

        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .apply(token, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码和设置工作人员手机号都是此方法
     *
     * @param token
     * @param body
     * @param subscriber
     */
    public static void setNumber(String token, RequestBody body, Subscriber<ResponseBody> subscriber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .changePwd(token, token, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取查询结果
     *
     * @param context
     * @param map
     * @param subscriber
     */
    public static void getSearchResult(Context context, Map<String, String>
            map, Subscriber<SearchResultBean> subscriber) {

        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(URL_HEAD)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService searchByTime = retrofit.create(ApiService.class);
        String token = SPUtil.get(context, "token", "") + "";
        String orgCode = SPUtil.get(context, "organizationCode", "") + "";
        searchByTime
                .searchByTime(token, map, orgCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 检查更新
     *
     * @param subscriber
     */
    public static void checkUpdate(Subscriber<UpdateInfo> subscriber) {
        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_NEW_APK)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .updateCheck()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 比对身份证方法
     * @param body
     * @param subscriber
     */
    public static void biduiID(RequestBody body, Subscriber<BookResult> subscriber) {

        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_NEW_APK)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .biduiID(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * 比对身份证方法
     * @param body
     * @param subscriber
     */
    public static void remoteInterview(RequestBody body, Subscriber<Object> subscriber) {

        OkHttpClient httpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置拦截器级别
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        httpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_NEW_APK)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService
                .remoteInterview(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
