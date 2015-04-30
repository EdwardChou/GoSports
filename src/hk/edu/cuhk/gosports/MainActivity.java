package hk.edu.cuhk.gosports;

import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.view.CreateSportsFragment;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import hk.edu.cuhk.gosports.view.LoginActivity;
import hk.edu.cuhk.gosports.view.SlidingLeftFragment;
import hk.edu.cuhk.gosports.view.SportsDetailFragment;
import hk.edu.cuhk.gosports.view.SportsFragment;
import hk.edu.cuhk.gosports.view.UserFragment;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Main activity
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-20 PM8:48:03
 * @version V1.0
 * 
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener, LocationListener {

	protected LocationManager locationManager;
	protected LocationListener locationListener;

	private final static String TAG = "MainActivity";
	ImageButton actionBarLeftBtn, actionBarRightBtn;
	private Fragment mContent;
	private Fragment sportsFragment;
	private Fragment userFragment;
	private Fragment createSportFragment;
	private Fragment sportDetailFragment;
	public SlidingMenu slidingMenu;

	public static int Mode = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		((Activity) LoginActivity.instance).finish();

		// location
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

		// initialize sports request
		if (GSApplication.user != null) {
			GSApplication.sportRequest.setUserId(GSApplication.user
					.getUserServerId());
		}
		GSApplication.sportRequest.setRange(10);
		// TODO
		GSApplication.sportRequest.setExpectTimeStart(new Date().getTime());
		// retrieve one week events
		GSApplication.sportRequest
				.setExpectTimeEnd(new Date().getTime() + 7 * 24 * 3600000);
		GSApplication.sportRequest.setLoadSportType(GSConstants.SPORT_TYPE_ALL);

		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.50f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		sportsFragment = new SportsFragment(this);
		userFragment = new UserFragment(this);
		createSportFragment = new CreateSportsFragment(this);
		mContent = sportsFragment;

		setContentView(R.layout.activity_main);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_main_frame_home, sportsFragment)
				.commit();

		setBehindContentView(R.layout.view_frame_home);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.view_frame_home_menu_left,
						new SlidingLeftFragment()).commit();

		slidingMenu.setMode(SlidingMenu.LEFT);
		switchContent(mContent, sportsFragment);

		TextView text = (TextView) findViewById(R.id.actionbar_title);
		text.setText("GoSports");

		// ImageView actionbarMenuIcon = (ImageView)
		// findViewById(R.id.actionbar_menu_icon);
		// actionbarMenuIcon.setVisibility(View.VISIBLE);

		actionBarLeftBtn = (ImageButton) findViewById(R.id.actionbar_left_ibtn);
		actionBarLeftBtn.setOnClickListener(this);
		actionBarRightBtn = (ImageButton) findViewById(R.id.actionbar_right_ibtn);
		actionBarRightBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			mContent = to;
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.activity_main_frame_home, to)
						.commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}

	public void switchMode(int type) {
		this.switchMode(type, 0);
	}

	public void switchMode(int type, int sportID) {
		switch (type) {
		case GSConstants.MENU_SPORT_LIST:
			switchContent(mContent, sportsFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_menu_white_36dp);
			actionBarRightBtn
					.setBackgroundResource(R.drawable.ic_add_white_36dp);
			actionBarRightBtn.setVisibility(View.VISIBLE);
			((SportsFragment) sportsFragment).pager.setCurrentItem(0);
			Mode = GSConstants.MENU_SPORT_LIST;
			break;
		case GSConstants.MENU_SPORT_MAP:
			switchContent(mContent, sportsFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_menu_white_36dp);
			actionBarRightBtn
					.setBackgroundResource(R.drawable.ic_add_white_36dp);
			actionBarRightBtn.setVisibility(View.VISIBLE);
			((SportsFragment) sportsFragment).pager.setCurrentItem(1);
			Mode = GSConstants.MENU_SPORT_MAP;
			break;
		case GSConstants.MENU_SPORT_DETAIL:
			if (sportDetailFragment == null) {
				sportDetailFragment = new SportsDetailFragment(
						getApplicationContext(), sportID);
			} else {				
				((SportsDetailFragment) sportDetailFragment).setSportId(sportID);
			}
			switchContent(mContent, sportDetailFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_arrow_back_white_36dp);
			actionBarRightBtn.setVisibility(View.GONE);
			Mode = GSConstants.MENU_SPORT_DETAIL;
			break;
		case GSConstants.MENU_SPORT_ADD:
			switchContent(mContent, createSportFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_arrow_back_white_36dp);
			actionBarRightBtn
					.setBackgroundResource(R.drawable.ic_check_white_36dp);
			actionBarRightBtn.setVisibility(View.VISIBLE);
			Mode = GSConstants.MENU_SPORT_ADD;
			break;
		case GSConstants.MENU_SPORT_SELECT_LOC:
			switchContent(mContent, sportsFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_close_white_36dp);
			actionBarRightBtn
					.setBackgroundResource(R.drawable.ic_check_white_36dp);
			actionBarRightBtn.setVisibility(View.VISIBLE);
			((SportsFragment) sportsFragment).pager.setCurrentItem(1);
			Mode = GSConstants.MENU_SPORT_SELECT_LOC;
			break;
		case GSConstants.MENU_USER:
			switchContent(mContent, userFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_arrow_back_white_36dp);
			actionBarRightBtn.setVisibility(View.GONE);
			Mode = GSConstants.MENU_USER;
			break;
		case GSConstants.MENU_DONE_ADD_RETURN_SPORT_LIST:
			((SportsFragment) sportsFragment).handler.sendEmptyMessage(0);
			((SportsFragment) sportsFragment).handler.sendEmptyMessage(1);
			switchContent(mContent, sportsFragment);
			actionBarLeftBtn
					.setBackgroundResource(R.drawable.ic_menu_white_36dp);
			actionBarRightBtn
					.setBackgroundResource(R.drawable.ic_add_white_36dp);
			actionBarRightBtn.setVisibility(View.VISIBLE);
			((SportsFragment) sportsFragment).pager.setCurrentItem(0);
			Mode = GSConstants.MENU_SPORT_LIST;
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_left_ibtn:
			if (Mode == GSConstants.MENU_SPORT_MAP
					|| Mode == GSConstants.MENU_SPORT_LIST) {
				slidingMenu.toggle();
			} else {
				switchMode(GSConstants.MENU_SPORT_LIST);
			}
			break;
		case R.id.actionbar_right_ibtn:
			if (Mode == GSConstants.MENU_SPORT_LIST
					|| Mode == GSConstants.MENU_SPORT_MAP) {
				// if current mode is displaying sports, the right button is
				// adding sports
				switchMode(GSConstants.MENU_SPORT_ADD);
			} else if (Mode == GSConstants.MENU_SPORT_SELECT_LOC) {
				// if current mode is selecting location for create sports, the
				// right button is finish selecting location and return to
				// Create Sports Fragment
				switchMode(GSConstants.MENU_SPORT_ADD);
			} else if (Mode == GSConstants.MENU_SPORT_ADD) {
				// if current mode is creating sports, the right button is
				// submit create new sports to server
				((Notify) createSportFragment).refresh(new Messager(
						Task.ADD_NEW_SPORT_TO_SERVER, GSConstants.STATUS_OK,
						null));
			} else if (Mode == GSConstants.MENU_SPORT_SELECT_LOC) {
				((Notify) createSportFragment).refresh(new Messager(
						Task.ADD_NEW_SPORT_TO_SERVER, GSConstants.STATUS_OK,
						null));
				switchMode(GSConstants.MENU_SPORT_ADD);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		GSApplication.sportRequest.setLatitude(location.getLatitude());
		GSApplication.sportRequest
				.setLongitude(location.getLongitude());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mContent == sportsFragment) {
				DefaultAlertDialog uploadDialog = new DefaultAlertDialog(this);
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
			} else {
				switchContent(mContent, sportsFragment);
			}
		}
		return false;
	}
}
