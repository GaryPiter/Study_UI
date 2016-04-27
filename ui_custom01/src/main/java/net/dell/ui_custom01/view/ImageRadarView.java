package net.dell.ui_custom01.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.dell.ui_custom01.R;

/**
 * @author 雷达扫描
 **/
public class ImageRadarView extends View {

    private Thread radarSweepThread;
    private int degree;
    private Paint circlePaint;
    private Paint linePaint;
    private Paint sweepPaint;
    private SweepGradient sweepGradient;
    private Paint foundPoitPaint;

    private Bitmap radar_top_src;
    private Bitmap radar_border_src;
    private Bitmap radar_back_src;

    private int startColorID;
    private int endColorID;
    private int sweeepDegree;
    private int sweepRadiusMargin;
    private int circleExpandReverseSpeed;

    /**
     * 设置圆形扩散速度，扩展速度与指定值相反
     *
     * @param circleExpandReverseSpeed
     */
    public void setCircleExpandReverseSpeed(int circleExpandReverseSpeed) {
        this.circleExpandReverseSpeed = circleExpandReverseSpeed;
    }

    /**
     * 设置扫描边距
     *
     * @param sweepRadiusMargin
     */
    public void setSweepRadiusMargin(int sweepRadiusMargin) {
        this.sweepRadiusMargin = sweepRadiusMargin;
    }

    /**
     * 设置扫描速度
     *
     * @param sweeepDegree
     */
    public void setSweeepDegree(int sweeepDegree) {
        this.sweeepDegree = sweeepDegree;
    }

    /**
     * 设置表盘图片
     *
     * @param back
     */
    public void setBackground(Bitmap back) {
        this.radar_back_src = back;
    }

    /**
     * 设置表盘标志
     *
     * @param top
     */
    public void setTopIcon(Bitmap top) {
        this.radar_top_src = top;
    }

    /**
     * 设置表盘边框图
     *
     * @param radar_border
     */
    public void setRadarBorder(Bitmap radar_border) {
        this.radar_border_src = radar_border;
    }

    /**
     * 设置扫描渐变颜色
     *
     * @param startColorID
     * @param endColorID
     */
    public void setSweepGradientColor(int startColorID, int endColorID) {
        this.startColorID = startColorID;
        this.endColorID = endColorID;
    }

    public ImageRadarView(Context context) {
        super(context);
    }

    public ImageRadarView(Context context, AttributeSet att) {
        super(context, att);
        TypedArray typeArray = context.obtainStyledAttributes(att, R.styleable.image_radar_view);
        // 获取参数
        startColorID = typeArray.getColor(R.styleable.image_radar_view_radar_start_color_id,
                Color.parseColor("#00FFFFFF"));
        endColorID = typeArray.getColor(R.styleable.image_radar_view_radar_end_color_id,
                Color.parseColor("#AAFFFFFF"));
        sweeepDegree = typeArray.getInt(R.styleable.image_radar_view_radar_sweeep_degree, 5);
        sweepRadiusMargin = typeArray.getInt(R.styleable.image_radar_view_radar_sweep_radius_margin, 15);
        circleExpandReverseSpeed = typeArray.getInt(R.styleable.image_radar_view_radar_circle_expand_reverse_speed, 25);

        // 获取资源
        Drawable backDrawable = typeArray.getDrawable(R.styleable.image_radar_view_radar_back_src);
        backDrawable = backDrawable == null ? getResources().getDrawable(R.mipmap.radar_back) : backDrawable;
        radar_back_src = ((BitmapDrawable) backDrawable).getBitmap();

        Drawable borderDrawable = typeArray.getDrawable(R.styleable.image_radar_view_radar_border_src);
        borderDrawable = borderDrawable == null ? getResources().getDrawable(R.mipmap.radar_border) : borderDrawable;
        radar_border_src = ((BitmapDrawable) borderDrawable).getBitmap();
        Drawable topIcon = typeArray.getDrawable(R.styleable.image_radar_view_radar_top_icon_src);
        topIcon = topIcon == null ? getResources().getDrawable(R.mipmap.cloud) : topIcon;
        radar_top_src = ((BitmapDrawable) topIcon).getBitmap();
        typeArray.recycle();
        initPaint();
    }

    /**
     * 初始化定义的画笔
     *
     * @param
     * @return void
     * @Description
     */
    private void initPaint() {
        // 圆形画笔，设置Paint为抗锯齿
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setARGB(255, 255, 255, 90);// 设置透明度和RGB颜色
        circlePaint.setStrokeWidth(3);// 轮廓宽度
        circlePaint.setStyle(Paint.Style.STROKE);
        // 线性画笔
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setARGB(255, 50, 57, 74);
        linePaint.setStrokeWidth(2);
        // 雷达Shader画笔
        sweepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sweepPaint.setStrokeCap(Paint.Cap.ROUND);
        sweepPaint.setStrokeWidth(4);
        sweepGradient = new SweepGradient(0, 0, startColorID, endColorID);
        sweepPaint.setShader(sweepGradient);
        //
        foundPoitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foundPoitPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 计算控件中心位置
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int cx = width / 2;
        int cy = height / 2;
        // 设置半径
        int radius = radar_back_src.getWidth() / 2;
        radius -= sweepRadiusMargin;
        canvas.save();
        // 设置扫描增量
        degree += sweeepDegree;
        canvas.drawBitmap(radar_back_src, cx - radar_back_src.getWidth() / 2,
                cy - radar_back_src.getHeight() / 2, null);
        canvas.drawBitmap(radar_border_src, cx - radar_border_src.getWidth()
                / 2, cy - radar_border_src.getHeight() / 2, null);
        // 设置转动原点
        canvas.translate(cx, cy);
        // 旋转
        canvas.rotate(270 + degree);
        // 绘制扫描区域
        canvas.drawCircle(0, 0, radius, sweepPaint);
        // 恢复Canvasy坐标（0,0）
        canvas.restore();

        canvas.drawCircle(cx, cy,
                radius * ((float) (degree / circleExpandReverseSpeed % circleExpandReverseSpeed) / (float) circleExpandReverseSpeed),
                sweepPaint);
        canvas.drawBitmap(radar_top_src, cx - radar_top_src.getWidth() / 2,
                cx - radar_top_src.getHeight() / 2, null);
        circlePaint.setAlpha(100);// 降低内部圆形的透明度
        circlePaint.setStrokeWidth(2);// 设置轮廓宽度
        canvas.save();//
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据图片大小设置控件大小
        int d = radar_border_src.getWidth() > radar_border_src.getHeight() ? radar_border_src.getWidth() : radar_border_src.getHeight();
        setMeasuredDimension(d, d); // 重写测量方法，保证获得的画布是正方形
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        this.setVisibility(View.VISIBLE);// 设置可见
        Animation radarAnimEnter = AnimationUtils.loadAnimation(getContext(),
                R.anim.radar_anim_enter);// 初始化radarView进入动画
        this.startAnimation(radarAnimEnter);// 开始进入动画
        radarSweepThread = new Thread(new RadarSweep());// 雷达扫描线程
        radarSweepThread.start();
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        Animation radarAnimEnter = AnimationUtils.loadAnimation(getContext(),
                R.anim.radar_anim_exit);// 初始化radarView退出动画
        this.startAnimation(radarAnimEnter);// 开始进入动画
        this.setVisibility(View.INVISIBLE);// 设置不可见
        radarSweepThread.interrupt();// 停止扫描更新
    }

    /**
     * @ClassName RadarSweep
     * @Description 雷达扫描动画刷新线程类
     */
    private class RadarSweep implements Runnable {
        int i = 1;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && i == 1) {
                try {
                    // 刷新radarView,执行onDraw();
                    ImageRadarView.this.postInvalidate();
                    Thread.sleep(10);// 暂停当前线程，更新UI线程
                } catch (InterruptedException e) {
                    i = 0;// 结束当前扫描线程标志符
                    break;
                }
            }
        }
    }//end

}
