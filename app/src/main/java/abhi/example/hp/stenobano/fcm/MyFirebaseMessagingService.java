package abhi.example.hp.stenobano.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import java.util.List;

import abhi.example.hp.stenobano.Dashboard.User_Home;
import abhi.example.hp.stenobano.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    // private Handler handler;
    boolean isCalling = false;
    private Context context;
    private MediaPlayer mp;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("sssss",s);

    }


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        context = getApplicationContext();
        Log.d(TAG, "Bodyasaprovider :>>> " + remoteMessage.getNotification().getTitle().toString());
        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        boolean isAppOpen = false;
        if (isAppIsInBackground(getApplicationContext())) {
            isAppOpen = false;
        } else {
            isAppOpen = true;
        }
        try {
            if (isAppOpen) {

                sendNotification(title,body, new Intent());
            }
            else {
                sendNotification(title,body, new Intent());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String title, String body, Intent intent) {
        Log.i(TAG, "Notification >>>" + title.toString());


        try {
            //   ShortcutBadger.applyCount(getApplicationContext(), count);'

            Bundle bundle = new Bundle();

            mp = MediaPlayer.create(getApplicationContext(), R.raw.notification);
            mp.setLooping(false);
            mp.start();
                intent = new Intent(getApplicationContext(), User_Home.class);
                intent.putExtras(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//                0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.stenobano_logo);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_black_icon_steno);

            notificationBuilder.setColor(getResources().getColor(R.color.transparent_color));
            notificationBuilder.setLargeIcon(bitmap);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_black_icon_steno);
        }


        try {
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(body);

        } catch (Exception e) {
            e.printStackTrace();
        }


        notificationBuilder.setAutoCancel(true);

        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setLights(Color.WHITE, 1000, 5000);

        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        notificationBuilder.setVibrate(pattern);

        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setContentIntent(pendingIntent);


            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
     /*   if (bitmap != null) {
            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            try {
                s.setSummaryText(body);

            notificationBuilder.setStyle(s);
        }*/

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        notificationBuilder.setChannelId(CHANNEL_ID);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(defaultSoundUri, attributes);
            mChannel.setLightColor(Color.WHITE);

            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(11, notificationBuilder.build());
    }




    private boolean isAppIsInBackground(Context context) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }



}

