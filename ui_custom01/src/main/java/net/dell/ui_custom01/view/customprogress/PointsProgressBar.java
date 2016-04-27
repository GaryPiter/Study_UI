package net.dell.ui_custom01.view.customprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import net.dell.ui_custom01.R;

/**
 * Created by dell on 2016/4/14.
 */
public class PointsProgressBar extends View {

    private int pointsCount;
    private Bitmap selectBm;
    private Bitmap unselectBm;
    private int currentSelectIndex;
    private Paint paint;
    private boolean isStop = false;
    private Thread pbThread;

    public PointsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PointsProgressBar(Context context) {
        super(context);
    }

    public PointsProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.points_p_bar);

        Drawable selectDrawable = typeArray.getDrawable(R.styleable.points_p_bar_selected_src);
        selectDrawable = selectDrawable == null ? getResources().getDrawable(
                R.mipmap.radar_back) : selectDrawable;
        selectBm = ((BitmapDrawable) selectDrawable).getBitmap();

        //长和宽放大缩小的比例
        Matrix matrix = new Matrix();
        matrix.postScale(1.5f, 1.5f);
        selectBm = Bitmap.createBitmap(selectBm, 0, 0, selectBm.getWidth(), selectBm.getHeight(), matrix, true);

        Drawable unselectDrawable = typeArray.getDrawable(R.styleable.points_p_bar_unselected_src);
        unselectDrawable = unselectDrawable == null ? getResources().getDrawable(
                R.mipmap.radar_back) : unselectDrawable;
        unselectBm = ((BitmapDrawable) unselectDrawable).getBitmap();
        //定时更新选中点位置
        pointsCount = typeArray.getInt(R.styleable.points_p_bar_point_count, 5);
        typeArray.recycle();
    }

    /**
     * 定时改变选择的点
     *
     * @author haihai.zhang
     */
    private Runnable timer = new Runnable() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && !isStop) {
                if (currentSelectIndex < pointsCount - 1) {
                    currentSelectIndex++;
                } else {
                    currentSelectIndex = 0;
                }
                try {
                    PointsProgressBar.this.postInvalidate();
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        paint.setColor(Color.YELLOW);
        canvas.save();
        int divider = width / pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            if (currentSelectIndex == i) {
                canvas.drawBitmap(selectBm, i * divider + divider / 2, selectBm.getWidth() / 2 - unselectBm.getWidth() / 2, new Paint());
            } else {
                canvas.drawBitmap(unselectBm, i * divider + divider / 2, unselectBm.getWidth() / 2, new Paint());
            }
        }
        canvas.save();
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度
        int width = 720;
        //设置高度为点大小
        int height = selectBm.getHeight();
        PointsProgressBar.this.postInvalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void stop() {
        isStop = true;
    }

    public void start() {
        this.setVisibility(View.VISIBLE);// 设置可见
        pbThread = new Thread(timer);// 雷达扫描线程
        pbThread.start();
    }
}
