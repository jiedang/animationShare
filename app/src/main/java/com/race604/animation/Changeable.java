
package com.race604.animation;

public interface Changeable extends Valuable {

    public void setClipValue(float min, float max);

    public boolean changeValue(float delta);

}
