package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;


public class StatusResponse {

	@SerializedName("message")
	private String msg;

	@SerializedName("status")
	private String status;

	public String getMsg() {
		return msg;
	}

	public String getStatus() {
		return status;
	}
}