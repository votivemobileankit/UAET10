
package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class VoucherUsed {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
