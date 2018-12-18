package soexample.umeng.com.androidgoodslist.utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {

    private  OkHttpClient okHttpClient;

    private OkHttpUtils(){
        okHttpClient = new OkHttpClient();
    }
    public static OkHttpUtils getInStance(){
        return OkHttpHolder.utils;
    }
    static class OkHttpHolder{
        private static final OkHttpUtils utils=new OkHttpUtils();
    }
    public void post(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
