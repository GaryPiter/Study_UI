package net.dell.ui_drawerlayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 思考？怎么设置点击图标显示左侧，怎么默认显示第一个或某一个
 */
public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private List<Item> data;
    private MAdapter<Item> adapter;
    private Boolean isDirection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.listview_main);

        data = new ArrayList<>();
        data.add(new Item(R.mipmap.ic_launcher, "实时路况"));
        data.add(new Item(R.mipmap.ic_launcher, "天气预报"));
        data.add(new Item(R.mipmap.ic_launcher, "后车倒影"));
        data.add(new Item(R.mipmap.ic_launcher, "智能导航"));
        data.add(new Item(R.mipmap.ic_launcher, "辅助刹车"));

        adapter = new MAdapter<Item>(data, R.layout.item_list) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setImageResource(R.id.image_icon, obj.getIconId());
                holder.setText(R.id.text_item, obj.getIconName());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == listView) {
                    isDirection = true;
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == listView) {
                    isDirection = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //设置默认显示值
        if (savedInstanceState == null) {
            selectItem(3);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //可扩展功能：一个Item对应一个Fragment
        selectItem(position);
    }

    /**
     * 显示的内容
     * @param position
     */
    private void selectItem(int position) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString("text", data.get(position).getIconName());
        contentFragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_main, contentFragment).commit();
        drawerLayout.closeDrawer(listView);//点击之后关闭
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
            if (!isDirection) {
                drawerLayout.openDrawer(listView);
            } else {
                drawerLayout.closeDrawer(listView);
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
