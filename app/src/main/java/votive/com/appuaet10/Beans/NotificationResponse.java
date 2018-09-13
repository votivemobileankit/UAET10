package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class NotificationResponse {

	@SerializedName("message")
	private String msg;

	@SerializedName("status")
	private String status;

	@SerializedName("noti_history")
	private ArrayList<NotificationBean> mNotificationList;

	@SerializedName("pages")
	private int mPageNo;

	// ==================== getter ===============
	public String getMsg() {
		return msg;
	}

	public String getStatus() {
		return status;
	}

	public ArrayList<NotificationBean> getNotificationList() {
		return mNotificationList;
	}

	public int getPageNo() {
		return mPageNo;
	}
}