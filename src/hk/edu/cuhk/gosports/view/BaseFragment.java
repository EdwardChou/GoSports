package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Base Fragment with title bar
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-20 PM9:03:52
 * @version V1.0
 * 
 */
public class BaseFragment extends Fragment {

	public Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private ImageButton actionbar_back_btn;
	private Button actionbar_btn;
	private ImageButton actionbar_imgbtn;

	private EditText actionbar_edittext;

	public BaseFragment(Context context) {
		this.context = context;
	}

	public void setTitle(View v, String s) {
		// AssetManager mgr = context.getAssets();
		// Typeface tf = Typeface.createFromAsset(mgr,
		// "fonts/Roboto-Light.ttf");
		TextView text = (TextView) v.findViewById(R.id.actionbar_title);
		// text.setTypeface(tf);
		text.setText(s);
	}

	public boolean setBackImgButton(View v, int backgroundResource) {
		if (backgroundResource != 0) {
			actionbar_back_btn = (ImageButton) v
					.findViewById(R.id.actionbar_left_ibtn);
			actionbar_back_btn.setVisibility(View.VISIBLE);
			actionbar_imgbtn.setBackgroundResource(backgroundResource);
			return true;
		}
		return false;
	}

	public boolean setActionbarButton(View v, String title) {
		if (!"".equals(title)) {
			actionbar_btn = (Button) v.findViewById(R.id.actionbar_right_btn);
			actionbar_btn.setVisibility(View.VISIBLE);
			actionbar_btn.setText(title);
			return true;
		}
		return false;
	}

	public boolean setActionbarImgButton(View v, int backgroundResource) {
		if (backgroundResource != 0) {
			actionbar_imgbtn = (ImageButton) v
					.findViewById(R.id.actionbar_right_ibtn);
			actionbar_imgbtn.setVisibility(View.VISIBLE);
			actionbar_imgbtn.setBackgroundResource(backgroundResource);
			return true;
		}
		return false;
	}

	public Button getActionbarButton() {
		return actionbar_btn;
	}

	public ImageButton getActionbarImgButton() {
		return actionbar_imgbtn;
	}

	public ImageButton getBackImgButton() {
		return actionbar_back_btn;
	}

	public boolean setEditText(View v, String hint) {

		TextView text = (TextView) v.findViewById(R.id.actionbar_title);
		text.setVisibility(View.INVISIBLE);
		actionbar_edittext = (EditText) v.findViewById(R.id.search_element_edt);
		actionbar_edittext.setVisibility(View.VISIBLE);

		if (hint != "") {
			actionbar_edittext.setHint(hint);
		}
		return true;
	}

	public EditText getEditText() {
		return actionbar_edittext;
	}

	public Button getSearchAddButton(View v) {
		Button add = (Button) v.findViewById(R.id.search_element_add_btn);
		return add;
	}
}
