package soexample.umeng.com.androidgoodslist.callback;

public interface MyCallBack<T> {
    void success(T user);
    void error(T error);
}
