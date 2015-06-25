
package com.race604.animation;

import android.view.animation.Interpolator;

import com.race604.animation.Changeable;

public class ValueObject implements Changeable {

    boolean debug;

/*    public void setDebug(boolean debug) {
        this.debug = debug;
    }*/

    float startValue;
    float endValue;
    float startTime;
    float endTime;
    Interpolator interpolator;
    float value;
    String tag;

    /**
     * for debug, add tag
     */
/*    public ValueObject(String tag, float startValue, float endValue, float startTime,
            float endTime, Interpolator interpolator) {
        this.tag = tag;
        this.startValue = startValue;
        this.endValue = endValue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interpolator = interpolator;
    }*/

    public ValueObject(float startValue, float endValue, float startTime, float endTime,
                       Interpolator interpolator) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interpolator = interpolator;
    }

    public void initAnim(float startValue, float endValue, float startTime, float endTime) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void initAnim(float startValue, float endValue, float startTime, float speed,
            float minDuration) {
        float duration = (int) Math.abs((endValue - startValue) / speed);
        if (duration < minDuration)
            duration = minDuration;

        this.startValue = startValue;
        this.endValue = endValue;
        this.startTime = startTime;
        this.endTime = startTime + duration;
    }

    public interface OnValueChangeListener {
        void onValueApply(float value, float min, float max);
    }

    OnValueChangeListener mOnValueChangeListener;

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mOnValueChangeListener = listener;
    }

    public float applyValueTime(float curTime) {
        float k = (curTime - startTime) / (endTime - startTime);
        return applyValueK(k);
    }

//    int count;

    public float applyValueK(float k) {
        if (k < 0)
            k = 0;
        if (k > 1)
            k = 1;

        if (interpolator != null) {
            k = interpolator.getInterpolation(k);
        }

        float temp = startValue + (endValue - startValue) * k;
        if (temp != value) {
            value = temp;
            if (mOnValueChangeListener != null)
                mOnValueChangeListener.onValueApply(value, min, max);
        }

        // count++;
        return value;
    }

/*    public void printCount() {
        if (tag != null)
            Log.d("show", tag + ", count = " + count);
    }*/

    public boolean inPeriod(float curTime) {
        return startTime <= curTime && curTime <= endTime;
    }

    public float distance() {
        return endValue - startValue;
    }

    public float duration() {
        return endTime - startTime;
    }

    public float getK() {
        float k = (value - startValue) / (endValue - startValue);
        return k;
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

    float min, max, middle;

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

/*    public float getMiddle() {
        return middle;
    }*/

    @Override
    public void setClipValue(float min, float max) {
        this.min = min;
        this.max = max;
        this.middle = (min + max) / 2f;
    }

    @Override
    public boolean changeValue(float delta) {
        float temp = value + delta;
        if (temp < min) {
            temp = min;
        } else if (temp > max) {
            temp = max;
        }
        if (temp != value) {
            // Log.d("show", "changeValue, value = " + value);
            value = temp;
            if (mOnValueChangeListener != null)
                mOnValueChangeListener.onValueApply(value, min, max);
            return true;
        }
        return false;
    }

    // public void clearCount() {
    // count = 0;
    // }

}
