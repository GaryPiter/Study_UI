package net.dell.ui_custom01;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.dell.ui_custom01.view.CombinationlStatusView;
import net.dell.ui_custom01.view.customprogress.MovingProgressBar;

public class CombinationlStatusActivity extends AppCompatActivity {

    private static final int MSG_PROGRESS_UPDATE = 0x110;
    private MovingProgressBar mProgressBar;
    private CombinationlStatusView combinationlStatusView;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combinationl_status);

        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
        combinationlStatusView = (CombinationlStatusView) findViewById(R.id.check);
        mProgressBar = (MovingProgressBar) combinationlStatusView.findViewById(R.id.progress_bar);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            time += 100;
            if (time < 1000) {
                Toast.makeText(CombinationlStatusActivity.this, "选中当前", Toast.LENGTH_SHORT).show();
                combinationlStatusView.show(getString(R.string.callback_status_view_type_checkbox));
            } else {
                int progress = mProgressBar.getProgress();
                if (progress >= 100) {
                    Toast.makeText(CombinationlStatusActivity.this, "完成显示结果", Toast.LENGTH_SHORT).show();
                    combinationlStatusView.show(getString(R.string.callback_status_view_type_checkedtext));
                } else {
                    Toast.makeText(CombinationlStatusActivity.this, "显示选中行操作进度", Toast.LENGTH_SHORT).show();
                    combinationlStatusView.show(getString(R.string.callback_status_view_type_progressbar));
                    mProgressBar.setProgress(++progress);
                }
            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        };
    };//end


}
