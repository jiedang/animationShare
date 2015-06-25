package com.race604;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dangjie.animationtest.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import com.race604.flyrefresh.internal.ElasticOutInterpolator;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
@ContentView(R.layout.activity_extrusion)
public class ExtrusionActivity extends RoboActivity implements View.OnClickListener{
    @InjectView(R.id.action_btn)
    private Button actionBtn;
    @InjectView(R.id.action_view)
    private TextView actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBtn.setOnClickListener(this);
        actionText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(actionText.getHeight() != 0){
                    RelativeLayout.LayoutParams lp  = (RelativeLayout.LayoutParams) actionText.getLayoutParams();
                    lp.bottomMargin = -actionText.getHeight();
                    actionText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }


    class DataHolder{
        float marginBottom;

        public DataHolder(float marginBottom) {
            this.marginBottom = marginBottom;
        }

        public float getMarginBottom() {
            return marginBottom;
        }

        public void setMarginBottom(float marginBottom) {
            this.marginBottom = marginBottom;
        }
    }

    class ViewHolder{
        TextView bottomView;

        public ViewHolder(TextView bottomView) {
            this.bottomView = bottomView;
        }

        public void setData(DataHolder dataHolder){
            DimenUtils.updateLayoutMargin(bottomView,-3,-3,-3,(int)dataHolder.getMarginBottom());
        }


        public DataHolder getData(){
            RelativeLayout.LayoutParams lp  = (RelativeLayout.LayoutParams) bottomView.getLayoutParams();
            return new DataHolder(lp.bottomMargin);
        }
    }

    private ObjectAnimator createAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(new ViewHolder(actionText), "data",
                new BottomEvaluator(), new DataHolder(0));
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new ElasticOutInterpolator());
        return objectAnimator;
    }
    public class BottomEvaluator implements TypeEvaluator<DataHolder> {
        @Override
        public DataHolder evaluate(float fraction, DataHolder startValue, DataHolder endValue) {
            return new DataHolder(startValue.getMarginBottom()
                    + fraction * (endValue.getMarginBottom() - startValue.getMarginBottom()));
        }
    }

    private ObjectAnimator objectAnimator= null;
    @Override
    public void onClick(View view) {
        if(view == null){
            return;
        }
        switch (view.getId()){
            case R.id.action_btn :
                if(objectAnimator == null){
                    objectAnimator =  createAnimation();
                }
                if(objectAnimator.isRunning()){
                    return;
                }
                if(view.getTag() == null || !(Boolean)view.getTag()){
                    objectAnimator.start();
                    view.setTag(true);
                }else{
                    objectAnimator.reverse();
                    view.setTag(false);
                }
                break;

        }

    }





}
