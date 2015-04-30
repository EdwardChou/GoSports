package hk.edu.cuhk.gosports.view;

import hk.edu.cuhk.gosports.GSApplication;
import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.model.User;
import hk.edu.cuhk.gosports.service.MainService;
import hk.edu.cuhk.gosports.service.MainService.LocalBinder;
import hk.edu.cuhk.gosports.utils.EmailFormatUtil;
import hk.edu.cuhk.gosports.utils.GSConstants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * register activity
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-29 PM3:45:06
 * @version V1.0
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener,
		Notify {

	// private static final String TAG = "RegisterActivity";

	private EditText account, password, confirm, age, email, description;
	private RadioButton sex;
	MainService mService;
	private ProgressDialog dialog;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		// check whether service is running
		if (MainService.isRun == true) {
			// Log.i(TAG, "MainService run");

		} else {
			// Log.i(TAG, "MainService not run");
			// bind service to activity
			Intent intent = new Intent(this, MainService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}

		account = (EditText) findViewById(R.id.activity_register_username_et);
		password = (EditText) findViewById(R.id.activity_register_password_et);
		confirm = (EditText) findViewById(R.id.activity_register_confirm_et);
		age = (EditText) findViewById(R.id.activity_register_age_et);
		email = (EditText) findViewById(R.id.activity_register_email_et);
		description = (EditText) findViewById(R.id.activity_register_description_et);
		sex = (RadioButton) findViewById(R.id.activity_register_male_rb);

		ImageButton exit = (ImageButton) findViewById(R.id.activity_register_exit_ibtn);
		Button register = (Button) findViewById(R.id.activity_register_register_btn);
		exit.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		// when start, bing service
		bindService(new Intent(this, MainService.class), this.mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_register_register_btn:
			if (checkInfo() && mBound) {
				/**
				 * progress bar
				 */
				dialog = new ProgressDialog(this);
				dialog.setMessage("Registering, wait for seconds...");
				dialog.show();

				MainService.addNewTask(new Task(Task.REGISTER, null, this),
						(Notify) this);
			}
			break;
		case R.id.activity_register_exit_ibtn:
			finish();
			break;
		default:
			break;
		}
	}

	private boolean checkInfo() {
		if ("".equals(account.getText().toString())) {
			Toast.makeText(this, "Empty account", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(password.getText().toString())) {
			Toast.makeText(this, "Empty password", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(confirm.getText().toString())) {
			Toast.makeText(this, "Empty confirm password", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!password.getText().toString()
				.equals(confirm.getText().toString())) {
			Toast.makeText(this, "Passwords conflict", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if ("".equals(age.getText().toString())) {
			Toast.makeText(this, "Empty age", Toast.LENGTH_SHORT).show();
			return false;
		} else if (Integer.parseInt(age.getText().toString()) > 100
				|| Integer.parseInt(age.getText().toString()) < 0) {
			Toast.makeText(this, "Error age", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(email.getText().toString())) {
			Toast.makeText(this, "Empty email", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!EmailFormatUtil.emailFormat(email.getText().toString())) {
			Toast.makeText(this, "Error email format", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		GSApplication.user.setUsername(account.getText().toString());
		GSApplication.user.setPassword(password.getText().toString());
		GSApplication.user.setAge(Integer.parseInt(age.getText().toString()));
		GSApplication.user.setSex(sex.isChecked() ? 1 : 0);
		GSApplication.user.setMailbox(email.getText().toString());
		GSApplication.user.setCredit(5);
		if (!"".equals(description.getText().toString())) {
			GSApplication.user.setDescription(description.getText().toString());
		}
		return true;
	}

	@Override
	public void init() {
	}

	@Override
	public void refresh(Object... params) {
		Messager msg = (Messager) params[0];
		if (msg.getType() == Task.REGISTER) {
			if (msg.getStatus() == GSConstants.STATUS_OK) {
				dialog.dismiss();
				GSApplication.hasLogin = true;
				finish();
			} else {
				dialog.dismiss();
				Toast.makeText(this, (String) msg.getObject(),
						Toast.LENGTH_SHORT).show();
				// if not ok, clear the user info
				GSApplication.user = new User();
			}
		}
	}

}
