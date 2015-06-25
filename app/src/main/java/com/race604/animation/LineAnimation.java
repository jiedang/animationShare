
package com.race604.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

public class LineAnimation extends Animation implements Valuable {

    View view;
    List<ValueObject> list = new ArrayList<ValueObject>();

    public LineAnimation(View v, List<ValueObject> l) {
        if (v == null) {
            throw new NullPointerException("LineAnimation(), view can't be null");
        }

        if (l == null || l.isEmpty()) {
            throw new NullPointerException("LineAnimation(), list can't be empty");
        }

        view = v;
        list.addAll(l);

        long duration = 0;
        for (int i = 0; i < list.size(); i++) {
            duration += list.get(i).duration();
        }
        setDuration(duration);
    }

    int i;
    float value;

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        ValueObject lb = list.get(i);
        if (lb == null)
            return;

        float curTime = interpolatedTime * getDuration();
        if (!lb.inPeriod(curTime)) {
            if (i + 1 >= list.size())
                return;
            lb = list.get(i + 1);
            i++;
        }

        float temp = lb.applyValueTime(curTime);
        if (temp != value) {
            value = temp;
            view.invalidate();
        }
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
        return i;
    }

}
