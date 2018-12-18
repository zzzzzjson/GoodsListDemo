package soexample.umeng.com.androidgoodslist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyView extends LinearLayout implements View.OnClickListener {
    private TextView delete_text;
    private TextView number_text;
    private TextView add_text;
    //定义商品数量
    private int mCount;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_view, this);
        initView();
    }

    private void initView() {
        delete_text=findViewById(R.id.delete_text);
        number_text=findViewById(R.id.number_text);
        add_text=findViewById(R.id.add_text);
        add_text.setOnClickListener(this);
        delete_text.setOnClickListener(this);
    }
    public void setNumber(int number){
      this.mCount=number;
      number_text.setText(number+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_text:
                mCount++;
                number_text.setText(mCount+"");
                if (countChange!=null){
                    countChange.setCount(mCount);
                }
                break;
            case R.id.delete_text:
                if (mCount>0){
                    mCount--;
                    number_text.setText(mCount+"");
                    if (countChange!=null){
                        countChange.setCount(mCount);
                    }
                }else {
                    Toast.makeText(getContext(), "商品已售空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public interface OnCountChange{
        void setCount(int count);
    }
    private OnCountChange countChange;
    public void setOnChange(OnCountChange countChange){
        this.countChange=countChange;
    }
}
