//package hk.edu.cuhk.gosports.view;
//
//
//
//import hk.edu.cuhk.gosports.R;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.core.SearchResult;
//import com.baidu.mapapi.search.geocode.GeoCodeResult;
//import com.baidu.mapapi.search.geocode.GeoCoder;
//import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
//import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
//import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
//
//public class InputActivity extends Activity implements
//OnGetGeoCoderResultListener {
//
//	GeoCoder mSearch = null; // geo搜索模块
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_input);
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(this);
//		ArrayList<String> locationList=(ArrayList<String>) getIntent().getStringArrayListExtra("ListString"); 
//		//intent中传入数据转换为经纬度数据
//		LatLng ptCenter = new LatLng((Float.valueOf(locationList.get(0).toString())), (Float.valueOf(locationList.get(1).toString())));
////		 反Geo搜索
//		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//				.location(ptCenter));
//		//geo搜索
////		mSearch.geocode(new GeoCodeOption().city(
////				"香港").address(
////				 "崇基图书馆"));
//		
//	}
//
//	
//	
//	public void onGetGeoCodeResult(GeoCodeResult result) {
//		
//		
//		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(InputActivity .this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//					.show();
//			return;
//		}
//		String strInfo = String.format("纬度：%f 经度：%f",
//				result.getLocation().latitude, result.getLocation().longitude);
//		System.out.println(strInfo);
//		Toast.makeText(InputActivity.this, strInfo, Toast.LENGTH_LONG).show();
//	}
//
//	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(InputActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//					.show();
//			return;
//		}
//		MapStatusUpdateFactory.newLatLng(result
//				.getLocation());
//		System.out.println(result.getAddress());
//		Toast.makeText(InputActivity.this, result.getAddress(),
//				Toast.LENGTH_LONG).show();
//
//	}
//
//	
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
