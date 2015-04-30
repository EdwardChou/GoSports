package hk.edu.cuhk.gosports.view.wheelview;

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import hk.edu.cuhk.gosports.view.DefaultDialog;
import hk.edu.cuhk.gosports.view.wheelview.adapter.AbstractWheelTextAdapter;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * sport type picker
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-29 下午10:47:46
 * @version V1.0
 * 
 */
public class SportTypesPickDialog extends DefaultDialog implements
		OnClickListener {

	private Context context;

	private DialogCallBack callBack;
	private WheelView typeWheelView;
	private Button cancelBtn, sureBtn;
	private TextView typeTv;

	private TypeWheelAdapter typeAdapter;

	public SportTypesPickDialog(Context context) {
		super(context);
		this.context = context;
		setContentView(R.layout.view_sport_type_picker);
		initView();
	}

	public void initView() {
		typeWheelView = (WheelView) findViewById(R.id.view_sport_type_wv);
		cancelBtn = (Button) findViewById(R.id.view_sport_type_cancel_btn);
		sureBtn = (Button) findViewById(R.id.view_sport_type_sure_btn);

		cancelBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);

		typeAdapter = new TypeWheelAdapter(context);
		typeWheelView.setViewAdapter(typeAdapter);
		typeWheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				if (wheel.getCurrentItem() == 1) {
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_sport_type_cancel_btn:
			if (callBack != null)
				callBack.onCancel();
			break;
		case R.id.view_sport_type_sure_btn:
			if (callBack != null)
				callBack.onSure();
			break;
		default:
			break;
		}
		dismiss();
	}
	
	public int getSportType() {
		return typeWheelView.getCurrentItem() + 1;
	}

	public DialogCallBack getCallBack() {
		return callBack;
	}

	public SportTypesPickDialog setCallBack(DialogCallBack callBack) {
		this.callBack = callBack;
		return this;
	}

	private class TypeWheelAdapter extends AbstractWheelTextAdapter {

		private final String[] types = { "Badminton", "Tennis", "Football",
				"Basketball", "Bike", "Table tennis", "Billiards", "Running",
				"Hiking", "Swimming", "Gym", "Dance", "Camping", "Golf",
				"Others" };

		protected TypeWheelAdapter(Context context) {
			super(context, R.layout.view_type_picker_item);
		}

		@Override
		public int getItemsCount() {
			return types.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			if (index >= 0 && index < getItemsCount()) {
				return types[index];
			}
			return null;
		}

		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			View v = super.getItem(index, convertView, parent);
			typeTv = (TextView) v.findViewById(R.id.view_type_picker_item_tv);
			typeTv.setText(getBoldFont(types[index]));
			return v;
		}

		public SpannableStringBuilder getBoldFont(String str) {
			SpannableStringBuilder extraStr = new SpannableStringBuilder(str);
			extraStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
					str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return extraStr;
		}

	}

}
