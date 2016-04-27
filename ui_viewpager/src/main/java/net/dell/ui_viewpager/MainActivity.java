package net.dell.ui_viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private IMPagerAdapter imPagerAdapter;
    private List<String> titleList;
    private List<View>  layoutList;
    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加标题
        viewPager= (ViewPager) findViewById(R.id.vpager_main);
        pagerTabStrip= (PagerTabStrip) findViewById(R.id.pagertitle);
        titleList=new ArrayList<>();
        titleList.add("首页");
        titleList.add("分类");
        titleList.add("设置");
        //添加对应的布局
        layoutList=new ArrayList<>();
        LayoutInflater ll=getLayoutInflater();
        layoutList.add(ll.inflate(R.layout.layout_first, null, false));
        layoutList.add(ll.inflate(R.layout.layout_second, null, false));
        layoutList.add(ll.inflate(R.layout.layout_three, null, false));

        imPagerAdapter=new IMPagerAdapter(titleList, layoutList);
        viewPager.setAdapter(imPagerAdapter);
        viewPager.setCurrentItem(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
           startActivity(new Intent(this, TabActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
