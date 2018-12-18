package soexample.umeng.com.androidgoodslist.model;

import soexample.umeng.com.androidgoodslist.callback.MyCallBack;

public interface Model {
    void getData(String url, MyCallBack callBack);
}
