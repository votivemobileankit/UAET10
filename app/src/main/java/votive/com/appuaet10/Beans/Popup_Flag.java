package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;


public class Popup_Flag{

	@SerializedName("flag")
	private String flag;

	@SerializedName("status")
	private int status;

	public void setFlag(String flag){
		this.flag = flag;
	}

	public String getFlag(){
		return flag;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Offer_Voucher{" +
			"flag = '" + flag + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}