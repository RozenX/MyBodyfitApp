package com.example.mybodyfit.struct.channel;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mybody.R;
import com.example.mybodyfit.struct.NumToTime;
import com.example.mybodyfit.struct.PersonalPreference;
import com.example.mybodyfit.struct.UserName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MyNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "notification_channel";
    private static final int NOTIFICATION_ID_1 = 1;
    private static final int NOTIFICATION_ID_2 = 2;
    private static final int NOTIFICATION_ID_3 = 3;
    private static PersonalPreference preference;

    @Override
    public void onReceive(Context context, Intent intent) {
        getNotifications(context);
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleNotifications(context);
        } else if (intent.getAction() != null && intent.getAction().startsWith("my_notification_action")) {
            int notificationId = intent.getIntExtra("notification_id", -1);
            if (notificationId != -1) {
                showNotification(context, "Notification " + notificationId);
            }
        }
    }

    public static void scheduleNotifications(Context context) {
        Calendar notificationTime1 = Calendar.getInstance();
        notificationTime1.set(Calendar.HOUR_OF_DAY, NumToTime.getHours(preference.breakfastNotification));
        notificationTime1.set(Calendar.MINUTE, NumToTime.getMinutes(preference.breakfastNotification));

        Calendar notificationTime2 = Calendar.getInstance();
        notificationTime2.set(Calendar.HOUR_OF_DAY, NumToTime.getHours(preference.lunchNotification));
        notificationTime2.set(Calendar.MINUTE, NumToTime.getMinutes(preference.lunchNotification));

        Calendar notificationTime3 = Calendar.getInstance();
        notificationTime3.set(Calendar.HOUR_OF_DAY, 16);
        notificationTime3.set(Calendar.MINUTE, 22);

        scheduleNotification(context, notificationTime1, NOTIFICATION_ID_1, "Notification 1");
        scheduleNotification(context, notificationTime2, NOTIFICATION_ID_2, "Notification 2");
        scheduleNotification(context, notificationTime3, NOTIFICATION_ID_3, "Notification 3");
    }

    private static void scheduleNotification(Context context, Calendar notificationTime, int notificationId, String contentText) {
        Intent intent = new Intent(context, MyNotificationReceiver.class);
        intent.putExtra("notification_id", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerTime = notificationTime.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    private static void showNotification(Context context, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon_without_backgorund)
                .setContentTitle("My Notification")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(1, builder.build());
    }

    public void getNotifications(final Context context) {
        FirebaseDatabase.getInstance().getReference().child("settings")
                .child(UserName.getName(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        preference = snapshot.getValue(PersonalPreference.class);
                        if (preference != null) {
                            scheduleNotifications(context);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}