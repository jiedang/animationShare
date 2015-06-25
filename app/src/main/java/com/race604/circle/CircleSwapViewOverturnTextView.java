package com.race604.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.example.dangjie.animationtest.R;
import com.race604.animation.Valuable;
import com.race604.utils.UiHelper;




public class CircleSwapViewOverturnTextView extends TextView {
	private final Camera mCamera = new Camera();
	Transformation transformToApply = new Transformation();
	private int width, height;
	private int cX, cY;
	private Bitmap memoryTipsBitMap, memoryBitMap;
	private float animValue = 0;
	private Paint paint = new Paint();
	private TextPaint tipsTextPaint;
	private Paint memorySignTextPaint,memoryTextPaint;
	private PaintFlagsDrawFilter paintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private Drawable bgDrawable;
	private String memory = "0", memorySign = "%", memoryTips = "";
	private boolean showOverTrun;
	private boolean hasInit = false;
	public CircleSwapViewOverturnTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CircleSwapViewOverturnTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleSwapViewOverturnTextView(Context context) {
		super(context);
		init();
	}
	private void init() {
		if(hasInit){
			return;
		}

		Context context = getContext();
		hasInit = true;
		tipsTextPaint = new TextPaint();
		tipsTextPaint.setTextSize(UiHelper.dp2px(context, 16));
		tipsTextPaint.setColor(Color.WHITE);

		memoryTextPaint = new Paint();
		memoryTextPaint.setTextSize(UiHelper.dp2px(context, 25));
		memoryTextPaint.setColor(Color.WHITE);

		memorySignTextPaint = new Paint();
		memorySignTextPaint.setTextSize(UiHelper.dp2px(context, 10));
		memorySignTextPaint.setColor(Color.WHITE);
		memoryTips = "点击";
		bgDrawable = getResources().getDrawable(R.drawable.floatingwindown_buttom_inner_bg);
	
		width = height = UiHelper.dp2px(context, 56);
		bgDrawable.setBounds(0, 0, width, height);
		cX = width / 2;
		cY = height / 2;
		memoryBitMap =  Bitmap.createBitmap(width, height, Config.ARGB_8888);
		memoryTipsBitMap =  Bitmap.createBitmap(width, height, Config.ARGB_8888);
	}
	public void showOverturn() {
		Canvas memoryBitMapCanvas = createCacheBitMap(memoryBitMap);
		drawMemoryText(memoryBitMapCanvas);
		Canvas cacheCanvas =createCacheBitMap(memoryTipsBitMap);
		StaticLayout staticLayout =new StaticLayout(memoryTips, tipsTextPaint,UiHelper.dp2px(getContext(), 42), Layout.Alignment.ALIGN_CENTER, 1, 0, false);
		cacheCanvas.translate((width - staticLayout.getWidth())/2, (height - staticLayout.getHeight())/2);
		staticLayout.draw(cacheCanvas);
		showOverTrun = true;
	}
	
	public void setMemoryTipsTextSize(int size) {
		tipsTextPaint.setTextSize(UiHelper.dp2px(getContext(), size));
	}

	public void stopOverturn() {
		showOverTrun = false;
	}

	private Canvas createCacheBitMap(Bitmap bitmap){
		Canvas cacheCanvas = new Canvas(bitmap);
		cacheCanvas.setDrawFilter(paintFilter);
		bgDrawable.draw(cacheCanvas);
		return cacheCanvas;
	}
	
	
	public void setMemroyText(CharSequence charSequence) {
		this.memory = charSequence.toString();
	}

	private float[] getDrawTextXY(String str, Paint paint) {
		float[] out = new float[2];
		float memoryTextWidth = paint.measureText(str);
		FontMetrics fm = paint.getFontMetrics();
		int memoryTextHeight = (int) Math.ceil(fm.descent - fm.ascent);
		out[0] = (width - memoryTextWidth) / 2;
		out[1] = height - (height - memoryTextHeight) / 2f - fm.bottom;
		return out;
	}

	private void drawMemoryText(Canvas canvas) {
		float[] xy = getDrawTextXY(memory, memoryTextPaint);
		float memoryTextX = xy[0];
		float memoryTextY = xy[1];
		canvas.drawText(memory, memoryTextX, memoryTextY, memoryTextPaint);
		float signTextX = memoryTextX + memoryTextPaint.measureText(memory);
		canvas.drawText(memorySign, signTextX, memoryTextY, memorySignTextPaint);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {o
		canvas.setDrawFilter(paintFilter);
		if (showOverTrun) {
			float value = getValue();
			if (value < 0)
				return;
			Matrix matrix = transformToApply.getMatrix();
			if (value <= 90 && memoryBitMap != null) {
				transformMatrix(matrix, cX, cY, 0, -value);
				canvas.save();
				canvas.concat(matrix);
				canvas.drawBitmap(memoryBitMap, 0, 0, paint);
				canvas.restore();
			}

			if (90 < value && memoryTipsBitMap != null) {
				transformMatrix(matrix, cX, cY, 0, 180 - value);
				canvas.save();
				canvas.concat(matrix);
				canvas.drawBitmap(memoryTipsBitMap, 0, 0, paint);
				canvas.restore();
			}
		} else {
			canvas.save();
			bgDrawable.draw(canvas);
            if(!TextUtils.isEmpty(memory)){
                drawMemoryText(canvas);
            }
			canvas.restore();
		}
	}

	private float getValue() {
		Animation anim = getAnimation();
		if (anim != null) {
			return ((Valuable) anim).getValue();
		}
		return animValue;
	}

	private void transformMatrix(Matrix matrix, float cw, float ch, float degressX, float degressY) {
		final Camera camera = mCamera;
		camera.save();
		if (degressX != 0)
			camera.rotateX(degressX);
		if (degressY != 0)
			camera.rotateY(degressY);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-cw, -ch);
		matrix.postTranslate(cw, ch);
	}
}
