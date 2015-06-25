package com.race604;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dangjie.animationtest.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.race604.animation.ObjectAnimation;
import com.race604.animation.OnInvalidateImp;
import com.race604.flyrefresh.internal.ElasticOutInterpolator;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_extrusion)
public class ExtrusionActivityByObject extends RoboActivity implements View.OnClickListener{
    @InjectView(R.id.action_btn)
    private Button actionBtn;
    @InjectView(R.id.action_view)
    private TextView actionText;
    private int height=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBtn.setOnClickListener(this);
        actionText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(actionText.getHeight() != 0){
                    height =actionText.getHeight();
                    RelativeLayout.LayoutParams lp  = (RelativeLayout.LayoutParams) actionText.getLayoutParams();
                    lp.bottomMargin = - height;
                    actionText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }



    private void createAnimation(boolean isShow) {
        ObjectAnimation objectAnimation = isShow?new ObjectAnimation(actionText,-height, 0):new ObjectAnimation(actionText, 0,-height);
        objectAnimation.setDuration(500);
       // objectAnimation.setInterpolator(new ElasticOutInterpolator());
        objectAnimation.addonInvalidateImp(new OnInvalidateImp() {
            @Override
            public void onInvalidate(View v,float value) {
                DimenUtils.updateLayoutMargin(v,-3,-3,-3,(int)value);
            }
        });
        actionText.startAnimation(objectAnimation);
    }

    @Override
    public void onClick(View view) {
        if(view == null){
            return;
        }
        switch (view.getId()){
            case R.id.action_btn :
                if(view.getTag() == null || !(Boolean)view.getTag()){
                    createAnimation(true);
                    view.setTag(true);
                }else{
                    createAnimation(false);
                    view.setTag(false);
                }
                break;

        }

    }





}
