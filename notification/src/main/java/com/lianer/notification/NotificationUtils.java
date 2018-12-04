package com.lianer.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class NotificationUtils extends ContextWrapper {
    private static final String channelID = "1";
    private static final String channelName = "nest";//我是渠道名字
    private NotificationManager manager;

    public NotificationUtils(Context context) {
        super(context);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), channelID)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//设置默认声音和震动
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setTicker("您收到新的消息")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setNumber(1);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, String txHash, String txType, Class<?> T) {
        return new Notification.Builder(getApplicationContext(), channelID)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//设置默认声音和震动
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setContentIntent(intent(T, txHash, txType))
                .setTicker("您收到新的消息")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setNumber(1);
    }

    public NotificationCompat.Builder getNotification(String title, String content) {
        return new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//设置默认声音和震动
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setTicker("您收到新的消息")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setNumber(1);
    }

    public NotificationCompat.Builder getNotification(String title, String content, String txHash, String txType, Class<?> T) {
        return new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//设置默认声音和震动
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setContentIntent(intent(T, txHash, txType))
                .setTicker("您收到新的消息")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true)
                .setNumber(1);
    }


    private PendingIntent intent(Class<?> T, String txHash, String txType) {
        Intent intent = new Intent(this, T);
        intent.putExtra("txHash", txHash);
        intent.putExtra("txType", txType);
        intent.putExtra("navigateType", 3);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void sendNotification(String title, String content) {
        title = TextUtils.isEmpty(title) ? "nest" : title;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification(title, content).build();
            getManager().notify(1, notification);
        }
    }

    public void sendNotification(String title, String content, String txHash, String type, Class<?> cls) {
        int notifyId = (int) System.currentTimeMillis();
        title = TextUtils.isEmpty(title) ? "nest" : title;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content, txHash, type, cls).build();
            getManager().notify(notifyId, notification);
        } else {
            Notification notification = getNotification(title, content, txHash, type, cls).build();
            getManager().notify(notifyId, notification);
        }
    }


}
