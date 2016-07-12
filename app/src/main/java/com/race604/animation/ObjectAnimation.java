
package com.race604.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

public class ObjectAnimation<T> extends Animation implements Valuable {

    View view;
    float startValue;
    float endValue;
    float value;
    OnInvalidateImp onInvalidateImp;
    public ObjectAnimation(View view, float startValue, float endValue) {
        if (view == null) {
            throw new NullPointerException("ObjectAnimation(), view can't be null");
        }

        this.view = view;
        this.startValue = startValue;
        this.endValue = endValue;
    }

/*    public void initAnim(float startValue, float endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }*/

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float temp = startValue + (endValue - startValue) * interpolatedTime;
        if (temp != value) {
            value = temp;
            if(onInvalidateImp != null){
                onInvalidateImp.onInvalidate(view,value);
            }
            view.invalidate();
        }
        //super.applyTransformation(interpolatedTime,t);
    }

    public void addonInvalidateImp(OnInvalidateImp a){
        this.onInvalidateImp = a;
    }


	@Override
	public void setValue(float value) {
		this.value = value;
	}

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public void setInterpolator(Interpolator i) {
        super.setInterpolator(i);
    }
}
