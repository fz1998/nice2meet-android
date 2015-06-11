package com.threebody.conference.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.threebody.conference.R;


public class MyCountView extends TextView{
	Paint mPaint;
	int radius;
	public MyCountView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyCountView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyCountView(Context context) {
		super(context);
		init();
	}

	private void init() {
//        if(isInEditMode()){return ;}
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
//		mTextPaint = new Paint(mPaint);
//		mTextPaint.setColor(getContext().getResources().getColor(R.color.white));
		radius = getContext().getResources().getDimensionPixelSize(R.dimen.TextSize15)/2;
        setTextColor(getContext().getResources().getColor(R.color.white));
	}
	@Override
	protected void onDraw(Canvas canvas) {
//		if(radius == 0){
//			radius = getWidth()/2;
//		}
		canvas.drawCircle(getWidth()/2, getHeight()/2, radius, mPaint);
		super.onDraw(canvas);
	}
}
