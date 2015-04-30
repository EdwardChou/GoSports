package hk.edu.cuhk.gosports.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * network operation
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午4:25:18
 * @version V1.0
 * 
 */
public class HttpUtil {

	private static final String TAG = "HttpUtil";

	public static String postRequest(String url, JSONObject parameters,
			Context context) {
		Log.i(TAG, TAG + " start post");
		// Create the POST object and add the parameters
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity;
		try {
			entity = new StringEntity(parameters.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			String text = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			Log.i(TAG, "response=" + text);
			return text;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
