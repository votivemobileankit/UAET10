package votive.com.appuaet10.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationBean implements Parcelable {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("id")
    private String notifyId;

    @SerializedName("type")
    private String notifyType;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("added_date")
    private String mAddedDate;


    protected NotificationBean(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        notifyId = in.readString();
        notifyType = in.readString();
        deviceId = in.readString();
        mAddedDate = in.readString();
    }

    public static final Creator<NotificationBean> CREATOR = new Creator<NotificationBean>() {
        @Override
        public NotificationBean createFromParcel(Parcel in) {
            return new NotificationBean(in);
        }

        @Override
        public NotificationBean[] newArray(int size) {
            return new NotificationBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(notifyId);
        dest.writeString(notifyType);
        dest.writeString(deviceId);
        dest.writeString(mAddedDate);
    }

    // ========================= getter =========================

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        if (imageUrl != null) {
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                imageUrl = "http://" + imageUrl;
            }
        }
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public String getDate() {
        String dateStr = null;
        if (mAddedDate != null){
            Date date = null;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            try {
                date = format.parse(mAddedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                DateFormat timeFormat = new SimpleDateFormat("dd MMM yy hh:mm aa", Locale.ENGLISH);
                dateStr = timeFormat.format(date);
            }
        }

        return dateStr;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getNotifyType() {
        return notifyType;
    }
}
