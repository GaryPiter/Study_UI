package net.dell.ui_actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //给左上角图标的左边加上一个返回的图标 ,对应ActionBar.DISPLAY_HOME_AS_UP
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //使左上角图标是否显示，如果设成false，则没有程序图标,对应ActionBar.DISPLAY_SHOW_HOME
        getActionBar().setDisplayShowHomeEnabled(true);

        //决定左上角的图标是否可以点击
        getActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this, "你点击了返回", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
