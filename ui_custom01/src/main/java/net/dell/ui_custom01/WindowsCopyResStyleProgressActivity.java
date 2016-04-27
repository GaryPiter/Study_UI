package net.dell.ui_custom01;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import net.dell.ui_custom01.view.customprogress.MovingProgressBar;
import net.dell.ui_custom01.view.customprogress.PointsProgressBar;
import net.dell.ui_custom01.view.customprogress.WindowsCopyResStyleProgress;

public class WindowsCopyResStyleProgressActivity extends AppCompatActivity {

    private static final int MSG_PROGRESS_UPDATE = 0x110;
    private WindowsCopyResStyleProgress mProgressBar;
    private MovingProgressBar movingProgressBar;
    private PointsProgressBar pointsProgressBar;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int progress = mProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
            movingProgressBar.setProgress(progress);
            if (progress >= 100) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows_copy_res_style_progress);

        mProgressBar = (WindowsCopyResStyleProgress) findViewById(R.id.progressbar);
        movingProgressBar = (MovingProgressBar) findViewById(R.id.moving_progress_bar);
        pointsProgressBar = (PointsProgressBar) findViewById(R.id.points_p_b);
        pointsProgressBar.start();
        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
    }
}
