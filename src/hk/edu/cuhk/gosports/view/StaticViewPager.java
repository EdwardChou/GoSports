package hk.edu.cuhk.gosports.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * no slide view pager
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-5-1 下午1:52:13
 * @version V1.0
 * 
 */
public class StaticViewPager extends ViewPager {

	// private static final String TAG = "StaticViewPager";
	private boolean isCanScroll = false;

	public StaticViewPager(Context context) {
		super(context);
	}

	public StaticViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isCanScroll)
			return super.onInterceptTouchEvent(arg0);
		else
			return false;
	}

	// @SuppressLint("ClickableViewAccessibility")
	// @Override
	// public boolean onTouchEvent(MotionEvent arg0) {
	// if (isCanScroll)
	// return super.onTouchEvent(arg0);
	// else
	// return false;
	// }

	public void setIsScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	// @Override
	// public void scrollTo(int x, int y) {
	// if (isCanScroll) {
	// super.scrollTo(x, y);
	// }
	// }
}
