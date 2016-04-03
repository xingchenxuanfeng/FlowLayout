package com.XC.flowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	private static final String TAG = "FlowLayout";
	private boolean flowLayout_bule;

	public boolean isFlowLayout_bule() {
		return flowLayout_bule;
	}

	public void setFlowLayout_bule(boolean flowLayout_bule) {
		this.flowLayout_bule = flowLayout_bule;
		invalidate();
		requestLayout();
	}

	public int getFlowLayout_mcolor() {
		return flowLayout_mcolor;
	}

	public void setFlowLayout_mcolor(int flowLayout_mcolor) {
		this.flowLayout_mcolor = flowLayout_mcolor;
	}

	private int flowLayout_mcolor;
	private boolean flowLayout_hasline;
	private Paint paint;

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		switch (flowLayout_mcolor) {
		case 1:
			paint.setColor(Color.YELLOW);
			Log.i(TAG, "黄色");
			break;
		case 2:
			paint.setColor(Color.RED);
			Log.i(TAG, "红色");
			break;
		}
		if (flowLayout_bule) {
			paint.setColor(Color.BLUE);
			Log.i(TAG, "蓝色");
		}
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = null;
		try {
			typedArray = context.getResources().obtainAttributes(attrs,
					R.styleable.FlowLayout);
			flowLayout_bule = typedArray.getBoolean(
					R.styleable.FlowLayout_bule, false);
			flowLayout_mcolor = typedArray.getInt(
					R.styleable.FlowLayout_mcolor, 0);
			flowLayout_hasline = typedArray.getBoolean(
					R.styleable.FlowLayout_hasline, false);
		} finally {
			typedArray.recycle();
		}
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int sizewidth = MeasureSpec.getSize(widthMeasureSpec);
		int modewidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeheight = MeasureSpec.getSize(heightMeasureSpec);
		int modeheight = MeasureSpec.getMode(heightMeasureSpec);

		int finalwidth = 0;
		int finalheight = 0;
		int linewidth = 0;
		int lineheight = 0;

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			int childwidth = child.getMeasuredWidth();
			int childheight = child.getMeasuredHeight();
			if (true/* child.getLayoutParams() instanceof MarginLayoutParams */) {
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				childwidth += lp.leftMargin + lp.rightMargin;
				childheight += lp.topMargin + lp.bottomMargin;
			}
			if (linewidth + childwidth > sizewidth - getPaddingLeft()
					- getPaddingRight()
					|| i == getChildCount() - 1) {
				finalwidth = Math.max(finalwidth, linewidth);
				finalheight += lineheight;
				linewidth = 0;
				lineheight = 0;

			}
			linewidth += childwidth;
			lineheight = Math.max(lineheight, childheight);
		}
		setMeasuredDimension(
				//
				modewidth == MeasureSpec.EXACTLY ? sizewidth : finalwidth
						+ getPaddingLeft() + getPaddingRight(),
				modeheight == MeasureSpec.EXACTLY ? sizeheight : finalheight
						+ getPaddingTop() + getPaddingBottom());

	}

	List<List<View>> allViews = new ArrayList<List<View>>();
	List<Integer> lineheightList = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		allViews.clear();
		lineheightList.clear();
		int width = getWidth();
		int linewidth = 0;
		int lineheight = 0;
		List<View> lineViews = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			int leftMargin = 0, rightMargin = 0, topMargin = 0, bottomMargin = 0;
			if (true/* child.getLayoutParams() instanceof MarginLayoutParams */) {
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				leftMargin = lp.leftMargin;
				rightMargin = lp.rightMargin;
				topMargin = lp.topMargin;
				bottomMargin = lp.bottomMargin;
			}
			if (linewidth + leftMargin + child.getMeasuredWidth() + rightMargin > width
					- getPaddingLeft() - getPaddingRight()
					|| i == getChildCount() - 1) {
				lineheightList.add(lineheight);
				allViews.add(lineViews);
				lineViews = new ArrayList<View>();
				linewidth = 0;
				lineheight = 0;
			}
			linewidth += leftMargin + child.getMeasuredWidth() + rightMargin;
			lineheight = Math.max(lineheight,
					topMargin + child.getMeasuredHeight() + topMargin);
			lineViews.add(child);
		}
		lineheight = 0;
		for (int i = 0; i < allViews.size(); i++) {
			lineViews = allViews.get(i);
			linewidth = 0;
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				int leftMargin = 0, rightMargin = 0, topMargin = 0, bottomMargin = 0;
				if (true/* child.getLayoutParams() instanceof MarginLayoutParams */) {
					MarginLayoutParams lp = (MarginLayoutParams) child
							.getLayoutParams();
					leftMargin = lp.leftMargin;
					rightMargin = lp.rightMargin;
					topMargin = lp.topMargin;
					bottomMargin = lp.bottomMargin;
				}
				int lc = linewidth + leftMargin;
				int tc = lineheight + topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();
				child.layout(lc, tc, rc, bc);
				linewidth = rc + rightMargin;
			}
			lineheight += lineheightList.get(i);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 与当前ViewGroup对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

}
