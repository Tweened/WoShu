package com.toxicant.hua.woshu;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * okhttp的get和post异步的封装
 * Created by hua on 2015/12/15.
 */
public class OkhttpUitls {

    private OkHttpClient mOkHttpClient=new OkHttpClient();
    private static OkhttpUitls mInstance=null;
    //获取UI线程
    private Handler mHandler= new Handler(Looper.getMainLooper());
    //回调接口
    interface MOkCallBack{
        void onSuccess(String str);
        void onError();
    }
    //获取实例
    public static OkhttpUitls getInstance() {
        if (mInstance == null)
        {
            synchronized (OkhttpUitls.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkhttpUitls();
                }
            }
        }
        return mInstance;
    }

    /**
     * get方式进行网络访问
     * @param url 地址
     * @param mCallBack 回调
     */
    public void get(String url, final MOkCallBack mCallBack){
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String re=response.body().string();
                mHandler.post(new Runnable() {
                    public void run() {
                            mCallBack.onSuccess(re);
                    }
                });
            }
        });
    }//get

}
