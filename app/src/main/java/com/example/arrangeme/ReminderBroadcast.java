package com.example.arrangeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyQues")
                .setSmallIcon(R.drawable.templogo)
                .setContentTitle("You haven't filled the questionnaire yet")
                .setContentText("You must fill it to recieve good schedules")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
           notificationManagerCompat.notify(0,builder.build());
    }
}
