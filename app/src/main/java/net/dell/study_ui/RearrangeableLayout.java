package net.dell.study_ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by dell on 2016/4/13.
 * 重新排列布局
 */
public class RearrangeableLayout extends ViewGroup {

    //android.graphics.PointF是持有两个浮动坐标;
    private PointF mStartTouch;
    private View mSelectionChild;
    private Paint mSelectionPaint, mOutlinePaint;
    //点击的缩放
    private float mSelectionZoom;

    public RearrangeableLayout(Context context) {
        this(context, null);
    }

    public RearrangeableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RearrangeableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && p instanceof LayoutParams;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mStartTouch = null;
        mSelectionChild = null;
        //获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RearrangeableLayout);
        float width = ta.getDimension(R.styleable.RearrangeableLayout_outlineWidth, 2.0f);
        int color = ta.getColor(R.styleable.RearrangeableLayout_outlineColor, Color.GRAY);
        float alpha = ta.getFloat(R.styleable.RearrangeableLayout_selectionAlpha, 0.5f);
        mSelectionZoom = ta.getFloat(R.styleable.RearrangeableLayout_selectionZoom, 1.2f);
        ta.recycle();
        //设置矩阵颜色值
        float filter[] = new float[]{
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, alpha, 0f
        };
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(new ColorMatrix(filter));
        //
        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(width);
        mOutlinePaint.setColor(color);
        mOutlinePaint.setColorFilter(colorFilter);
        //
        mSelectionPaint = new Paint();
        mSelectionPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //
        width = Math.max(width, getMinimumWidth());
        height = Math.max(height, getMinimumHeight());

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            LayoutParams mp = (LayoutParams) view.getLayoutParams();
            view.measure(MeasureSpec.makeMeasureSpec(width - mp.leftMargin - mp.rightMargin, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(height - mp.topMargin - mp.bottomMargin, MeasureSpec.AT_MOST));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mSelectionChild == null) {
            doInitLayout(l, t, r, b, getChildCount());
        } else {
            layoutSelectedChild();
        }
    }

    /**
     * @param l
     * @param t
     * @param r
     * @param b
     * @param childCount
     */
    private void doInitLayout(int l, int t, int r, int b, int childCount) {
        int currentLeft = l;
        int currentTop = t;
        int prevChildBottom = -1;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LayoutParams mp = (LayoutParams) view.getLayoutParams();
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            int left, top, right, bottom;
            //
            if (view.getVisibility() != view.GONE && !mp.moved) {
                if (currentTop + height > b || l + width > r) {
                    Toast.makeText(getContext(), "Couldn't fit a child View, skipping it", Toast.LENGTH_SHORT)
                            .show();
                    continue;
                }
                if (currentLeft + width > r) {
                    left = l + mp.leftMargin;
                    currentTop = prevChildBottom;
                } else {
                    left = currentLeft + mp.topMargin;
                }
                top = currentTop + mp.topMargin;
                right = left + width;
                bottom = top + height;
                mp.left = left;
                mp.top = top;
                view.layout(left, top, right, bottom);
                //
                currentLeft = right + mp.rightMargin;
                prevChildBottom = bottom + mp.bottomMargin;
            } else if (mp.moved && view != mSelectionChild) {
                int x1 = Math.round(mp.left);
                int y1 = Math.round(mp.top);
                int x2 = Math.round(mp.left) + width;
                int y2 = Math.round(mp.top) + height;
                view.layout(x1, y1, x2, y2);
            }
        }
    }

    /**
     *
     */
    private void layoutSelectedChild() {
        LayoutParams lp = (LayoutParams) mSelectionChild.getLayoutParams();
        int l = Math.round(lp.left);
        int t = Math.round(lp.top);
        int r = l + mSelectionChild.getMeasuredWidth();
        int b = t + mSelectionChild.getMeasuredHeight();

        lp.moved = true;
        mSelectionChild.layout(l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mSelectionChild != null) {
            mSelectionChild.setVisibility(View.GONE);
        }
        super.dispatchDraw(canvas);

        if (mSelectionChild != null) {
            Rect rect = new Rect();
            mSelectionChild.getHitRect(rect);

            int restorePoint = canvas.save();
            canvas.scale(mSelectionZoom, mSelectionZoom, rect.centerX(), rect.centerY());
            canvas.drawRect(rect, mOutlinePaint);

            mSelectionChild.setDrawingCacheEnabled(true);
            Bitmap child = mSelectionChild.getDrawingCache();
            if (child != null) {
                LayoutParams lp = (LayoutParams) mSelectionChild.getLayoutParams();
                canvas.drawBitmap(child, lp.left, lp.top, mSelectionPaint);
            } else {
                mSelectionChild.draw(canvas);
            }
            canvas.restoreToCount(restorePoint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartTouch = null;
                mSelectionChild = findChildViewInsideTouch(Math.round(x), Math.round(y));
                if (mSelectionChild != null) {
                    bringChildToFront(mSelectionChild);
                    LayoutParams lp = (LayoutParams) mSelectionChild.getLayoutParams();
                    lp.initial = new PointF(lp.left, lp.top);
                    mStartTouch = new PointF(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mSelectionChild != null && mStartTouch != null) {
                    LayoutParams lp = (LayoutParams) mSelectionChild.getLayoutParams();
                    float dx = x - mStartTouch.x;
                    float dy = y - mStartTouch.y;

                    lp.left = lp.initial.x + dx;
                    if (lp.left < 0.0f) {
                        lp.left = 0.0f;
                    }

                    lp.top = lp.initial.y + dy;
                    if (lp.top < 0.0f) {
                        lp.top = 0.0f;
                    }

                    layoutSelectedChild();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                if (mSelectionChild != null) {
                    mSelectionChild.setVisibility(View.VISIBLE);
                    mSelectionChild = null;
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     *
     * @return selectedChild
     */
    private View findChildViewInsideTouch(int x, int y) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            Rect rect = new Rect();
            view.getHitRect(rect);
            if (rect.contains(x, y)) {
                return view;
            }
        }
        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 自定义内部类；
     */
    public static class LayoutParams extends MarginLayoutParams {
        float left;
        float top;
        PointF initial;
        boolean moved;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            left = -1.0f;
            top = -1.0f;
            initial = new PointF(0.0f, 0.0f);
            moved = false;
        }
    }
}
