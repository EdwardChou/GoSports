package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.controller.SportsFragmentAdapter;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.utils.NetworkUtil;
import hk.edu.cuhk.gosports.utils.TaskDetailOperation;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * list view and map view of sports
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-21 PM10:22:02
 * @version V1.0
 * 
 */
public class SportsFragment extends BaseFragment implements Notify {

	private static final String TAG = "SportsFragment";

	public ViewPager pager;
	private ImageView leftCursor, rightCursor;
	private List<View> views;

	private SportsFragmentAdapter adapter;
	private ProgressDialog dialog;
	private PullToRefreshListView refresh;

	boolean locationMovableMode = true;
	public static Marker latestNewMarker = null;
	MainService mService;
	private boolean mBound;

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	// TODO private int type = -1;
	BitmapDescriptor mCurrentMarker;

	private InfoWindow mInfoWindow;
	ImageButton requestLocButton;
	Button positionButton, refresBtn;
	LatLng selfll;

	/**
	 * 当前地点击点
	 */
	private LatLng currentPt;

	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位

	public SportsFragment(Context Context) {
		super(Context);
	}

	public void setLocationMode(boolean displaySelectionLocationMode) {
		this.locationMovableMode = displaySelectionLocationMode;
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (latestNewMarker != null) {
					latestNewMarker.remove();
				}
				adapter.refreshData();
				adapter.notifyDataSetChanged();
				refresh.onRefreshComplete();
				break;
			case 1:
				List<Sport> sports = null;
				Messager messager = TaskDetailOperation
						.LoadSportsFromDB(context);
				if (messager.getStatus() == GSConstants.STATUS_OK) {
					sports = (List<Sport>) messager.getObject();
					for (Sport sport : sports) {
						Log.e("ADD MAP POINT======",
								"longtitude=" + sport.getLongitude()
										+ ",latitude=" + sport.getLatitude());
						addMarkerOnMap(sport.getLongitude(),
								sport.getLatitude(), sport.getSportType(),
								sport.getSportID());
					}
				}
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

		View v = inflater.inflate(R.layout.fragment_sports, null);

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

		pager = (ViewPager) v.findViewById(R.id.sports_fragment_viewpage);
		TextView leftTitle = (TextView) v
				.findViewById(R.id.sports_fragment_left_title);
		TextView rightTitle = (TextView) v
				.findViewById(R.id.sports_fragment_right_title);
		leftCursor = (ImageView) v
				.findViewById(R.id.sports_fragment_left_cursor);
		rightCursor = (ImageView) v
				.findViewById(R.id.sports_fragment_right_cursor);

		leftTitle.setOnClickListener(new MyOnClickListener());
		rightTitle.setOnClickListener(new MyOnClickListener());

		leftTitle.setText("List");
		rightTitle.setText("Map");
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.view_pull_to_refresh, null));
		views.add(inflater.inflate(R.layout.fragment_map, null));
		pager.setAdapter(new mPagerAdapter());
		pager.setOnPageChangeListener(new mOnPageChangeListener());
		return v;
	}

	@Override
	public void init() {
	}

	@Override
	public void onStart() {
		super.onStart();
		// when start, bing service
		getContext().bindService(new Intent(getContext(), MainService.class),
				this.mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mMapView != null)
			mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mMapView != null)
			mMapView.onResume();
		if (NetworkUtil.isNetworkAvailable(getActivity())) {
			if (refresBtn != null) {
				refresh.setVisibility(View.VISIBLE);
				refresBtn.setVisibility(View.GONE);
			}
		} else {
			if (refresBtn != null) {
				refresh.setVisibility(View.GONE);
				refresBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			getContext().unbindService(mConnection);
			mBound = false;
		}
		// 退出时销毁定位
		mLocClient.stop();
		// bdA.recycle();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// mMapView.onDestroy();
		if (mMapView != null)
			mMapView = null;
	}

	@Override
	public void refresh(Object... params) {
		Messager msg = (Messager) params[0];
		if (msg.getType() == Task.LOAD_NET_SPORTS) {
			if (msg.getStatus() == GSConstants.STATUS_OK) {
				dialog.dismiss();
				handler.sendEmptyMessage(0);
			} else {
				dialog.dismiss();
				Toast.makeText(getContext(), (String) msg.getObject(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class mPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			switch (position) {
			case 0:
				prepareListview(view);
				break;
			case 1:
				prepareMap(view);
				break;
			}
			return view;
		}

	}

	private void prepareListview(View v) {
		ListView listView;
		refresBtn = (Button) v.findViewById(R.id.pull_refresh_list_btn);
		refresh = (PullToRefreshListView) v
				.findViewById(R.id.pull_refresh_list);
		refresh.setShowIndicator(false);
		refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (mBound) {
					/**
					 * progress bar
					 */
					dialog = new ProgressDialog(getContext());
					dialog.setMessage("Loading, wait for seconds...");
					dialog.show();
					MainService.addActivity(SportsFragment.this);
					MainService.addNewTask(new Task(Task.LOAD_NET_SPORTS, null,
							getActivity()));
					handler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});
		listView = refresh.getRefreshableView();
		listView.setDivider(null);
		adapter = new SportsFragmentAdapter(getActivity());
		listView.setAdapter(adapter);
		refresh.setRefreshing(false);
	}

	private void prepareMap(View view) {
		Log.i(TAG, "prepareMap() start");
		// 初始化界面（按钮）
		requestLocButton = (ImageButton) view
				.findViewById(R.id.fragment_map_type_btn);
		requestLocButton.setOnClickListener(new MyOnClickListener());

		positionButton = (Button) view
				.findViewById(R.id.fragment_map_my_position_btn);
		positionButton.setOnClickListener(new MyOnClickListener());

		// 地图初始化
		mMapView = (MapView) view.findViewById(R.id.fragment_map_bmapview);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.stop();

		// load sport from database and add to map view
		handler.sendEmptyMessage(1);

		// 地图长按响应事件
		mBaiduMap.setOnMapLongClickListener(new MyOnMapLongClickListener());
		mBaiduMap.setOnMapClickListener(new MyOnMapClickListener());
		// marker图标的响应事件
		mBaiduMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
	}

	class MyOnMapClickListener implements OnMapClickListener {

		@Override
		public void onMapClick(LatLng arg0) {
			mBaiduMap.hideInfoWindow();
		}

		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			return false;
		}

	}

	/**
	 * view pager change listener
	 * 
	 * @author EdwardChou edwardchou_gmail_com
	 * @date 2015-4-22 下午4:07:50
	 * @version V1.0
	 * 
	 */
	class mOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				leftCursor.setVisibility(View.VISIBLE);
				rightCursor.setVisibility(View.GONE);
				break;
			case 1:
				leftCursor.setVisibility(View.GONE);
				rightCursor.setVisibility(View.VISIBLE);
				break;
			}
		}
	}

	/**
	 * location SDK listener
	 * 
	 * @author EdwardChou edwardchou_gmail_com
	 * @date 2015-4-26 下午4:34:25
	 * @version V1.0
	 * 
	 */
	public class MyLocationListenner implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			Log.e(TAG, locData.latitude + "");
			Log.e(TAG, locData.longitude + "");

			if (locationMovableMode) {
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				selfll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * map long click listener, when long click, add new marker on map
	 * 
	 * @author EdwardChou edwardchou_gmail_com
	 * @date 2015-4-26 下午5:14:53
	 * @version V1.0
	 * 
	 */
	public class MyOnMapLongClickListener implements OnMapLongClickListener {
		@Override
		public void onMapLongClick(LatLng point) {
			currentPt = point;
			BitmapDescriptor bdA = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_map_location);
			OverlayOptions ooA = new MarkerOptions().position(currentPt)
					.icon(bdA).zIndex(1).draggable(true);
			if (latestNewMarker != null) {
				latestNewMarker.remove();
			}
			latestNewMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			Bundle bundle = new Bundle();
			bundle.putBoolean("isnew", true);
			latestNewMarker.setExtraInfo(bundle);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(currentPt);
			mBaiduMap.animateMapStatus(u);
			Log.e(TAG, "setOnMapLongClickListener=" + currentPt.latitude + ","
					+ "setOnMapLongClickListener=" + currentPt.longitude);
			popupButton(latestNewMarker);
		}
	}

	public void moveToLocation(double latitude, double longtitude) {
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(
				latitude, longtitude));
		mBaiduMap.animateMapStatus(u);
		setLocationMode(false);
	}

	public class MyOnMarkerClickListener implements OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(final Marker marker) {
			popupButton(marker);
			return true;
		}
	}

	private void popupButton(Marker marker) {
		final Button button = new Button(getContext());
		button.setBackgroundResource(R.drawable.map_text);
		button.setTextColor(Color.WHITE);

		Bundle bundle = marker.getExtraInfo();
		final boolean isNew = bundle.getBoolean("isnew");
		final int sportId = bundle.getInt("sportid");
		if (isNew) {
			button.setText("New Activity");
		} else {
			button.setText("Show detail");
		}
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivity context = (MainActivity) getActivity();
				if (isNew) {
					// add new activity
					context.switchMode(GSConstants.MENU_SPORT_ADD);
				} else {
					// detail page
					context.switchMode(GSConstants.MENU_SPORT_DETAIL, sportId);
				}
				mBaiduMap.hideInfoWindow();
			}
		});
		LatLng ll = marker.getPosition();
		mInfoWindow = new InfoWindow(button, ll, -80);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	public Marker addMarkerOnMap(double longtitude, double latitude,
			int sportType, int sportId) {
		Marker marker = null;
		LatLng latLng = new LatLng(latitude, longtitude);
		marker = addMarkerOnMap(latLng, sportType, sportId);
		return marker;
	}

	public Marker addMarkerOnMap(LatLng latLng, int sportType, int sportId) {
		Marker marker = null;
		int resLogo = 0;
		switch (sportType) {
		case GSConstants.SPORT_TYPE_BADMINTON:
			resLogo = R.drawable.yueyundong5_16;
			break;
		case GSConstants.SPORT_TYPE_TENNIS:
			resLogo = R.drawable.yueyundong5_24;
			break;
		case GSConstants.SPORT_TYPE_FOOTBALL:
			resLogo = R.drawable.yueyundong5_32;
			break;
		case GSConstants.SPORT_TYPE_BASKETBALL:
			resLogo = R.drawable.yueyundong5_40;
			break;
		case GSConstants.SPORT_TYPE_BIKE:
			resLogo = R.drawable.yueyundong5_49;
			break;
		case GSConstants.SPORT_TYPE_TABLE_TENNIS:
			resLogo = R.drawable.yueyundong5_57;
			break;
		case GSConstants.SPORT_TYPE_BILLIARDS:
			resLogo = R.drawable.yueyundong5_64;
			break;
		case GSConstants.SPORT_TYPE_RUNNING:
			resLogo = R.drawable.yueyundong5_72;
			break;
		case GSConstants.SPORT_TYPE_HIKING:
			resLogo = R.drawable.yueyundong5_80;
			break;
		case GSConstants.SPORT_TYPE_SWIMMING:
			resLogo = R.drawable.yueyundong5_88;
			break;
		case GSConstants.SPORT_TYPE_GYM:
			resLogo = R.drawable.yueyundong5_97;
			break;
		case GSConstants.SPORT_TYPE_DANCE:
			resLogo = R.drawable.yueyundong5_104;
			break;
		case GSConstants.SPORT_TYPE_CAMP:
			resLogo = R.drawable.yueyundong5_120;
			break;
		case GSConstants.SPORT_TYPE_GOLF:
			resLogo = R.drawable.yueyundong5_128;
			break;
		case GSConstants.SPORT_TYPE_OTHERS:
			resLogo = R.drawable.yueyundong5_136;
			break;
		}
		BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(resLogo);
		OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA)
				.zIndex(9).draggable(true);
		marker = (Marker) (mBaiduMap.addOverlay(ooA));
		Bundle bundle = new Bundle();
		bundle.putBoolean("isnew", false);
		bundle.putInt("sportid", sportId);
		marker.setExtraInfo(bundle);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(u);
		return marker;
	}

	public class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sports_fragment_left_title:
				pager.setCurrentItem(0);
				break;
			case R.id.sports_fragment_right_title:
				pager.setCurrentItem(1);
				break;
			case R.id.fragment_map_type_btn:
				// TODO add type
				break;
			case R.id.fragment_map_my_position_btn:
				if (selfll == null) {
					return;
				}
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(selfll);
				// mBaiduMap.setMapStatus(u);
				mBaiduMap.animateMapStatus(u);
				setLocationMode(true);
				break;
			case R.id.pull_refresh_list_btn:
				if (NetworkUtil.isNetworkAvailable(getActivity())) {
					if (refresBtn != null) {
						refresh.setVisibility(View.VISIBLE);
						refresBtn.setVisibility(View.GONE);
					}
				} else
					Toast.makeText(getContext(), "Check network setting",
							Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

}
