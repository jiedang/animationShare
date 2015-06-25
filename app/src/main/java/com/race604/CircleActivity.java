package com.race604;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.dangjie.animationtest.R;
import com.race604.circle.CircleSwapView;
import com.race604.circle.CircleSwapViewOverturnTextView;


public class CircleActivity extends Activity implements CircleSwapView.OnCircleChangeListener ,View.OnClickListener {
    private CircleSwapView mCircle;
    private int percent = 92;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_turn);
        mCircle = (CircleSwapView) findViewById(R.id.circle);

        CircleSwapViewOverturnTextView over = (CircleSwapViewOverturnTextView) findViewById(R.id.float_window_memory_clean_tips_2);
        mCircle.setCircleSwapViewOverturnTextView(over);
        mCircle.setOnCountChangeListener(this);
        mCircle.setOnClickListener(this);
        mCircle.startCleanAnim(percent);
    }




    @Override
    public void onChange(final float degree) {
        mCircle.post(new Runnable() {
            @Override
            public void run() {
                mCircle.setMemoryText((int) (degree / 360 * 100)+"");
            }
        });
    }

    @Override
    public void onCleanAnimFinish(boolean hasOverShot) {
        mCircle.post(new Runnable() {
            @Override
            public void run() {
                mCircle.setMemoryText(percent+"");
            }
        });
    }

    @Override
    public void onCleanAnimStart() {

    }

    @Override
    public void onClick(View v) {
        percent = 63;
        mCircle.cleanSweep(percent,false);
    }
}
