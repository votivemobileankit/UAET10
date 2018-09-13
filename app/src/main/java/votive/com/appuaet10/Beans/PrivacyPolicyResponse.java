package votive.com.appuaet10.Beans;


import com.google.gson.annotations.SerializedName;

import votive.com.appuaet10.Utilities.Constant;

public class PrivacyPolicyResponse {

    @SerializedName(Constant.JSON_RESPONSE_STATUS_KEY)
    private String mStatus;

    @SerializedName(Constant.JSON_RESPONSE_MSG_KEY)
    private String mMsg;

    @SerializedName(Constant.JSON_PRIVACY_POLICY_KEY)
    private String mPrivacyPolicyText;

    @SerializedName(Constant.JSON_RESPONSE_ERROR_CODE)
    private String mErrorCode;

    //====================getter=================

    public String getmStatus() {
        return mStatus;
    }

    public String getmMsg() {
        return mMsg;
    }

    public String getmErrorCode() {
        return mErrorCode;
    }

    public String getmPrivacyPolicyText() {
        return mPrivacyPolicyText;
    }
}
