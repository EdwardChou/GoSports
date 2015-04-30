package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.GSApplication;
import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.model.TimeBean;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.utils.TimeUtil;
import hk.edu.cuhk.gosports.utils.ViewUtil;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import hk.edu.cuhk.gosports.view.wheelview.SportTypesPickDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateSportsFragment extends BaseFragment implements Notify {

	private static final String TAG = "CreateSportsFragment";

	private TimeBean tBean = new TimeBean(Calendar.getInstance(), 0);
	Button timeButton, locButton, typeButton;
	EditText locEditText, titleEditText, remarksEditText, expectNumEditText;
	MainService mService;
	private boolean mBound;
	int sportType;

	// private static Notify sportFragmentNotify;

	public CreateSportsFragment(Context context) {
		super(context);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// check whether service is running
		if (MainService.isRun == true) {
			Log.i(TAG, "MainService run");
		} else {
			Log.i(TAG, "MainService not run");
			// bind service to activity
			Intent intent = new Intent(getContext(), MainService.class);
			getContext().bindService(intent, mConnection,
					Context.BIND_AUTO_CREATE);
		}

		View currentView = inflater.inflate(R.layout.fragment_create_sports,
				null);
		intiView(currentView);
		return currentView;
	}

	@Override
	public void onStart() {
		super.onStart();
		// when start, bing service
		getContext().bindService(new Intent(getContext(), MainService.class),
				this.mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			getContext().unbindService(mConnection);
			mBound = false;
		}
	}

	// public static void addNotify(Context context) {
	// sportFragmentNotify = (Notify) context;
	// }

	private void intiView(View v) {

		titleEditText = (EditText) v
				.findViewById(R.id.fragment_create_sports_title_et);
		locEditText = (EditText) v
				.findViewById(R.id.fragment_create_sports_loc_et);
		expectNumEditText = (EditText) v
				.findViewById(R.id.fragment_create_sports_num_et);
		remarksEditText = (EditText) v
				.findViewById(R.id.fragment_create_sports_remarks_et);
		typeButton = (Button) v
				.findViewById(R.id.fragment_create_sports_type_btn);
		timeButton = (Button) v
				.findViewById(R.id.fragment_create_sports_time_btn);
		timeButton.setText(TimeUtil.transferDateToString(new Date()));
		locButton = (Button) v
				.findViewById(R.id.fragment_create_sports_loc_btn);
		if (SportsFragment.latestNewMarker != null) {
			locButton.setVisibility(View.GONE);
			locEditText.setVisibility(View.VISIBLE);
		}
		typeButton.setOnClickListener(new mOnClickListener());
		timeButton.setOnClickListener(new mOnClickListener());
		locButton.setOnClickListener(new mOnClickListener());
	}

	@Override
	public void onResume() {
		if (SportsFragment.latestNewMarker != null) {
			locButton.setVisibility(View.GONE);
			locEditText.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
		Messager msg = (Messager) params[0];
		if (msg != null) {
			if (msg.getType() == Task.DONE_SELECT_LOC) {
				if (SportsFragment.latestNewMarker != null) {
					locButton.setVisibility(View.GONE);
					locEditText.setVisibility(View.VISIBLE);
				}
			}
			if (msg.getType() == Task.DONE_ADD_NEW_SPORT_TO_SERVER) {
				if (msg.getStatus() == GSConstants.STATUS_OK) {
					Toast.makeText(context, "Success insert sport to Server",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, (String) msg.getObject(),
							Toast.LENGTH_SHORT).show();
				}
				locEditText.setText("");
				titleEditText.setText("");
				remarksEditText.setText("");
				expectNumEditText.setText("");
				MainActivity activity = (MainActivity) getActivity();
				activity.switchMode(GSConstants.MENU_DONE_ADD_RETURN_SPORT_LIST);
			}
			if (msg.getType() == Task.ADD_NEW_SPORT_TO_SERVER) {
				HashMap<String, Sport> para = new HashMap<String, Sport>();
				Sport sport = new Sport();
				sport.setSportType(sportType);
				sport.setCreateTime(TimeUtil.transferDateToString(new Date()));
				sport.setCreateUserID(GSApplication.user.getUserServerId());
				sport.setExpectNum(Integer.parseInt(expectNumEditText.getText()
						.toString()));
				sport.setEventTitle(titleEditText.getText().toString() != null ? titleEditText
						.getText().toString() : "");
				sport.setExtraInfo(remarksEditText.getText().toString() != null ? remarksEditText
						.getText().toString() : "");
				sport.setStartTime("Now"
						.equals(timeButton.getText().toString()) ? TimeUtil
						.transferDateToString(new Date()) : timeButton
						.getText().toString());
				sport.setLocation(locEditText.getText().toString() != null ? locEditText
						.getText().toString() : "");
				sport.setLongitude(SportsFragment.latestNewMarker.getPosition().longitude);
				sport.setLatitude(SportsFragment.latestNewMarker.getPosition().latitude);
				para.put("sport", sport);
				if (mBound) {
					MainService.addActivity(CreateSportsFragment.this);
					MainService.addNewTask(new Task(
							Task.DONE_ADD_NEW_SPORT_TO_SERVER, para,
							getContext()));
				}
			}
		}
	}

	class mOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.fragment_create_sports_type_btn:
				final SportTypesPickDialog typeDialog = new SportTypesPickDialog(
						getActivity());
				typeDialog.setCallBack(new DialogCallBack() {

					@Override
					public void onSure() {
						sportType = typeDialog.getSportType();
						typeButton.setText(ViewUtil.getSportName(sportType));
					}

					@Override
					public void onCancel() {
					}
				});
				typeDialog.show();
				break;
			case R.id.fragment_create_sports_time_btn:
				final TimePickDialog dialog = new TimePickDialog(getActivity(),
						tBean);
				dialog.setCallBack(new DialogCallBack() {

					@Override
					public void onSure() {
						tBean = dialog.getBookDate();
						timeButton.setText(dialog.getBookDate() == null
								|| dialog.getBookDate().isImm() ? "Now"
								: dialog.getBookDate().toStringForSport());
					}

					@Override
					public void onCancel() {
					}
				});
				dialog.show();
				break;
			case R.id.fragment_create_sports_loc_btn:
				MainActivity activity = (MainActivity) getActivity();
				activity.switchMode(GSConstants.MENU_SPORT_SELECT_LOC);
				// sportFragmentNotify.refresh(new Messager(Task.SELECT_LOC, 0,
				// null));
				break;
			default:
				break;
			}
		}
	}

}
