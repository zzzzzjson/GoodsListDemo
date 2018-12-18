package soexample.umeng.com.androidgoodslist.presenter;

import soexample.umeng.com.androidgoodslist.callback.MyCallBack;
import soexample.umeng.com.androidgoodslist.model.ModelImpl;
import soexample.umeng.com.androidgoodslist.view.IView;

public class PresenterIMpl implements Presenter{
    private ModelImpl model;
    private IView iView;

    public PresenterIMpl(IView iView) {
        this.iView = iView;
        model=new ModelImpl();
    }

    @Override
    public void startRequest(String url) {
        model.getData(url, new MyCallBack() {
            @Override
            public void success(Object user) {
                iView.success(user);
            }

            @Override
            public void error(Object error) {
               iView.error(error);
            }
        });
    }
    public void onDetach(){
        if (iView!=null){
            iView=null;
        }
        if (model!=null){
            model=null;
        }
    }
}
