package net.dell.ui_viewflipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

    private final static int MIN_MOVE = 200;
    private Context context;
    private MyGestureListener gestureListener;
    private GestureDetector detector;
    private ViewFlipper viewFlipper;
    private Button button;
    private int[] resId = {
            R.mipmap.bg18_fog_night,
            R.mipmap.bg_fog_day,
            R.mipmap.bg_moderate_rain_day,
            R.mipmap.bg_snow_night,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        //实例化对象
        gestureListener = new MyGestureListener();
        detector = new GestureDetector(this, gestureListener);
        //初始化控件，动态导入子View
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        button = (Button) findViewById(R.id.btn_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FromtoRightActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });
        for (int i = 0; i < resId.length; i++) {
            viewFlipper.addView(getImageView(resId[i]));
        }
    }

    private ImageView getImageView(int resId) {
        ImageView img = new ImageView(this);
        img.setBackgroundResource(resId);
        return img;
    }

    /**
     * 触发监听事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    /**
     * 监听事件
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > MIN_MOVE) {
                viewFlipper.setInAnimation(context, R.anim.right_in);
                viewFlipper.setOutAnimation(context, R.anim.right_out);
                viewFlipper.showNext();
            } else if (e2.getX() - e1.getX() > MIN_MOVE) {
                viewFlipper.setInAnimation(context, R.anim.left_in);
                viewFlipper.setOutAnimation(context, R.anim.left_out);
                viewFlipper.showPrevious();
            }
            return true;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent intent = new Intent(context, MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//        return super.onKeyDown(keyCode, event);
//    }
}
