package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * sliding menu
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-27 下午2:06:42
 * @version V1.0
 * 
 */
public class SlidingLeftFragment extends Fragment implements OnClickListener {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_left, null);
		Button sport = (Button) view
				.findViewById(R.id.fragment_menu_left_sports_btn);
		Button user = (Button) view
				.findViewById(R.id.fragment_menu_left_user_btn);
		Button exit = (Button) view
				.findViewById(R.id.fragment_menu_left_exit_btn);
		sport.setOnClickListener(this);
		user.setOnClickListener(this);
		exit.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		MainActivity activity = (MainActivity) getActivity();
		switch (v.getId()) {
		case R.id.fragment_menu_left_sports_btn:
			activity.slidingMenu.toggle();
			activity.switchMode(GSConstants.MENU_SPORT_LIST);
			break;
		case R.id.fragment_menu_left_user_btn:
			activity.slidingMenu.toggle();
			activity.switchMode(GSConstants.MENU_USER);
			break;
		case R.id.fragment_menu_left_exit_btn:
			activity.slidingMenu.toggle();
			DefaultAlertDialog uploadDialog = new DefaultAlertDialog(
					getActivity());
			uploadDialog.setTitle("提示").setContent("是否退出？")
					.setCallBack(new DialogCallBack() {
						@Override
						public void onCancel() {
						}

						@Override
						public void onSure() {
							System.exit(0);
						}
					}).show();
			break;
		default:
			break;
		}
	}

}
