package soexample.umeng.com.androidgoodslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import soexample.umeng.com.androidgoodslist.adapter.MyAdapter;
import soexample.umeng.com.androidgoodslist.base.BaseActivity;
import soexample.umeng.com.androidgoodslist.bean.MyBean;
import soexample.umeng.com.androidgoodslist.presenter.PresenterIMpl;
import soexample.umeng.com.androidgoodslist.view.IView;

public class MainActivity<T> extends BaseActivity implements IView<T> {

    private ExpandableListView Expand_able;
    private CheckBox Check_box;
    private TextView Count_sum;
    private TextView Count_price;
    private PresenterIMpl presenterIMpl;
    private ArrayList<MyBean.DataBean> list = new ArrayList<>();
    private MyAdapter myAdapter;
    private String mUrl = "http://www.wanandroid.com/tools/mockapi/6523/restaurant-list";

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Expand_able = (ExpandableListView) findViewById(R.id.Expand_able);
        Check_box = (CheckBox) findViewById(R.id.Check_box);
        Count_sum = (TextView) findViewById(R.id.Count_sum);
        Count_price = (TextView) findViewById(R.id.Count_price);
        //去掉自带的箭头
        Expand_able.setGroupIndicator(null);
    }

    @Override
    protected void setOnClick() {
        Check_box.setOnClickListener(this);
    }

    @Override
    protected void processLogin() {
        presenterIMpl = new PresenterIMpl(this);
        myAdapter = new MyAdapter(list, this);
        Expand_able.setAdapter(myAdapter);
        presenterIMpl.startRequest(mUrl);
        myAdapter.setCallback(new MyAdapter.AdapterCallback() {
            //组的是否被选中
            @Override
            public void setGroupCheck(int groupPosition) {
                //判断group的状态
                boolean childAllCheck = myAdapter.isChildAllCheck(groupPosition);
                myAdapter.childAllCheck(groupPosition, !childAllCheck);
                myAdapter.notifyDataSetChanged();
                flushButtonLayout();
            }

            @Override
            public void setChildCheck(int groupPosition, int childPosition) {
                //判断child的状态
                boolean childChecked = myAdapter.isChildChecked(groupPosition, childPosition);
                myAdapter.setChildChecked(groupPosition, childPosition, !childChecked);
                myAdapter.notifyDataSetChanged();
                flushButtonLayout();
            }

            @Override
            public void setNumber(int groupPosition, int childPosition, int number) {
                myAdapter.setGoodsNumber(groupPosition, childPosition, number);
                myAdapter.notifyDataSetChanged();
                flushButtonLayout();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Check_box:
            //判断底部全选全不选的状态
            boolean allGoods = myAdapter.isAllGoods();
            myAdapter.setAllGood(!allGoods);
            myAdapter.notifyDataSetChanged();
            flushButtonLayout();
            break;
        }
    }

    @Override
    public void success(T user) {
        MyBean data = (MyBean) user;
        list.addAll(data.getData());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(T error) {
        Toast.makeText(this, error + "", Toast.LENGTH_SHORT).show();
    }

    //释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenterIMpl != null) {
            presenterIMpl.onDetach();
        }
    }

    private void flushButtonLayout() {
        //上下checkbox联动
        boolean allGoods = myAdapter.isAllGoods();
        Check_box.setChecked(allGoods);
        int allGoodsNumber = myAdapter.getAllGoodsNumber();
        float allGoodsPrice = myAdapter.getAllGoodsPrice();
        Count_price.setText("合计："+allGoodsPrice);
        Count_sum.setText("去结算（" + allGoodsNumber + "）");
    }
}
