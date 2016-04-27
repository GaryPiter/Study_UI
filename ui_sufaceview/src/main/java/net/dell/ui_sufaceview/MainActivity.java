package net.dell.ui_sufaceview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MSurfaceView sufaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.clear).setOnClickListener(this);
        sufaceView= (MSurfaceView) findViewById(R.id.surface_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                sufaceView.clear();
                break;
        }
    }
}
