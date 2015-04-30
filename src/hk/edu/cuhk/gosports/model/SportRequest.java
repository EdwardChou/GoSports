package hk.edu.cuhk.gosports.model;

import java.io.Serializable;

/**
 * sport request configuration info
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-23 下午12:43:25
 * @version V1.0
 * 
 */
public class SportRequest implements Serializable {

	private static final long serialVersionUID = 1543534534L;

	private int userId = 0;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private double range = 0.0;
	private long expectTimeStart = 0L;
	private long expectTimeEnd = 0L;
	private int loadSportType = 0;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public long getExpectTimeStart() {
		return expectTimeStart;
	}

	public void setExpectTimeStart(long expectTimeStart) {
		this.expectTimeStart = expectTimeStart;
	}

	public long getExpectTimeEnd() {
		return expectTimeEnd;
	}

	public void setExpectTimeEnd(long expectTimeEnd) {
		this.expectTimeEnd = expectTimeEnd;
	}

	public int getLoadSportType() {
		return loadSportType;
	}

	public void setLoadSportType(int loadSportType) {
		this.loadSportType = loadSportType;
	}

	public String toString() {
		return "userid=" + userId + ",latitude=" + latitude + ",longitude="
				+ longitude + ",range=" + range + ",expectTimeStart="
				+ expectTimeStart + ",expectTimeEnd=" + expectTimeEnd
				+ ",loadSportType=" + loadSportType;
	}
}
