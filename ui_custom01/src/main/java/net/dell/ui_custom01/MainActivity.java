package net.dell.ui_custom01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView roteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //旋转图片
        roteView = (ImageView) findViewById(R.id.rote_icon);
        startRotate();
    }

    /**
     * 启动动画
     */
    private void startRotate() {
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotating_forever);
        roteView.setAnimation(rotateAnim);
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnim.setInterpolator(lir);
        rotateAnim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.radar_item:
                startActivity(new Intent(this, RadarActivity.class));
                break;
            case R.id.water_item:
                startActivity(new Intent(this, WaterProgressActivity.class));
                break;
            case R.id.windows_item:
                startActivity(new Intent(this, WindowsCopyResStyleProgressActivity.class));
                break;
            case R.id.custom_status_item:
                startActivity(new Intent(this, CombinationlStatusActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
