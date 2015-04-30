package hk.edu.cuhk.gosports.view;

/**
 * 
 */

import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.model.TimeBean;
import hk.edu.cuhk.gosports.view.DefaultAlertDialog.DialogCallBack;
import hk.edu.cuhk.gosports.view.wheelview.OnWheelScrollListener;
import hk.edu.cuhk.gosports.view.wheelview.WheelView;
import hk.edu.cuhk.gosports.view.wheelview.adapter.AbstractWheelTextAdapter;
import hk.edu.cuhk.gosports.view.wheelview.adapter.NumericWheelAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * File: TimePickDialog.java </p> Copyright: iflyzunhong Copyright (c) 2012</p>
 * 
 * @author lau
 * @version 1.0
 * @since 2013年10月25日上午9:43:31
 * @modify
 * @description
 */
@SuppressLint("SimpleDateFormat")
public class TimePickDialog extends DefaultDialog implements
		android.view.View.OnClickListener {

	@SuppressWarnings("unused")
	private final String TAG = TimePickDialog.class.getSimpleName();

	private Context context;

	private DialogCallBack callBack;
	private WheelView dayWheelView, hourWheelView, minuWheelView;
	private Button cancelBtn, sureBtn;
	private TimeBean bookDate;
	private View coverView;
	private TextView dayTv, weekdayTv;

	private Calendar newCalendar = null;

	private NumericWheelAdapter hourAdapter, minuAdapter;
	private DayWheelAdapter dayAdapter;

	private NumericWheelAdapter newHourAdapter, newMinuteAdapter;

	private DateFormat weekFormat = new SimpleDateFormat("EEE");
	private DateFormat dayFormat = new SimpleDateFormat("MM月dd日");

	public TimeBean getBookDate() {
		int dayIndex = dayWheelView.getCurrentItem();
		String dayStr = dayAdapter.getItemText(dayIndex).toString();
		// bookDate = dayStr.equals("立即发起") ? new
		// TimeBean(Calendar.getInstance(), 0) :
		// getNewBookDate();
		if (dayStr.equals("立即发起")) {
			TimeBean bean = new TimeBean(Calendar.getInstance(), 0);
			bean.setImm(true);
			return bean;
		} else {
			int day = dayIndex == 0 ? 0 : dayIndex - 1;
			Calendar newCalendar2 = (Calendar) Calendar.getInstance().clone();
			newCalendar2.add(Calendar.DAY_OF_YEAR, day);
			TimeBean bean = new TimeBean(newCalendar2, 0);
			if (dayWheelView.getCurrentItem() != 1) {
				bean.setHour(hourAdapter.getItemText(
						hourWheelView.getCurrentItem()).toString());
				bean.setMinute(minuAdapter.getItemText(
						minuWheelView.getCurrentItem()).toString());
			} else {
				bean.setHour(newHourAdapter.getItemText(
						hourWheelView.getCurrentItem()).toString());
				bean.setMinute(newMinuteAdapter.getItemText(
						minuWheelView.getCurrentItem()).toString());
			}
			bean.setImm(false);
			return bean;
		}
	}

	public TimePickDialog(Context context, TimeBean bookDate) {
		super(context);
		this.context = context;
		// this.bookDate = bookdate==null?"立即会议":bookdate;
		this.bookDate = bookDate;
		setContentView(R.layout.view_time_picker);
		initView();
	}

	public void initView() {
		dayWheelView = (WheelView) findViewById(R.id.wheel_day_confer_time);
		hourWheelView = (WheelView) findViewById(R.id.wheel_hour_confer_time);
		minuWheelView = (WheelView) findViewById(R.id.wheel_minute_confer_time);
		cancelBtn = (Button) findViewById(R.id.confer_timepicker_cancel_btn);
		sureBtn = (Button) findViewById(R.id.confer_timepicker_sure_btn);
		coverView = findViewById(R.id.wheel_cover);

		cancelBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);

		hourAdapter = new NumericWheelAdapter(context, 0, 23, "%02d", 1);
		hourAdapter.setTextSize(15);
		hourAdapter.setTextColor(0xff343535);
		hourWheelView.setViewAdapter(hourAdapter);

		minuAdapter = new NumericWheelAdapter(context, 0, 59, "%02d", 2);
		minuAdapter.setTextSize(15);
		/* minuAdapter.setTextSize((int) DensityUtil.sp2px(context, 10)); */
		minuAdapter.setTextColor(0xff343535);
		minuWheelView.setViewAdapter(minuAdapter);

		dayAdapter = new DayWheelAdapter(context, Calendar.getInstance());
		dayWheelView.setViewAdapter(dayAdapter);
		/*
		 * hourWheelView.setEnabled(!bookDate.equals("立即会议"));
		 * minuWheelView.setEnabled(!bookDate.equals("立即会议"));
		 * coverView.setVisibility
		 * (bookDate.equals("立即会议")?View.VISIBLE:View.GONE);
		 */
		hourWheelView.setEnabled(bookDate != null && !bookDate.isImm());
		minuWheelView.setEnabled(bookDate != null && !bookDate.isImm());
		coverView.setVisibility(bookDate == null
				|| (bookDate != null && bookDate.isImm()) ? View.VISIBLE
				: View.GONE);
		// TODO 暂时没办法做，因为没有方法去获取索引index
		// dayWheelView.setCurrentItem(index);
		/*
		 * if(bookDate!=null) { bookDate.getMonth()+bookDate.getDay() }
		 */

		if (bookDate != null && !bookDate.isImm()) {
			String day = bookDate.getMonth() + bookDate.getDay();
			int index = 0;// 0就是今天
			Calendar c = (Calendar) Calendar.getInstance().clone();
			for (int i = 0; i < 9; i++) {
				c.add(Calendar.DAY_OF_MONTH, i == 0 ? 0 : 1);
				if (dayFormat.format(c.getTime()).equals(day)) {
					index = i;
					break;
				}
			}
			dayWheelView.setCurrentItem(index + 1 == 9 ? 0 : index + 1);
		}

		dayWheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				if (wheel.getCurrentItem() == 1) {
					// TODO 今天
					newHourAdapter = new NumericWheelAdapter(context, Calendar
							.getInstance().get(Calendar.HOUR_OF_DAY), 23,
							"%02d", 1);
					newHourAdapter.setTextSize(15);
					newHourAdapter.setTextColor(0xff343535);
					hourWheelView.setViewAdapter(newHourAdapter);
					newMinuteAdapter = new NumericWheelAdapter(context,
							Calendar.getInstance().get(Calendar.MINUTE), 59,
							"%02d", 2);
					newMinuteAdapter.setTextSize(15);
					newMinuteAdapter.setTextColor(0xff343535);
					minuWheelView.setViewAdapter(newMinuteAdapter);
				} else {
					hourWheelView.setViewAdapter(hourAdapter);
					minuWheelView.setViewAdapter(minuAdapter);
				}
				hourWheelView.setEnabled(wheel.getCurrentItem() != 0);
				minuWheelView.setEnabled(wheel.getCurrentItem() != 0);
				coverView.setVisibility(wheel.getCurrentItem() == 0 ? View.VISIBLE
						: View.GONE);
				if (wheel.getCurrentItem() == 0)
					hourWheelView.setCurrentItem(0);
				if (wheel.getCurrentItem() == 0)
					minuWheelView.setCurrentItem(0);
			}
		});
		hourWheelView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				if (dayWheelView.getCurrentItem() == 1
						&& wheel.getCurrentItem() == 0) {
					newMinuteAdapter = new NumericWheelAdapter(context,
							Calendar.getInstance().get(Calendar.MINUTE), 59,
							"%02d", 2);
					newMinuteAdapter.setTextSize(15);
					newMinuteAdapter.setTextColor(0xff343535);
					minuWheelView.setViewAdapter(newMinuteAdapter);
				} else {
					minuWheelView.setViewAdapter(minuAdapter);
				}
			}
		});

		if (dayWheelView.getCurrentItem() == 1) {
			// TODO 今天
			newHourAdapter = new NumericWheelAdapter(context, Calendar
					.getInstance().get(Calendar.HOUR_OF_DAY), 23, "%02d", 1);
			newHourAdapter.setTextSize(15);
			newHourAdapter.setTextColor(0xff343535);
			hourWheelView.setViewAdapter(newHourAdapter);
			newMinuteAdapter = new NumericWheelAdapter(context, Calendar
					.getInstance().get(Calendar.MINUTE), 59, "%02d", 2);
			newMinuteAdapter.setTextSize(15);
			newMinuteAdapter.setTextColor(0xff343535);
			minuWheelView.setViewAdapter(newMinuteAdapter);
		} else {
			hourWheelView.setViewAdapter(hourAdapter);
			minuWheelView.setViewAdapter(minuAdapter);
		}

		if (dayWheelView.getCurrentItem() == 1
				&& hourWheelView.getCurrentItem() == 0) {
			newMinuteAdapter = new NumericWheelAdapter(context, Calendar
					.getInstance().get(Calendar.MINUTE), 59, "%02d", 2);
			newMinuteAdapter.setTextSize(15);
			newMinuteAdapter.setTextColor(0xff343535);
			minuWheelView.setViewAdapter(newMinuteAdapter);
		} else {
			minuWheelView.setViewAdapter(minuAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confer_timepicker_cancel_btn:
			if (callBack != null)
				callBack.onCancel();
			break;
		case R.id.confer_timepicker_sure_btn:
			if (callBack != null)
				callBack.onSure();
			break;
		default:
			break;
		}
		dismiss();
	}

	public DialogCallBack getCallBack() {
		return callBack;
	}

	public TimePickDialog setCallBack(DialogCallBack callBack) {
		this.callBack = callBack;
		return this;
	}

	private class DayWheelAdapter extends AbstractWheelTextAdapter {

		private final int daysCount = 7;
		private Calendar calendar;

		protected DayWheelAdapter(Context context, Calendar calendar) {
			super(context, R.layout.view_day_picker_item);
			this.calendar = calendar;
		}

		@Override
		public int getItemsCount() {
			return daysCount + 2;
		}

		@Override
		protected CharSequence getItemText(int index) {
			if (index >= 0 && index < getItemsCount()) {
				int day = index == 0 ? 0 : index - 1;
				Calendar newCalendar2 = (Calendar) calendar.clone();
				newCalendar2.add(Calendar.DAY_OF_YEAR, day);
				return index == 0 ? getBoldFont("立即发起")
						: day == 0 ? getBoldFont("今天")
								: getBoldFont((newCalendar2.get(Calendar.YEAR))
										+ "年"
										+ dayFormat.format(newCalendar2
												.getTime()));
			}
			return null;
		}

		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			int day = index == 0 ? 0 : index - 1;
			newCalendar = (Calendar) calendar.clone();
			// newCalendar.roll(Calendar.DAY_OF_YEAR, day+62);
			newCalendar.add(Calendar.DAY_OF_YEAR, day);
			View v = super.getItem(index, convertView, parent);
			dayTv = (TextView) v.findViewById(R.id.day_picker_day_tv);
			weekdayTv = (TextView) v.findViewById(R.id.day_picker_weekday_tv);
			weekdayTv.setVisibility(index == 0 || day == 0 ? View.GONE
					: View.VISIBLE);
			dayTv.setText(index == 0 ? getBoldFont("立即发起")
					: day == 0 ? getBoldFont("今天") : getBoldFont(dayFormat
							.format(newCalendar.getTime())));
			weekdayTv.setText(getBoldFont(weekFormat.format(newCalendar
					.getTime())));
			return v;
		}

		public SpannableStringBuilder getBoldFont(String str) {
			SpannableStringBuilder extraStr = new SpannableStringBuilder(str);
			extraStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
					str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return extraStr;
		}

	}

}
