package hk.edu.cuhk.gosports.model;

import java.io.Serializable;

/**
 * JavaBean for sport
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-21 PM10:33:19
 * @version V1.0
 * 
 */
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;

	private int sportServerID = 0;
	private int sportID = 0;
	private int createUserID = 0;
	private String eventTitle = "";
	private String location = "";
	private double latitude = 0.0;
	private double longitude = 0.0;
	private int expectNum = 0;
	private int currentNum = 0;
	private int sportType = 0;
	private String startTime = "";
	private String createTime = "";
	private String extraInfo = "";
	private boolean isCreator = false;
	private boolean isJoined = false;

	public int getSportServerID() {
		return sportServerID;
	}

	public void setSportServerID(int sportServerID) {
		this.sportServerID = sportServerID;
	}

	public int getSportID() {
		return sportID;
	}

	public void setSportID(int sportID) {
		this.sportID = sportID;
	}

	public int getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(int createUserID) {
		this.createUserID = createUserID;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public int getExpectNum() {
		return expectNum;
	}

	public void setExpectNum(int expectNum) {
		this.expectNum = expectNum;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public int getSportType() {
		return sportType;
	}

	public void setSportType(int sportType) {
		this.sportType = sportType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public boolean isCreator() {
		return isCreator;
	}

	public void setCreator(boolean isCreator) {
		this.isCreator = isCreator;
	}

	public boolean isJoined() {
		return isJoined;
	}

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

}
