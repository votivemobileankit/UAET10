package votive.com.appuaet10.Beans;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class OfferDetail implements Parcelable {

	@SerializedName("image")
	private String image;

	@SerializedName("company_contact")
	private String companyContact;

	@SerializedName("voucher_image")
	private String voucherImage;

	@SerializedName("voucher")
	private String voucher;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("expiry_date")
	private String expiryDate;

	@SerializedName("term_and_condition")
	private String termAndCondition;

	@SerializedName("company_email")
	private String companyEmail;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	@SerializedName("desc")
	private String desc;

	@SerializedName("apply_more")
	private int mApplyMore;

	public String getmUsed() {
		return mUsed;
	}

	public void setmUsed(String mUsed) {
		this.mUsed = mUsed;
	}

	@SerializedName("used")
	private String mUsed;

	public String getMimage_inner() {
		return mimage_inner;
	}

	public void setMimage_inner(String mimage_inner) {
		this.mimage_inner = mimage_inner;
	}

	@SerializedName("image_inner")
	private String mimage_inner;


	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setCompanyContact(String companyContact){
		this.companyContact = companyContact;
	}

	public String getCompanyContact(){
		return companyContact;
	}

	public void setVoucherImage(String voucherImage){
		this.voucherImage = voucherImage;
	}

	public String getVoucherImage(){
		return voucherImage;
	}

	public void setVoucher(String voucher){
		this.voucher = voucher;
	}

	public String getVoucher(){
		return voucher;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setExpiryDate(String expiryDate){
		this.expiryDate = expiryDate;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public void setTermAndCondition(String termAndCondition){
		this.termAndCondition = termAndCondition;
	}

	public String getTermAndCondition(){
		return termAndCondition;
	}

	public void setCompanyEmail(String companyEmail){
		this.companyEmail = companyEmail;
	}

	public String getCompanyEmail(){
		return companyEmail;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}

	public int getApplyMore(){
		return mApplyMore;
	}

	@Override
 	public String toString(){
		return 
			"OfferDetail{" + 
			"image = '" + image + '\'' + 
			",company_contact = '" + companyContact + '\'' + 
			",voucher_image = '" + voucherImage + '\'' + 
			",voucher = '" + voucher + '\'' + 
			",company_name = '" + companyName + '\'' + 
			",expiry_date = '" + expiryDate + '\'' + 
			",term_and_condition = '" + termAndCondition + '\'' + 
			",company_email = '" + companyEmail + '\'' + 
			",title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			",desc = '" + desc + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.image);
		dest.writeString(this.companyContact);
		dest.writeString(this.voucherImage);
		dest.writeString(this.voucher);
		dest.writeString(this.companyName);
		dest.writeString(this.expiryDate);
		dest.writeString(this.termAndCondition);
		dest.writeString(this.companyEmail);
		dest.writeString(this.title);
		dest.writeString(this.message);
		dest.writeInt(this.status);
		dest.writeString(this.desc);
		dest.writeInt(this.mApplyMore);
	}

	public OfferDetail() {
	}

	protected OfferDetail(Parcel in) {
		this.image = in.readString();
		this.companyContact = in.readString();
		this.voucherImage = in.readString();
		this.voucher = in.readString();
		this.companyName = in.readString();
		this.expiryDate = in.readString();
		this.termAndCondition = in.readString();
		this.companyEmail = in.readString();
		this.title = in.readString();
		this.message = in.readString();
		this.status = in.readInt();
		this.desc = in.readString();
		this.mApplyMore = in.readInt();
	}

	public static final Parcelable.Creator<OfferDetail> CREATOR = new Parcelable.Creator<OfferDetail>() {
		public OfferDetail createFromParcel(Parcel source) {
			return new OfferDetail(source);
		}

		public OfferDetail[] newArray(int size) {
			return new OfferDetail[size];
		}
	};
}