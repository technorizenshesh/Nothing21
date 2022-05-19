package com.nothing21.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nothing21.HomeAct;
import com.nothing21.R;
import com.nothing21.retrofit.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    JSONObject object;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Notification_Data:" + remoteMessage.getData());


        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            try {
                String title = "", key = "",message="";
                object = new JSONObject(data.get("message"));
               // title = object.getString("status");
                key = object.getString("status");
              //  message = object.getString("message");

                if(key.equals("Accept")){
                    title = "Accept";
                    message = "Order accept successfully";
                    Intent intent1 = new Intent("Order_Status_Action");
                    intent1.putExtra("status",key);
                    sendBroadcast(intent1);

                }

              else   if(key.equals("Cancel")){
                    title = "In Progress";
                    message = "Order cancel successfully";
                    Intent intent1 = new Intent("Order_Status_Action");
                    intent1.putExtra("status",key);
                    sendBroadcast(intent1);

                }

              else   if(key.equals("Shiped")){
                    title = "Shipped";
                    message = "Order shipped successfully";
                    Intent intent1 = new Intent("Order_Status_Action");
                    intent1.putExtra("status",key);
                    sendBroadcast(intent1);

                }

              else   if(key.equals("Delivered")){
                    title = "Delivered";
                    message = "Order Delivered successfully";
                    Intent intent1 = new Intent("Order_Status_Action");
                    intent1.putExtra("status",key);
                    sendBroadcast(intent1);

                }

            /*  else   if(key.equals("chat")){
                    title  = "New Message";
                    message = "You have a new message";
                    Intent intent1 = new Intent("Order_Status_Action");
                    intent1.putExtra("status",key);
                    sendBroadcast(intent1);
                }*/


             /* else {
                        if(key.equals("notification")){
                            title  = object.getString("Title");
                            message = object.getString("Description");
                        }*/

            //  }



            if(key.equals("Accept") || key.equals("Cancel") || key.equals("Shiped") || key.equals("Delivered")  ) {
                if (!SessionManager.readString(getApplicationContext(), Constant.USER_INFO, "").equals("")) {
                    wakeUpScreen();
                    displayCustomNotificationForOrders(key, title, message);
                }
            }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void displayCustomNotificationForOrders(String status, String title, String msg) {
        // SessionManager.writeString(getApplicationContext(),"provider_id",provider_id);
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            Intent intent = null;
            intent = new Intent(this, HomeAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription((String) msg);
                mChannel.enableVibration(true);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),attributes);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_logo) // required
                    .setContentText(msg)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_RINGTONE));

            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {
            Intent intent = null;
            intent = new Intent(this, HomeAct.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(msg));





            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1251, notificationBuilder.build());
        }
    }


    private void wakeUpScreen() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on......", "" + isScreenOn);
        if (isScreenOn == false) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }






}
