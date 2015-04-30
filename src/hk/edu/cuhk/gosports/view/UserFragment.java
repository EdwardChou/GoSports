package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.GSApplication;
import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.User;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.GSConstants;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * user info fragment
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午5:43:50
 * @version V1.0
 * 
 */
public class UserFragment extends BaseFragment implements Notify {

	private static final String TAG = UserFragment.class.getSimpleName();

	ImageView headImageView;
	TextView nameAndGenderTv, ageTv, emailTv, descriptionTv;
	RatingBar creditRb;

	MainService mService;
	private boolean mBound;

	public UserFragment(Context context) {
		super(context);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;

			default:
				break;
			}

		};
	};

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

		MainActivity activity = (MainActivity) getActivity();
		activity.switchMode(GSConstants.MENU_USER);
		View currentView = inflater.inflate(R.layout.fragment_user, null);
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

	private void intiView(View v) {
		headImageView = (ImageView) v.findViewById(R.id.fragment_user_head_iv);
		nameAndGenderTv = (TextView) v.findViewById(R.id.fragment_user_name_tv);
		ageTv = (TextView) v.findViewById(R.id.fragment_user_age_tv);
		emailTv = (TextView) v.findViewById(R.id.fragment_user_email_tv);
		creditRb = (RatingBar) v.findViewById(R.id.fragment_user_credit_rb);
		descriptionTv = (TextView) v
				.findViewById(R.id.fragment_user_description_tv);

		ImageButton profileButton = (ImageButton) v
				.findViewById(R.id.fragment_user_edit_profile_imbtn);
		RelativeLayout launchedEvents = (RelativeLayout) v
				.findViewById(R.id.fragment_user_launched_event_rl);
		RelativeLayout joinedEvents = (RelativeLayout) v
				.findViewById(R.id.fragment_user_joined_events_rl);
		RelativeLayout myClub = (RelativeLayout) v
				.findViewById(R.id.fragment_user_my_club_rl);
		RelativeLayout myCredit = (RelativeLayout) v
				.findViewById(R.id.fragment_user_my_credit_rl);
		RelativeLayout myRating = (RelativeLayout) v
				.findViewById(R.id.fragment_user_my_rating_rl);
		RelativeLayout setting = (RelativeLayout) v
				.findViewById(R.id.fragment_user_setting_rl);

		profileButton.setOnClickListener(new mOnClickListener());
		launchedEvents.setOnClickListener(new mOnClickListener());
		joinedEvents.setOnClickListener(new mOnClickListener());
		myClub.setOnClickListener(new mOnClickListener());
		myCredit.setOnClickListener(new mOnClickListener());
		myRating.setOnClickListener(new mOnClickListener());
		setting.setOnClickListener(new mOnClickListener());
		User user = GSApplication.user;
		if (user != null) {
			// headImageView
			if (user.getSex() == GSConstants.FEMALE) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.ic_female);
				// important step
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				nameAndGenderTv
						.setCompoundDrawables(drawable, null, null, null);
			}
			nameAndGenderTv.setText(user.getUsername());
			ageTv.setText(user.getAge() + "");
			emailTv.setText(user.getMailbox());
			descriptionTv.setText(user.getDescription());
			creditRb.setRating((float) (user.getCredit() / 5.0));
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {

	}

	class mOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO
			switch (v.getId()) {
			case R.id.fragment_user_edit_profile_imbtn:
				// if (mBound) {
				// MainService.addNewTask(
				// new Task(Task.USER_INFO, null, getActivity()),
				// UserFragment.this);
				// }
				break;
			case R.id.fragment_user_launched_event_rl:
				break;
			case R.id.fragment_user_joined_events_rl:
				break;
			case R.id.fragment_user_my_club_rl:
				break;
			case R.id.fragment_user_my_credit_rl:
				break;
			case R.id.fragment_user_my_rating_rl:
				break;
			case R.id.fragment_user_setting_rl:
				break;
			default:
				break;
			}
		}
	}

}
