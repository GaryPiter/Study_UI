package net.dell.ui_sufaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dell on 2016/4/15.
 */
public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawingThread mDrawingThread;

    public MSurfaceView(Context context) {
        super(context);
        init();
    }

    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 监听surfaceview的生命周期
     */
    private void init() {
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.mDrawingThread.addItem((int) event.getX(), (int) event.getY());
        }
        return super.onTouchEvent(event);
    }

    /**
     * 创建时，开启线程,加载图片
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap map= BitmapFactory.decodeResource(getResources(), R.mipmap.comment_user_weibo_icon);
        mDrawingThread = new DrawingThread(holder, map);
        mDrawingThread.start();
    }

    /**
     * 发生改变调用
     * 更新绘制图片的运行范围
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mDrawingThread.updateSize(width, height);
    }

    /**
     * 销毁时，停止线程，停止绘制
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mDrawingThread.quit();
        this.mDrawingThread = null;
    }

    public void clear() {
        this.mDrawingThread.clearItems();
    }

    /**
     * 自定义线程类
     */
    private static class DrawingThread extends HandlerThread implements Handler.Callback {

        //添加图片条目
        private static final int MSG_ADD = 100;
        //点击消息（点击图片移动标识）
        private static final int MSG_MOVE = 101;
        //清理图片标识
        private static final int MSG_CLEAR = 102;
        //定义surfaceView宽和高
        private int mDrawingWidth, mDrawingHeight;
        //缓存视图
        private SurfaceHolder mDrawingHolder;
        //定义图片的画笔
        private Paint mPaint;
        //要绘制的图片
        private Bitmap mIcon;
        //定义要显示的图片对象数组
        private List<DrawingItem> mLocations;
        //更新主线程UI(包括平移，透明度，缩放。。。)
        private Handler mReveiver;
        //是否正在运行
        private boolean isRunning;

        public DrawingThread(SurfaceHolder surfaceHolder, Bitmap bitmap) {
            super("DrawingThread");
            this.mDrawingHolder=surfaceHolder;
            this.mIcon=bitmap;
            this.mLocations=new ArrayList<>();
            this.mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD:
                    Random random = new Random();
                    DrawingItem drawingItem = new DrawingItem(msg.arg1, msg.arg2, random.nextInt(1) == 0, random.nextInt(1) == 0);
                    mLocations.add(drawingItem);
                    break;
                case MSG_MOVE://绘制图片
                    if (!isRunning) {
                        return true;
                    }
                    //加锁的画布
                    Canvas canvas = mDrawingHolder.lockCanvas();
                    if (canvas == null) {
                        break;
                    }
                    //首先清空画布
                    canvas.drawColor(Color.BLACK);
                    for (DrawingItem item : mLocations) {
                        //思考？如何让图片动起来
                        //解决方案：循环改变图片显示坐标
                        item.x += item.isHorizontal ? 5 : -5;
                        if (item.x >= this.mDrawingWidth - mIcon.getWidth()) {
                            item.isHorizontal = false;
                        } else {
                            item.isHorizontal = true;
                        }
                        item.y += item.isVertical ? 5 : -5;
                        if (item.y >= this.mDrawingHeight - mIcon.getHeight()) {
                            item.isVertical = false;
                        } else {
                            item.isVertical = true;
                        }
                        //绘制图片
                        if (item.isHorizontal || item.isVertical) {
                            canvas.drawBitmap(mIcon, item.x, item.y, mPaint);
                        }else{

                        }
                    }
                    // 解锁画布
                    mDrawingHolder.unlockCanvasAndPost(canvas);
                    break;
                case MSG_CLEAR://清空所以的图片
                    mLocations.clear();
                    break;
            }
            //如果线程正在运行，那就持续不断的发送消息
            if (isRunning) {
                mReveiver.sendEmptyMessage(MSG_MOVE);
            }
            return false;
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            //线程开始的时候我们准备基本的操作，初始化
            this.mReveiver = new Handler(getLooper(), this);
            //默认运行线程
            isRunning = true;
            //默认创建图片对象
            this.mReveiver.sendEmptyMessage(MSG_ADD);
        }

        @Override
        public boolean quit() {
            isRunning = false;
            mReveiver.removeCallbacksAndMessages(null);
            return super.quit();
        }

        /**
         * 更新surfaceView的宽高
         */
        public void updateSize(int width, int height) {
            this.mDrawingWidth = width;
            this.mDrawingHeight = height;
        }

        /**
         * 添加图片条目的方法
         */
        public void addItem(int x, int y) {
            Message msg = Message.obtain(mReveiver, MSG_ADD, x, y);
            mReveiver.sendMessage(msg);
        }

        /**
         * 清空
         */
        public void clearItems() {
            mReveiver.sendEmptyMessage(MSG_CLEAR);
        }

        /**
         * 图片的对象类
         */
        private class DrawingItem {
            //图片的坐标显示
            int x, y;
            //图片运动的方向
            boolean isHorizontal, isVertical;

            public DrawingItem(int x, int y, boolean isHorizontal, boolean isVertical) {
                this.x = x;
                this.y = y;
                this.isHorizontal = isHorizontal;
                this.isVertical = isVertical;
            }
        }
    }// end class
}
