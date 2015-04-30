package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.utils.ViewUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

public class DefaultDialog extends Dialog {

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param msg
	 */
	public DefaultDialog(Context context) {
		super(context, R.style.MyTheme_Dialog);

	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param theme
	 */
	public DefaultDialog(Context context, int theme) {
		super(context, theme);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void show() {
		super.show();
		android.view.WindowManager.LayoutParams params = getWindow()
				.getAttributes();
		params.height = LayoutParams.WRAP_CONTENT;
		float w = ViewUtil.getWindowWidth(getContext());
		float h = ViewUtil.getWindowHeight(getContext());
		params.width = (int) (w > h ? h * 0.9 : w * 0.9);
		getWindow().setAttributes(params);
	}
}
