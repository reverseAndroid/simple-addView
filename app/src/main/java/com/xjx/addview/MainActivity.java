package com.xjx.addview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLinearLayout;
    private Button mButtonAdd, mButtonGet;
    private TextView mTextView1, mTextView2;
    private EditText mEditText1, mEditText2;
    private View mView;
    //标记这是第几次添加的item,也是作为一个tag来使用
    private int position;
    //存储添加的View
    private Map<Integer, View> mapView = new HashMap<>();
    private List<String> editText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.main_linear);
        mButtonAdd = (Button) findViewById(R.id.main_btn_add);
        mButtonAdd.setOnClickListener(this);

        mButtonGet = (Button) findViewById(R.id.main_btn_get);
        mButtonGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_add:
                addItemView();
                break;

            case R.id.main_btn_get:
                //通过getEditItem的返回值，判断程序是否继续往下走
                boolean b = getEditItem();
                if (!b) {
                    return;
                }
                Toast.makeText(this, "我是editText，我存了" + editText.size() + "个EditText", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //动态添加一个item的方法
    private void addItemView() {
        mView = new View(MainActivity.this);
        mView = View.inflate(this, R.layout.item_main, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mTextView1 = mView.findViewById(R.id.item_tv);
        mEditText1 = mView.findViewById(R.id.item_et);
        mTextView2 = mView.findViewById(R.id.item_tv1);
        mEditText2 = mView.findViewById(R.id.item_et1);
        mLinearLayout.addView(mView, params);
        //给当前添加的View设置一个tag,这一步很重要。如果没有这个tag，我们后面无法取出view
        mView.setTag(position);
        //将tag和当前生成的view存入集合
        mapView.put(position, mView);
        //让tag自增一下，因为tag在这还承担着当前view序号的作用
        position++;
        mTextView1.setText("我是第" + position + "条" + "第1个TextView");
        mTextView2.setText("我是第" + position + "条" + "第2个TextView");
        //设置长按删除
        setLongDelete();
    }

    private void setLongDelete() {
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //根据点击的当前View，获取上面存储的tag，这个view就是onLongClick（View view）里的这个view，直接拿来用，他可以判断出当前点击的哪一个view
                                int positionView = (int) view.getTag();
                                //通过map集合把刚获得的tag放进去，从而获取到当前要删除的view
                                mLinearLayout.removeView(mapView.get(positionView));
                                //移除掉这个tag
                                mapView.remove(positionView);
                                //当map集合删除掉最后一条时，会初始化tag和view
                                if (mapView.size() == 0) {
                                    position = 0;
                                    editText.clear();
                                    mLinearLayout.removeAllViews();
                                }
                                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
                return false;
            }
        });
    }

    private boolean getEditItem() {
        //getChildCount()此方法可以获取到当前的linearLayout里含有多少个view
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            //将每一个view遍历出来，然后findViewById后，就可以获取EditText的值
            View childAtView = mLinearLayout.getChildAt(i);
            mEditText1 = childAtView.findViewById(R.id.item_et);
            mEditText2 = childAtView.findViewById(R.id.item_et1);
            if (!mEditText1.getText().toString().equals("") && !mEditText2.getText().toString().equals("")) {
                editText.add(mEditText1.getText().toString());
                editText.add(mEditText2.getText().toString());
            } else {
                Toast.makeText(this, "第" + (i + 1) + "个item的EditText没有完整填写", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
