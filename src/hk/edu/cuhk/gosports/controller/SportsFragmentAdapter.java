package hk.edu.cuhk.gosports.controller;

import hk.edu.cuhk.gosports.MainActivity;
import hk.edu.cuhk.gosports.R;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.utils.TaskDetailOperation;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * adapter for sports listview
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-21 下午10:46:36
 * @version V1.0
 * 
 */
public class SportsFragmentAdapter extends BaseAdapter {

	private List<Sport> sports = new ArrayList<Sport>();
	private Context context;
	private ViewHolder holder;

	public SportsFragmentAdapter(Context context) {
		this.context = context;
		refreshData();
		notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public void refreshData() {
		Messager messager = TaskDetailOperation.LoadSportsFromDB(context);
		if (messager.getStatus() == GSConstants.STATUS_OK) {
			sports = (List<Sport>) messager.getObject();
		}
	}

	@Override
	public int getCount() {
		return sports.size();
	}

	@Override
	public Sport getItem(int position) {
		return sports.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_sports_list, null);
			holder = new ViewHolder();
			holder.user_name = (TextView) convertView
					.findViewById(R.id.item_sports_list_username_tv);
			holder.event_title = (TextView) convertView
					.findViewById(R.id.item_sports_list_event_title_tv);
			holder.join_num = (TextView) convertView
					.findViewById(R.id.item_sports_list_join_num_tv);
			holder.start_time = (TextView) convertView
					.findViewById(R.id.item_sports_list_start_time_tv);
			holder.location = (TextView) convertView
					.findViewById(R.id.item_sports_list_location_tv);
			holder.go2map = (ImageButton) convertView
					.findViewById(R.id.item_sports_list_go2map_imbtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Sport sport = getItem(position);
		holder.user_name.setText(sport.getCreateUserID() + "");
		// FIXME
		holder.event_title.setText(sport.getExtraInfo());
		holder.join_num.setText(sport.getCurrentNum() + "");
		holder.start_time.setText(sport.getStartTime());
		holder.location.setText(sport.getLocation());
		holder.go2map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity) context;
				activity.switchMode(GSConstants.MENU_SPORT_MAP, sport.getSportID());
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity) context;
				activity.switchMode(GSConstants.MENU_SPORT_DETAIL, sport.getSportID());
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView user_name;
		TextView join_num;
		TextView event_title;
		TextView start_time;
		TextView location;
		ImageButton go2map;
	}

}
