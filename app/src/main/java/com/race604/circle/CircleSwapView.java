
package com.race604.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;

import com.race604.animation.ObjectAnimation;
import com.race604.utils.UiHelper;

public class CircleSwapView extends View {

    private Paint mPaint;
    // private boolean mUseCenters;
    private RectF mBigOval;

    private float mEndTo = 0.0f;
    private float mCurrentSweep = 0.0f;
    private OnCircleChangeListener mOnCircleChangeListener;

    private boolean mIsAnim = false;
    private final float SWEEP_INC = 0.030f;
    private final int FRESH_DURATION = 20;
    private DecelerateInterpolator mInterpolator;
    private float mInter;

//    public CircleSwapView(Context context) {
//        super(context);
//        init();
//    }

    public CircleSwapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initDraw();
    }

    public void initDraw() {
        mPaint = new Paint();
        //mPaint.setColor(0xFF498bf7);
        mPaint.setColor(0xFFD6E3F8);
        mPaint.setAntiAlias(true);
        // BEGIN, James
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStrokeWidth(UiHelper.dp2px(getContext(), 2));
        // END, James
        // mUseCenters = true;
        mInterpolator = new DecelerateInterpolator();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int padding = UiHelper.dp2px(getContext(), 4);
                mBigOval = new RectF(0 + padding, 0 + padding, getWidth() - padding, getHeight()
                        - padding);
            }
        });
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // HardwareAccCheck.disableHardwareAcce(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawArc(mBigOval, -90, mCurrentSweep, false, mPaint);
    }

    public float getInterpolation(float t) {
        float t1 = (float) (t / (0.8 / (Math.PI / 2)));
        return (float) (1.12f * Math.sin(t1));
    }


    public interface OnCircleChangeListener {

        public void onChange(float degree);

        public void onCleanAnimFinish(boolean hasOverShot);

        public void onCleanAnimStart();
    }

    public void setOnCountChangeListener(OnCircleChangeListener l) {
        mOnCircleChangeListener = l;
    }

    public void sweepTo(final float from, final float to, final boolean hasOverShot) {
        mCurrentSweep = from;
        if (!mIsAnim) {
            mIsAnim = true;
            // Log.d("show", "set true");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mOnCircleChangeListener != null) {
                    mOnCircleChangeListener.onCleanAnimStart();
                }
                final boolean isDirect = to >= from;
                mInter = 0.0f;
                while (mInter <= 1.0f) {
                    float k = mInterpolator.getInterpolation(mInter);
                    if ( hasOverShot)
                        k = getInterpolation(k);
                    float change =  k * (Math.abs(to - from));
                    if (isDirect) {
                        mCurrentSweep = from + change;
                    } else {
                        mCurrentSweep = from - change;
                    }
                    mInter += SWEEP_INC;
                    mHandler.sendEmptyMessage(MSG_INVALIDATE);
                    if (mOnCircleChangeListener != null) {
                        if (mInter > 0.3f)
                            mOnCircleChangeListener.onChange(mCurrentSweep);
                    }
                    try {
                        Thread.sleep(FRESH_DURATION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (to != mEndTo) {
                    if(mCurrentSweep <= 0){
                        mCurrentSweep = 0;
                    }
                    mHandler.sendEmptyMessage(MSG_SWEEP);
                } else {
                	if(needShowMemoryCleanTips){
						if (circleSwapViewOverturnTextView != null) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									ObjectAnimation objectAnimation = new ObjectAnimation(circleSwapViewOverturnTextView, 0, 180);
									objectAnimation.setDuration(1000);
									objectAnimation.setFillAfter(true);
									objectAnimation.setAnimationListener(new AnimationListener() {
										@Override
										public void onAnimationStart(Animation animation) {
											circleSwapViewOverturnTextView.showOverturn();
										}
										@Override
										public void onAnimationRepeat(Animation animation) {
										}
										@Override
										public void onAnimationEnd(Animation animation) {
											mIsAnim = false;
										}
									});
									circleSwapViewOverturnTextView.startAnimation(objectAnimation);
								}
							});
						}
                		needShowMemoryCleanTips = false;
                    }else{
                    	if (mOnCircleChangeListener != null) {
                            mOnCircleChangeListener.onCleanAnimFinish(hasOverShot);
                        }
                        mIsAnim = false;                    	
                    }
                }
            }
        }).start();
    }


    private final int MSG_INVALIDATE = 0;
    private final int MSG_SWEEP = 1;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INVALIDATE:
                    invalidate();
                    break;
                case MSG_SWEEP:
                    sweepTo(mCurrentSweep, mEndTo, false);
                    break;
            }
        };
    };

    public void cleanSweep(float percent,boolean hasOverShot) {
        float to = (float) percent / 100 * 360;
        mEndTo = to;
        if(circleSwapViewOverturnTextView != null ){
        	circleSwapViewOverturnTextView.stopOverturn();
        }
        sweepTo(mCurrentSweep, 0.1f, hasOverShot);
    }
    
    public void startCleanAnim(float percent){
    	mCurrentSweep = 0.1f;
    	cleanSweep(percent,true);
    }

    public void cleanSingleSweep(float percent) {
        float to = (float) percent / 100 * 360;
        mEndTo = to;
        if(circleSwapViewOverturnTextView != null){
        	circleSwapViewOverturnTextView.stopOverturn();
        }
        sweepTo(mCurrentSweep, mEndTo, false);
    }

    private boolean needShowMemoryCleanTips = true;
    private CircleSwapViewOverturnTextView circleSwapViewOverturnTextView;
	public void setCircleSwapViewOverturnTextView(CircleSwapViewOverturnTextView _v){
		circleSwapViewOverturnTextView = _v;
	}
    
    

	public void setMemoryText(CharSequence charSequence){
		if(circleSwapViewOverturnTextView != null){
			circleSwapViewOverturnTextView.setMemroyText(charSequence);
            circleSwapViewOverturnTextView.invalidate();
		}
	}
	
}
