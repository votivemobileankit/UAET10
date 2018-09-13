package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;


public class OfferVoucher {

	@SerializedName("offer_detail")
	private OfferDetail offerDetail;

	public void setOfferDetail(OfferDetail offerDetail){
		this.offerDetail = offerDetail;
	}

	public OfferDetail getOfferDetail(){
		return offerDetail;
	}

	@Override
 	public String toString(){
		return 
			"OfferVoucher{" +
			"offer_detail = '" + offerDetail + '\'' + 
			"}";
		}
}