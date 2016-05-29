package com.lhp.toggleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关视图类
 */

public class ToggleView extends View {

	private Bitmap switchBackgroundBitmap;
	private Bitmap slideButtonBitmap;
	private Paint paint;
	private boolean isSwitchOn = false;
	private boolean isMove = false;
	private float curX;
	private float widthMinus;
	private float center;
	private OnSwitchStateChangedListener onSwitchStateChangedListener;

	/** 必须重写该构造函数 */
	public ToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
		widthMinus = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
		center = switchBackgroundBitmap.getWidth() / 2.0f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 绘制背景
		canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);
		// 根据开关状态决定按钮的显示位置
		if (isMove) {
			if (curX >= (widthMinus + slideButtonBitmap.getWidth() / 2.0f)) {
				curX = widthMinus + slideButtonBitmap.getWidth() / 2.0f;
			}
			if (curX <= slideButtonBitmap.getWidth() / 2.0f) {
				curX = slideButtonBitmap.getWidth() / 2.0f;
			}
			canvas.drawBitmap(slideButtonBitmap, curX - slideButtonBitmap.getWidth() / 2.0f, 0, paint);
		} else {
			if (isSwitchOn) {
				canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
			} else {
				canvas.drawBitmap(slideButtonBitmap, // 要显示的位图
						switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth(), // 起始点的X坐标
						0, // 起始点的Y坐标
						paint); // 画笔
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			isMove = true;
			curX = event.getX(); // 不是整个屏幕的X坐标，而是本View的X坐标。
			break;
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_UP:
			// 抬起时不再是isMove状态
			isMove = false;
			// 判断点击为位置对应哪种状态
			boolean state = (event.getX() < center);
			// 如果抬起的位置对应的开关状态与原开关状态不同，则
			// 注：如果调用者没有设置监听，那么监听接口对象为null，此处不加判断会空指针。
			if (state != isSwitchOn && onSwitchStateChangedListener != null) {
				// 更新开关状态，以实现监听，并返回给调用者。
				onSwitchStateChangedListener.onStateChanged(state);
			}
			// 更新开关状态
			isSwitchOn = state;
			break;

		default:
			break;
		}
		invalidate(); // 重绘View
		return true; // 消费该事件
	}

	/** 设置背景图片 */
	public void setSwitchBackgroundResource(int switchBackgroundId) {
		switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switchBackgroundId);
	}

	/** 设置滑动按钮图片资源 */
	public void setSlideButtonResource(int slideButtonId) {
		slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButtonId);
	}

	/** 设置开关状态 */
	public void setSwitchState(boolean isSwitchOn) {
		this.isSwitchOn = isSwitchOn;
	}

	/** 提供方法：设置监听  */
	public void setOnSwitchStateChangedListener(OnSwitchStateChangedListener onSwitchStateChangedListener) {
		// 传入监听接口对象
		this.onSwitchStateChangedListener = onSwitchStateChangedListener;
	}
	
	/** 提供回调接口：监听状态接口  */
	public interface OnSwitchStateChangedListener {
		// 状态回调方法：监听本View的开关状态并传递给调用者使用
		void onStateChanged(boolean state);
	}

}
