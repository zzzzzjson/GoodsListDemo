package soexample.umeng.com.androidgoodslist.view;

public interface IView<T> {
    void success(T user);
    void error(T error);
}
