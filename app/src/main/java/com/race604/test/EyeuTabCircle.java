package com.race604.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.race604.DimenUtils;
import com.race604.animation.Valuable;


/**
 * Created by dangjie on 16/11/24.
 */

public class EyeuTabCircle extends RelativeLayout {
    private Paint mPaint;
    private float mCW, mCH;
    private int padding ,radius,strokeWidth;
    public EyeuTabCircle(Context context) {
        super(context);
        init(context);
    }

    public EyeuTabCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init(Context context) {
        padding = DimenUtils.dp2px(context,10);
        strokeWidth = DimenUtils.dp2px(context,5);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(Color.RED);
        setLayerType( LAYER_TYPE_SOFTWARE , null);
        setWillNotDraw(false);
        setPadding(padding,padding,padding,padding);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mCW == 0 && mCH == 0) {
            mCW = getWidth() / 2;
            mCH = getHeight() /2;
        }
        float progress =  getValue();
        if(mCH >= mCW){
            radius = (int)(mCW - padding + progress *  padding) ;
        }else{
            radius = (int)(mCH - padding + progress *  padding) ;
        }
        canvas.drawCircle(mCW, mCH, radius, mPaint);
    }


    private float getValue() {
        Animation anim = getAnimation();
        if (anim != null) {
            return ((Valuable) anim).getValue();
        }
        return 0;
    }

}
