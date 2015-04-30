package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.GSApplication;
import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.database.GSProvider;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.model.User;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * login
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-23 下午3:38:10
 * @version V1.0
 * 
 */
public class LoginActivity extends Activity implements OnClickListener, Notify {

	private static final String TAG = "LoginActivity";

	public static Context instance;
	private EditText account;
	private EditText password;
	// private Button clearAddress;
	private Button login;
	private Button register;
	private ProgressDialog dialog;
	MainService mService;
	private boolean mBound;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if has login, finish and change to main activity
		if (GSApplication.hasLogin == true) {
			finish();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		}
		// must call before setContentiew()
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		Intent intent = new Intent(LoginActivity.this, MainService.class);
		startService(intent);
		// check whether service is running
		if (MainService.isRun == true) {
			Log.i(TAG, "MainService run");

		} else {
			Log.i(TAG, "MainService not run");
			// bind service to activity
			this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			Log.i(TAG, "MainService is run?=" + MainService.isRun);
		}
		GSProvider.init(this);
		User user = GSProvider.getInstance().queryMyUser(true);
		if (user != null) {
			GSApplication.user = user;
		}
		initView();
		instance = this;
	}

	private void initView() {
		account = (EditText) findViewById(R.id.activity_login_account_et);
		password = (EditText) findViewById(R.id.activity_login_password_et);
		// clearAddress = (Button)
		// findViewById(R.id.activity_login_clear_aacount_btn);
		login = (Button) findViewById(R.id.activity_login_login_btn);
		register = (Button) findViewById(R.id.activity_login_register_btn);

		// clearAddress.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		if (GSApplication.user != null) {
			account.setText(GSApplication.user.getUsername());
			password.setText(GSApplication.user.getPassword());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (GSApplication.hasLogin == true) {
			finish();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// when start, bing service
		this.bindService(new Intent(this, MainService.class), this.mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			this.unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_login_login_btn:
			if (account.getText().toString() != null
					&& password.getText().toString() != null) {
				// TODO check account format
				GSApplication.user.setUsername(account.getText().toString());
				GSApplication.user.setPassword(password.getText().toString());
				MainService.addNewTask(new Task(Task.LOGIN, null, this));
				MainService.addActivity(this);
				/**
				 * progress bar
				 */
				dialog = new ProgressDialog(LoginActivity.this);
				dialog.setMessage("Loading, wait for seconds...");
				dialog.show();
			} else {
				Toast.makeText(this, "Empty account or password!",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.activity_login_register_btn:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	public void init() {

	}

	@SuppressLint("ShowToast")
	@Override
	public void refresh(Object... params) {
		Messager msg = (Messager) params[0];
		if (msg.getType() == Task.LOGIN) {
			if (msg.getStatus() == GSConstants.STATUS_OK) {
				dialog.dismiss();
				GSApplication.hasLogin = true;
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				dialog.dismiss();
				Toast.makeText(this, (String) msg.getObject(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
		}
		return false;
	}

}
