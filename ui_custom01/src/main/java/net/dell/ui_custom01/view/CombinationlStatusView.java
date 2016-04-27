package net.dell.ui_custom01.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.dell.ui_custom01.R;
import net.dell.ui_custom01.view.customprogress.MovingProgressBar;

/**
 * Created by dell on 2016/4/14.
 */
public class CombinationlStatusView extends LinearLayout {

    public final static String TYPE_CheckBox = "checkbox";
    public final static String TYPE_ProgressBar = "progressbar";
    public final static String TYPE_CheckedTextView = "checkedtext";
    public final static String TYPE_TextView = "text";

    public CheckBox checkbox = null;
    public MovingProgressBar progressBar = null;
    public CheckedTextView checkedTextView = null;
    public TextView textView = null;

    public CombinationlStatusView(Context context) {
        super(context);
    }

    public CombinationlStatusView(Context context, AttributeSet attr) {
        super(context, attr);
        if(isInEditMode()) {
            return;
        }
        obtainViews(context);
        initViewStatus(context, attr);
    }

    /**
     * 加载布局
     * @param context
     */
    private void obtainViews(Context context) {
        // 导入布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.custom_view_backup_status, this, true);

        checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        progressBar = (MovingProgressBar) view.findViewById(R.id.progress_bar);
        checkedTextView = (CheckedTextView) view.findViewById(R.id.checked_textview);
        textView = (TextView) view.findViewById(R.id.textview);
    }

    /**
     * 初始化控件状态
     * @param context
     * @param attr
     */
    private void initViewStatus(Context context, AttributeSet attr ){
        TypedArray typeArray = context.obtainStyledAttributes(attr, R.styleable.combinationl_status_view);
        //获取显示的控件类型
        String type = typeArray.getString(R.styleable.combinationl_status_view_type);
        //获取checkbox状态
        boolean checkState = typeArray.getBoolean(R.styleable.combinationl_status_view_check_state, true);
        //获取进度
        int progress = typeArray.getInteger(R.styleable.combinationl_status_view_progress, 0);
        //获取checkedTextView显示的内容
        String checkedText = typeArray.getString(R.styleable.combinationl_status_view_checkedtext);
        //获取TextView显示的内容
        String text = typeArray.getString(R.styleable.combinationl_status_view_text);
        //缓存属性,可以不设置，主要是为了提高效率
        typeArray.recycle();
        show(type);

        setCheckStated(checkState);
        setProgress(progress);
        setCheckedText(checkedText);
        setCheckedText(text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置Checkbox状态
     * @param state
     */
    public void setCheckStated(boolean state){
        checkbox.setChecked(state);
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }

    /**
     * 设置checkedTextView 内容
     * @param text
     */
    public void setCheckedText(String text){
        checkedTextView.setText(text);
    }

    /**
     * 设置TextView内容
     * @param text
     */
    public void setText(String text){
        textView.setText(text);
    }

    /**
     * 根据给定的类型显示指定的控件
     * @param type
     */
    public void show(String type) {
        //首先隐藏所有控件
        hideBesides(type);

        if(type.equals(TYPE_CheckBox))
            checkbox.setVisibility(VISIBLE);
        if(type.equals(TYPE_ProgressBar))
            progressBar.setVisibility(VISIBLE);
        if(type.equals(TYPE_CheckedTextView))
            checkedTextView.setVisibility(VISIBLE);
        if(type.equals(TYPE_TextView))
            textView.setVisibility(VISIBLE);
    }

    /**
     * 隐藏所以的控件
     * @param type
     */
    private void hideBesides(String type) {
        if(checkbox.getVisibility() == View.VISIBLE && type != TYPE_CheckBox) checkbox.setVisibility(GONE);
        if(progressBar.getVisibility() ==View.VISIBLE && type != TYPE_ProgressBar) progressBar.setVisibility(GONE);
        if(checkedTextView.getVisibility() ==View.VISIBLE && type != TYPE_CheckedTextView) checkedTextView.setVisibility(GONE);
        if(textView.getVisibility() ==View.VISIBLE && type != TYPE_TextView) textView.setVisibility(GONE);
    }
}
