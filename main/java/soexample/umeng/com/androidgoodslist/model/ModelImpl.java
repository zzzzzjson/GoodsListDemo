package soexample.umeng.com.androidgoodslist.model;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import soexample.umeng.com.androidgoodslist.bean.MyBean;
import soexample.umeng.com.androidgoodslist.callback.MyCallBack;
import soexample.umeng.com.androidgoodslist.utils.OkHttpUtils;

public class ModelImpl implements Model{
    private MyCallBack callBack;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str= (String) msg.obj;
            Gson gson = new Gson();
            MyBean myBean = gson.fromJson(str, MyBean.class);
            callBack.success(myBean);
        }
    };
    @Override
    public void getData(String url, final MyCallBack callBack) {
        this.callBack=callBack;
        OkHttpUtils.getInStance().post(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.error("异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                mHandler.sendMessage(mHandler.obtainMessage(0,string));
            }
        });
    }
}
