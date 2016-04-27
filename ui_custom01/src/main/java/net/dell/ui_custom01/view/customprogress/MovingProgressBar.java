package net.dell.ui_custom01.view.customprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import net.dell.ui_custom01.R;

/**
 * Created by dell on 2016/4/14.
 */
public class MovingProgressBar extends View {
    private Paint mPaint;
    int progress = 0;
    int secondaryProgress = 0;
    private int progColor;
    private int secondProgDrawable;

    Bitmap bmp = null;
    Bitmap newbmp = null;
    Rect progRect = new Rect(0, 0, 0, 0);
    Handler handler = new Handler();

    public MovingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MovingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MovingProgressBar(Context context) {
        super(context);
        init(null);
    }

    /**
     * 初始化参数：自定义属性
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        this.mPaint = new Paint();
        if (attrs != null) {
            TypedArray typeArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MovingProgressBar, 0, 0);
            progColor = typeArray.getColor(R.styleable.MovingProgressBar_movingProgressColor, 0xFFFFFFFF);
            secondProgDrawable = typeArray.getResourceId(R.styleable.MovingProgressBar_secondProg, 0);
        }
    }

//    public void setSecondaryProgress(int prog) {
//        secondaryProgress = prog;
//        if (secondaryProgress == 0) {
//            handler.removeCallbacks(runSecondaryProgress);
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();
        int progWidth = width * progress / 100;
        int secondProgWidth = width * secondaryProgress / 100;

        mPaint.setColor(progColor);
        progRect.set(0, 0, progWidth, height);
        canvas.drawRect(progRect, mPaint);

        if (secondaryProgress > 0 && progress != 100) {
            if (newbmp == null) {
                bmp = BitmapFactory.decodeResource(this.getResources(), secondProgDrawable);
                Matrix matrix = new Matrix();
                float scaleHeight = ((float) height / bmp.getHeight());
                matrix.postScale(scaleHeight, scaleHeight);
                newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                bmp.recycle();
                bmp = null;
            }
            canvas.drawBitmap(newbmp, secondProgWidth - newbmp.getWidth(), 0, mPaint);
        }
        handler.removeCallbacks(runSecondaryProgress);
        if (progress != 100) {
            handler.postDelayed(runSecondaryProgress, 10);
        }
    }

    //    public void reDraw() {
//        invalidate();
//    }
    public void setProgress(int prog) {
        progress = prog;
        if (prog == 100) {
            invalidate();
        }
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 启动线程刷新进度
     */
    Runnable runSecondaryProgress = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            if (progress != 0) {
                secondaryProgress = (secondaryProgress + 1 + progress) % progress;
            } else {
                secondaryProgress = 0;
            }
            invalidate();
            if (progress != 100 && secondaryProgress != 0) {
                handler.postDelayed(this, 20);
            }
        }
    };
}
