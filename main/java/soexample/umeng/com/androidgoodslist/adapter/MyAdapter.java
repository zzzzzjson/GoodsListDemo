package soexample.umeng.com.androidgoodslist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import soexample.umeng.com.androidgoodslist.MyView;
import soexample.umeng.com.androidgoodslist.R;
import soexample.umeng.com.androidgoodslist.bean.MyBean;

public class MyAdapter extends BaseExpandableListAdapter {
    private ArrayList<MyBean.DataBean> list;
    private Context context;


    public MyAdapter(ArrayList<MyBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getSpus().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        groupViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_view, null);
            viewHolder = new groupViewHolder();
            viewHolder.Group_ck = convertView.findViewById(R.id.Group_ck);
            viewHolder.Group_title = convertView.findViewById(R.id.Group_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (groupViewHolder) convertView.getTag();
        }
        viewHolder.Group_title.setText(list.get(groupPosition).getName());
        boolean childAllCheck = isChildAllCheck(groupPosition);
        viewHolder.Group_ck.setChecked(childAllCheck);
        viewHolder.Group_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterCallback != null) {
                    adapterCallback.setGroupCheck(groupPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        childViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.child_view, null);
            viewHolder = new childViewHolder();
            viewHolder.Child_ck = convertView.findViewById(R.id.Child_ck);
            viewHolder.Child_img = convertView.findViewById(R.id.Child_img);
            viewHolder.Child_price = convertView.findViewById(R.id.Child_price);
            viewHolder.Child_title = convertView.findViewById(R.id.Child_title);
            viewHolder.myView = convertView.findViewById(R.id.My_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (childViewHolder) convertView.getTag();
        }
        viewHolder.Child_title.setText(list.get(groupPosition).getSpus().get(childPosition).getName() + "");
        viewHolder.Child_price.setText(list.get(groupPosition).getSpus().get(childPosition).getSkus().get(0).getPrice() + "");
        viewHolder.Child_ck.setChecked(list.get(groupPosition).getSpus().get(childPosition).isChildChecked());
        viewHolder.myView.setNumber(list.get(groupPosition).getSpus().get(childPosition).getPraise_num());
        Glide.with(context).load(list.get(groupPosition).getSpus().get(childPosition).getPic_url()).into(viewHolder.Child_img);
        viewHolder.Child_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterCallback != null) {
                    adapterCallback.setChildCheck(groupPosition, childPosition);
                }
            }
        });
        viewHolder.myView.setOnChange(new MyView.OnCountChange() {
            @Override
            public void setCount(int count) {
                if (adapterCallback!=null){
                    adapterCallback.setNumber(groupPosition,childPosition,count);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class groupViewHolder {
        CheckBox Group_ck;
        TextView Group_title;
    }

    class childViewHolder {
        CheckBox Child_ck;
        ImageView Child_img;
        TextView Child_title;
        TextView Child_price;
        MyView myView;
    }

    //因为点击Group和Child第CheckBox在主页面都需要刷新值所以做成接口回调
    //整个项目的用到的大致方法   接口回调会使用
    public interface AdapterCallback {
        //group的按钮
        void setGroupCheck(int groupPosition);

        //每个child的按钮点击事件
        void setChildCheck(int groupPosition, int childPosition);

        //加减的点击事件
        void setNumber(int groupPosition, int childPosition, int number);
    }

    private AdapterCallback adapterCallback;

    public void setCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    //点击group的CheckBox让所有child选中
    //联动 让child跟着group的状态
    public void childAllCheck(int groupPosition, boolean isChecked) {
        List<MyBean.DataBean.SpusBean> spus = list.get(groupPosition).getSpus();
        for (int i = 0; i < spus.size(); i++) {
            spus.get(i).setChildChecked(isChecked);
        }
    }

    //判断group是否被选中的状态
    public boolean isChildAllCheck(int groupPosition) {
        boolean boo = true;
        List<MyBean.DataBean.SpusBean> spus = list.get(groupPosition).getSpus();
        for (int i = 0; i < spus.size(); i++) {
            MyBean.DataBean.SpusBean spusBean = spus.get(i);
            if (!spusBean.isChildChecked()) {
                return false;
            }
        }
        return boo;
    }

    //点击单独child给他赋值
    public void setChildChecked(int groupPosition, int childPosition, boolean isChecked) {
        MyBean.DataBean.SpusBean spusBean = list.get(groupPosition).getSpus().get(childPosition);
        spusBean.setChildChecked(isChecked);
    }

    //判断单独的child的选中状态
    public boolean isChildChecked(int groupPosition, int childPosition) {
        MyBean.DataBean.SpusBean spusBean = list.get(groupPosition).getSpus().get(childPosition);
        if (spusBean.isChildChecked()) {
            return true;
        }
        return false;
    }

    //给商品数量进行赋值
    public void setGoodsNumber(int groupPosition, int childPosition, int number) {
        list.get(groupPosition).getSpus().get(childPosition).setPraise_num(number);
    }

    //判断底部视图全选全不选的商品状态
    public boolean isAllGoods() {
        boolean boo = true;
        for (int i = 0; i < list.size(); i++) {
            MyBean.DataBean dataBean = list.get(i);
            for (int j = 0; j < dataBean.getSpus().size(); j++) {
                MyBean.DataBean.SpusBean spusBean = dataBean.getSpus().get(j);
                if (!spusBean.isChildChecked()) {
                    boo = false;
                }
            }
        }
        return boo;
    }
    //实现全选全不选的功能
    public void setAllGood(boolean isChecked){
        for (int i = 0; i < list.size(); i++) {
            MyBean.DataBean dataBean = list.get(i);
            for (int j = 0; j < dataBean.getSpus().size(); j++) {
                dataBean.getSpus().get(j).setChildChecked(isChecked);
            }
        }
    }
    //计算所有商品的数量
    public int getAllGoodsNumber(){
        int allNumber=0;
        for (int i = 0; i < list.size(); i++) {
            MyBean.DataBean dataBean = list.get(i);
            for (int j = 0; j < dataBean.getSpus().size(); j++) {
                MyBean.DataBean.SpusBean spusBean = dataBean.getSpus().get(j);
                if (spusBean.isChildChecked()){
                    allNumber+=spusBean.getPraise_num();
                }
            }
        }
        return allNumber;
    }
    //计算所有商品的价格
    public float getAllGoodsPrice(){
        float allPrice=0;
        for (int i = 0; i < list.size(); i++) {
            MyBean.DataBean dataBean = list.get(i);
            for (int j = 0; j < dataBean.getSpus().size(); j++) {
                MyBean.DataBean.SpusBean spusBean = dataBean.getSpus().get(j);
                if (spusBean.isChildChecked()){
                    allPrice+=spusBean.getPraise_num()*Float.parseFloat(spusBean.getSkus().get(0).getPrice());
                }
            }
        }
        return allPrice;
    }
}
