package net.dell.ui_actionbar;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by dell on 2016/4/25.
 */
public class CustomEditText extends EditText {

    private Context mContext;
    private Drawable imageDraw;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    private void init() {
        imageDraw = mContext.getDrawable(R.mipmap.ad_gif_cancel);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
    }

    /**
     * 设置删除
     */
    private void setDrawable() {
        if (length()<1){
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, imageDraw, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (imageDraw!=null){
                    int x= (int) event.getRawX();
                    int y= (int) event.getRawY();

                    Rect rect=new Rect();
                    getGlobalVisibleRect(rect);

                    rect.left=rect.right-70;
                    if (rect.contains(x,y)){
                        setText("");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
