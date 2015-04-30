package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultAlertDialog extends DefaultDialog implements
		OnClickListener {
	TextView tvTitle;
	private TextView tvContent;
	private Button btnCancel, btnSure;
	private DialogCallBack callBack;

	private ImageView ivContent;

	public interface DialogCallBack {
		void onCancel();

		void onSure();

	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param msg
	 */
	public DefaultAlertDialog(Context context) {
		super(context);
		setContentView(R.layout.view_dialog_alert_default);
		initView();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param theme
	 */
	public DefaultAlertDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private CheckBox checkBox;

	public DefaultAlertDialog showCheckBox(String checkText) {
		checkBox = (CheckBox) ((ViewStub) findViewById(R.id.view_stub))
				.inflate();
		checkBox.setText(checkText);
		return this;
	}

	/**
	 * 初始化viewPager
	 */
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvContent = (TextView) this.findViewById(R.id.tv_content);

		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		btnSure = (Button) this.findViewById(R.id.btn_sure);
		btnSure.setOnClickListener(this);

		ivContent = (ImageView) this.findViewById(R.id.iv_content);
	}

	public DefaultAlertDialog setContent(String content) {
		tvContent.setText(content);
		return this;
	}

	public DefaultAlertDialog setTitle(String title) {
		tvTitle.setText(title);
		return this;
	}

	public DefaultAlertDialog setCancelText(String title) {
		btnCancel.setText(title);
		return this;
	}

	public DefaultAlertDialog setSureText(String title) {
		btnSure.setText(title);
		return this;
	}

	/**
	 * 如果对话框中间只是图片，调用此方法设置图片的资源id。 会把原来显示文字内容的TextView设置为gone
	 * 
	 * @param imageId
	 * @return
	 */
	public DefaultAlertDialog setImage(int imageId) {
		tvContent.setVisibility(View.GONE);
		ivContent.setVisibility(View.VISIBLE);
		ivContent.setBackgroundResource(imageId);
		return this;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_cancel:
			if (callBack != null)
				callBack.onCancel();
			break;
		case R.id.btn_sure:
			if (callBack != null)
				callBack.onSure();
			break;
		}
	}

	public DialogCallBack getCallBack() {
		return callBack;
	}

	public DefaultAlertDialog setCallBack(DialogCallBack callBack) {
		this.callBack = callBack;
		return this;
	}

	public boolean getChecked() {
		if (checkBox != null)
			return checkBox.isChecked();
		return false;
	}
}
