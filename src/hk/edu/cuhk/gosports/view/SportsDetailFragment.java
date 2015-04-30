package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.database.GSProvider;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.utils.ViewUtil;

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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * sport detail
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-29 下午7:48:25
 * @version V1.0
 * 
 */
public class SportsDetailFragment extends BaseFragment implements Notify {

	private static final String TAG = "SportsDetailFragment";

	private int sportId = -1;
	TextView title, type, time, location, number, remarks;
	CheckBox attend;
	private boolean mBound;
	MainService mService;
	double latitude, longtitude;

	// private static Notify sportFragmentNotify;

	public SportsDetailFragment(Context context, int sportId) {
		super(context);
		this.sportId = sportId;
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

		View currentView = inflater.inflate(R.layout.fragment_display_sports,
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

	public void setSportId(int sportId) {
		this.sportId = sportId;
		refreshView();
	}

	private void intiView(View v) {

		title = (TextView) v
				.findViewById(R.id.fragment_display_sports_title_tv);
		type = (TextView) v.findViewById(R.id.fragment_display_sports_type_tv);
		time = (TextView) v.findViewById(R.id.fragment_display_sports_time_tv);
		location = (TextView) v
				.findViewById(R.id.fragment_display_sports_loc_tv);
		number = (TextView) v.findViewById(R.id.fragment_display_sports_num_tv);
		remarks = (TextView) v
				.findViewById(R.id.fragment_display_sports_remarks_tv);
		attend = (CheckBox) v
				.findViewById(R.id.fragment_display_sports_attend_ibtn);
		attend.setOnClickListener(new mOnClickListener());
		location.setOnClickListener(new mOnClickListener());
		// locButton.setOnClickListener(new mOnClickListener());
		refreshView();
	}

	private void refreshView() {
		if (sportId != -1) {
			GSProvider.init(context);
			Sport sport = GSProvider.getInstance().getSport(sportId);
			if (sport != null) {
				latitude = sport.getLatitude();
				longtitude = sport.getLongitude();
				this.sportId = sport.getSportID();
				title.setText(sport.getEventTitle());
				type.setText(ViewUtil.getSportName(sport.getSportType()));
				time.setText(sport.getStartTime());
				location.setText(sport.getLocation());
				number.setText(sport.getCurrentNum() + "/"
						+ sport.getExpectNum());
				remarks.setText(sport.getExtraInfo());
				attend.setChecked(sport.isJoined());
			}
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
		Messager msg = (Messager) params[0];
		if (msg != null) {
			if (msg.getType() == Task.LOAD_DETAIL_SPORTS) {
				if (msg.getStatus() == GSConstants.STATUS_OK) {
					Sport sport = (Sport) msg.getObject();
					sportId = sport.getSportID();
					title.setText(sport.getExtraInfo());
					type.setText(ViewUtil.getSportName(sport.getSportType()));
					time.setText(sport.getStartTime());
					location.setText(sport.getLocation());
					number.setText(sport.getCurrentNum() + "/"
							+ sport.getExpectNum());
					attend.setChecked(sport.isJoined());
				} else {
					Toast.makeText(getContext(), (String) msg.getObject(),
							Toast.LENGTH_SHORT).show();
				}
			}
			if (msg.getType() == Task.ATTEND_EVENTS
					|| msg.getType() == Task.QUIT_EVENT) {
				if (msg.getStatus() == GSConstants.STATUS_OK) {
					MainActivity activity = (MainActivity) getActivity();
					activity.switchMode(GSConstants.MENU_SPORT_LIST);
				} else {
					Toast.makeText(getContext(), (String) msg.getObject(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	class mOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.fragment_display_sports_attend_ibtn:
				if (mBound) {
					HashMap<String, Integer> taskParams = new HashMap<String, Integer>();
					taskParams.put("sportId", sportId);
					if (attend.isChecked()) {
						MainService.addNewTask(new Task(Task.ATTEND_EVENTS,
								taskParams, getContext()),
								(Notify) SportsDetailFragment.this);
					} else {
						MainService.addNewTask(new Task(Task.QUIT_EVENT,
								taskParams, getContext()),
								(Notify) SportsDetailFragment.this);

					}
				}
				break;
			case R.id.fragment_display_sports_loc_tv:
				if (latitude != 0.0) {
					MainActivity activity = (MainActivity) getActivity();
					activity.switchMode(GSConstants.MENU_DISPLAY_SPORT_ON_MAP,
							latitude, longtitude);
				}
				break;
			default:
				break;
			}
		}
	}

}
