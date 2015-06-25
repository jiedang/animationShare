#细说Android动画#

动画感觉说白了就是一句话   
**在触发范围内 循环的修改view特点后 使view重新绘制 展示新的效果**

所以只要有满足三个条件就能完成一个效果

- 触发范围  

 比如给个时间 在范围内做个事，或者是给个滑动距离 手指在范围内跟随滑动做个事。

- 能改变一个值 
 
 比如View基本属性 或者布局的某个属性，或者自己写的某个效果 画个水波，翻页，做个扩散

- 循环触发绘制

 只要能循环触发 View.invalidate就可以了
 
 触发条件 范围 一般都是产品，交互给定的，触发绘制是系统的， 所以关键是 **怎么去改好这个值**。
 
****
##属性动画##
   要说属性动画必须提到 ***nineOld( JakeWharton  大牛作品)***。nineOld和Android系统里面的类 名称，功能也一样，所以使用起来 没啥区别。同事还支持3.0以下版本的，还提供了ViewHelper支持通过set,get方法修改View基本属性。（3.0以上 可以支持set get方法去修改）

###基本使用###
1. 最简单写法

 ![](http://note.youdao.com/yws/res/287/64D80AE2E1684624A61D17AEEA4EAF20)


2. 支持的基本属性 可以在 nineOld源码中得 ObjectAnimator 查看支持的属性
 ![](http://note.youdao.com/yws/res/288/F98F2BAFF20F4DD1B318A16776A10D70)
3. viewHelper支持低版本 可以直接set,get操作VIEW属性 

4. AnimatorSet 控制多个动画设置执行顺序
 

 
 ```
  AnimatorSet  animation = new AnimatorSet();
  1 animation.playTogether(AA,BB); 
  2 animation.playSequentially(AA,BB);
  3 animation.play(AA).with(BB).after(CC).before(DD);
 ```
  代码块1 同时执行AA和BB
 
  代码块2 线性执行AA和BB
 
  代码块3 自己构建执行顺序
  

  
5. 一个view多个属性同时变化完成动画 

 
 - PropertyValuesHolder使用
 
 ```
 
 PropertyValuesHolder.ofFloat("width", A,B;
 
 PropertyValuesHolder.ofFloat("height", c,D);
 
 通过objectAnimator的 PropertyValues借口实现多个属性同时修改
 ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder... )
 
 
 ```

 - ViewPropertyAnimator.animate连缀写
 
    左右翻转着 平移到（A,B）
  
  ```
   ViewPropertyAnimator.animate(view).rotationYBy(720).x(xValue).y(yValue)
  
  ``` 
  

###nineOld实现原理及其自定义实现###
 
 1. 自定义实现 **demo挤出效果**
  
  有时候完成一个效果 已经不是view基本属性的范围 就需要自定义一个“属性”。
  
  例如 做一个挤出 收回 VIEW的动画 需要动态调整属性是 布局属性 layout_marginBottom

  自定属性包含三部分
  
  - viewHolder 被操作的VIEW
  
  - dataHolder 要操作的数据
  
  - Evaluator 数据怎么变

2. nineOld怎么改值 怎么实现的

 - API 11以上  通过反射修改
 	
 	  核心类 **PropertyValuesHolder**
	
	  PropertyValuesHolder来反射修改属性值 最关键的 是获取方法
	
	```
	 private Method getPropertyFunction(Class targetClass, String prefix, Class valueType) {
        // TODO: faster implementation...
        Method returnVal = null;
        String methodName = getMethodName(prefix, mPropertyName);
        Class args[] = null;
        if (valueType == null) {
            try {
                returnVal = targetClass.getMethod(methodName, args);
            } catch (NoSuchMethodException e) {
                /* The native implementation uses JNI to do reflection, which allows access to private methods.
                 * getDeclaredMethod(..) does not find superclass methods, so it's implemented as a fallback.
                 */
                try {
                    returnVal = targetClass.getDeclaredMethod(methodName, args);
                    returnVal.setAccessible(true);
                } catch (NoSuchMethodException e2) {
                    Log.e("PropertyValuesHolder",
                            "Couldn't find no-arg method for property " + mPropertyName + ": " + e);
                }
            }
        } else {
            args = new Class[1];
            Class typeVariants[];
            if (mValueType.equals(Float.class)) {
                typeVariants = FLOAT_VARIANTS;
            } else if (mValueType.equals(Integer.class)) {
                typeVariants = INTEGER_VARIANTS;
            } else if (mValueType.equals(Double.class)) {
                typeVariants = DOUBLE_VARIANTS;
            } else {
                typeVariants = new Class[1];
                typeVariants[0] = mValueType;
            }
            for (Class typeVariant : typeVariants) {
                args[0] = typeVariant;
                try {
                    returnVal = targetClass.getMethod(methodName, args);
                    // change the value type to suit
                    mValueType = typeVariant;
                    return returnVal;
                } catch (NoSuchMethodException e) {
                    /* The native implementation uses JNI to do reflection, which allows access to private methods.
                     * getDeclaredMethod(..) does not find superclass methods, so it's implemented as a fallback.
                     */
                    try {
                        returnVal = targetClass.getDeclaredMethod(methodName, args);
                        returnVal.setAccessible(true);
                        // change the value type to suit
                        mValueType = typeVariant;
                        return returnVal;
                    } catch (NoSuchMethodException e2) {
                        // Swallow the error and keep trying other variants
                    }
                }
            }
            // If we got here, then no appropriate function was found
            Log.e("PropertyValuesHolder",
                    "Couldn't find setter/getter for property " + mPropertyName +
                            " with value type "+ mValueType);
        }

        return returnVal;
    }
	
	```
		
- API 11以下  view矩阵变化

	核心类 ***AnimatorProxy***
	
	AnimatorProxy矩阵变换
	
	```
	 private void transformMatrix(Matrix m, View view) {
        final float w = view.getWidth();
        final float h = view.getHeight();
        final boolean hasPivot = mHasPivot;
        final float pX = hasPivot ? mPivotX : w / 2f;
        final float pY = hasPivot ? mPivotY : h / 2f;

        final float rX = mRotationX;
        final float rY = mRotationY;
        final float rZ = mRotationZ;
        if ((rX != 0) || (rY != 0) || (rZ != 0)) {
            final Camera camera = mCamera;
            camera.save();
            camera.rotateX(rX);
            camera.rotateY(rY);
            camera.rotateZ(-rZ);
            camera.getMatrix(m);
            camera.restore();
            m.preTranslate(-pX, -pY);
            m.postTranslate(pX, pY);
        }

        final float sX = mScaleX;
        final float sY = mScaleY;
        if ((sX != 1.0f) || (sY != 1.0f)) {
            m.postScale(sX, sY);
            final float sPX = -(pX / w) * ((sX * w) - w);
            final float sPY = -(pY / h) * ((sY * h) - h);
            m.postTranslate(sPX, sPY);
        }

        m.postTranslate(mTranslationX, mTranslationY);
    }
	
	```
3. 触发绘制

	关键类 ***ValueAnimator***
  
  	关键方法 ***animationFrame*** 控制run 
  
	关键方法 ***handleMessage*** 处理一些状态 start stop 
	
###组合使用 flyRefrsh ###

  flyRefrsh 使用系统属性动画来组合做出来的 没有使用nineOld做低版本支持，拿来主要看组合使用。
  
  github项目地址 [传送门](https://github.com/race604/FlyRefresh)
	
  实现分析[传送门](http://www.race604.com/flyrefresh/)

****	 
##自定义动画##
 
 我们也可以像Android官方提供动画一样 自己去复写一个类似于ScaleAnimation一样
 
 主要方法 
 
 - applyTransformation 实现动画的效果变化(官方都是改矩阵 也可以改其他)
 - initialize 初始化数据

 
###对于属性操作###

**demo挤出效果 自定义动画实现**

ObjectAnimation 关键方法

```

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
    
```

在实现效果方法里面 丢出借口 onInvalidate（view,value），外部使用中间量 实现对应效果（改属性或者改矩阵）

关于动画绘制过程 有哪些方法 都是干什么的 可以关注啊下

android 动画执行流程分析 [传送门](http://dangjie.me/2015/06/05/animationProcess.html)

###复写draw实现###

说到draw就必须说这三个类

最关键类 canvas画布  paint画笔   matrix矩阵

- canvas 整体性操作 类型 翻转，平移，缩放，扭曲，裁剪

 canvas操作中 不受到其他操作影响 一定要 save()、restore()

- paint 效果细节 颜色，粗细，重叠方式，空实心等 比较多写不过来

- matrix 存储view基本特征属性(位置，大小，2D/3D旋转角度，倾斜角度) 

 可以通过修改这些属性实现效果


 可以百度下 很多基础功能类介绍，
 
 对于3D翻转,使用camera构建一个矩阵添加到画布上去 类似于nineOld中对于API 11以下版本实现方式


**demo简单出票**

主要完成一个画布的裁剪 实现一个“吐出来”效果
有使用一些动画上 原有功能 RepeatCount RepeatMode 实现往复

```

     		canvas.save();
            canvas.translate(0,currentHeight - ticketH);
            canvas.clipRect(ticketL, ticketT + ticketH - currentHeight,ticketR,ticketB);
            ticket.draw(canvas);
            canvas.restore();
 
```


**demo圆环**

复杂点 2个状态切换的 圆环翻转 和 圆环弧度变化

翻转

生成翻转矩阵 添加到画布上

```
XX 矩阵操作生成翻转 matrix  XX
canvas.concat(matrix);

```

跟弧度 画对应长度圆弧标示百分比

```
canvas.drawArc(mBigOval, -90, mCurrentSweep, false, mPaint);

```

优化点

讲两个view合成为一个 在objectionAnimation 存储状态值 
判断状态值 进行不同操作

```
  @Override
    public float getValue() {
        return value;
    }

    @Override
    public int getStep() {
        return 0;
    }

```





###控件动画实现###

控件每个都不一样 主要是找到你要进行动画的view 添加对应的效果

1. listView item添加效果

 **demo jazzyListView** 实现核心代码

2. viewGourp item 添加效果

 **demoe sortGridView** 实现核心代码



****
## interpolator 插值器##
原来收藏过的 比较详细的一个 图比较多 很清楚直观

介绍文档[传送门](http://my.oschina.net/banxi/blog/135633)

一个github项目[传送门](https://github.com/daimajia/AnimationEasingFunctions)
