package votive.com.appuaet10.Notification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import votive.com.appuaet10.Activities.SplashActivity;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Constant;
import votive.com.appuaet10.Utilities.StorageUtils;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Bitmap bitmap;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message = remoteMessage.getData().get(Constant.NOTIFICATION_MSG_KEY);
        String imageUri = remoteMessage.getData().get(Constant.NOTIFICATION_IMAGE_URL_KEY);
        String type = remoteMessage.getData().get(Constant.NOTIFICATION_TYPE_KEY);
        String id = remoteMessage.getData().get(Constant.NOTIFICATION_ID_KEY);
        String title = remoteMessage.getData().get(Constant.NOTIFICATION_TITLE_KEY);
        if (type == null) {
            type = Constant.NOTIFICATION_TYPE_FOR_DEFAULT;
        }

        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
            pushNotification.putExtra(Constant.INTENT_NOTIFICATION_MSG, message);
            pushNotification.putExtra(Constant.INTENT_NOTIFICATION_TITLE, title);
            pushNotification.putExtra(Constant.INTENT_NOTIFICATION_ID, id);
            pushNotification.putExtra(Constant.INTENT_NOTIFICATION_TYPE, type);
            pushNotification.putExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL, imageUri);

            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            // update counter value
            int count = StorageUtils.getPrefForCount(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY);
            count++;
            StorageUtils.putPref(getApplicationContext(), Constant.NOTIFICATION_COUNTER_VALUE_KEY, count);

            Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
            resultIntent.putExtra(Constant.INTENT_NOTIFICATION_MSG, message);
            resultIntent.putExtra(Constant.INTENT_NOTIFICATION_TITLE, title);
            resultIntent.putExtra(Constant.INTENT_NOTIFICATION_ID, id);
            resultIntent.putExtra(Constant.INTENT_NOTIFICATION_TYPE, type);
            resultIntent.putExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL, imageUri);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // check for image attachment
            if (TextUtils.isEmpty(imageUri)) {
                sendNotification(message, title, id, type, resultIntent);
            } else {
                bitmap = getBitmapFromUrl(imageUri);
                sendBigNotification(message, bitmap, title, id, type, resultIntent);
            }
        }
    }

    private void sendNotification(String message, String title, String aId, String aType, Intent resultIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendBigNotification(String messageBody, Bitmap image, String title, String aId, String aType, Intent resultIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("????e = " + e.getMessage());
            return null;
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}