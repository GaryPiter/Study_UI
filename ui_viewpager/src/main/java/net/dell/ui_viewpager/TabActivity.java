package net.dell.ui_viewpager;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView titleHome, titleType, titleSetup;
    private ViewPager viewPager;
    private ImageView imageCursor;
    private List<View> layoutList;
    private int currIndex = 0;//当前页面的编号
    private int offset = 0;//移动条图片的偏移量
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离，比如1->2,或者2->3
    private int two = 0; //滑动条移动两页的距离，比如1直接跳3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
    }

    private void initView() {
        titleHome = (TextView) findViewById(R.id.title_home);
        titleType = (TextView) findViewById(R.id.title_type);
        titleSetup = (TextView) findViewById(R.id.title_setup);
        imageCursor = (ImageView) findViewById(R.id.image_cursor);
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        //设置图标，获取下划线的宽度
        bmpWidth= BitmapFactory.decodeResource(getResources(),R.mipmap.line).getWidth();
        //获取手机分辨率的宽度
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //计算偏移量：（屏幕宽度/选项卡个数-下划线图片的宽度）/2
        offset=(width/3-bmpWidth)/2;
        Matrix matrix=new Matrix();
        matrix.postTranslate(offset, 0);
        //设置初始位置
        imageCursor.setImageMatrix(matrix);
        //设置移动的距离
        one=offset*2+bmpWidth;
        two=one*2;

        //添加对应的布局
        layoutList=new ArrayList<>();
        LayoutInflater ll=getLayoutInflater();
        layoutList.add(ll.inflate(R.layout.layout_first, null, false));
        layoutList.add(ll.inflate(R.layout.layout_second, null, false));
        layoutList.add(ll.inflate(R.layout.layout_three, null, false));
        viewPager.setAdapter(new TabPagerAdapter(layoutList));
        viewPager.setCurrentItem(0);//设置默认显示页

        titleHome.setOnClickListener(this);
        titleSetup.setOnClickListener(this);
        titleType.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.title_type:
                viewPager.setCurrentItem(1);
                break;
            case R.id.title_setup:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation animation = null;
        switch (position) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
        }
        currIndex = position;
        animation.setFillAfter(true);// true表示图片停在动画结束位置
        animation.setDuration(300); //设置动画时间为300毫秒
        imageCursor.startAnimation(animation);//开始动画
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
