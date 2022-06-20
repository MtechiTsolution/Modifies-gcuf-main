package com.providentitgroup.attendergcuf.Notifications;

import static com.providentitgroup.attendergcuf.LoginActivity.CNIC;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.providentitgroup.attendergcuf.ChatsAcitivity;
import com.providentitgroup.attendergcuf.MainActivity;
import com.providentitgroup.attendergcuf.Utility.DataLocal;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public boolean isRestricted() {
        return super.isRestricted();
    }

    @Override
    public void onCreate() {
       // updateToken("test");
        Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();

        super.onCreate();
    }


    @Override
    public void onNewToken(@NonNull String s) {
        //if(FirebaseAuth.getInstance().getCurrentUser()!=null){
        Toast.makeText(this, "new token", Toast.LENGTH_SHORT).show();
          //  updateToken(s);
       // }

        super.onNewToken(s);
    }
    private void updateToken(String refreshToken) {
        Log.d("noti","update token");
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens"+refreshToken);
        Token token = new Token(refreshToken);


        reference.child(DataLocal.getString(this,CNIC)).setValue(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText(this, "rec", Toast.LENGTH_SHORT).show();
       // updateToken("check");

//        Log.d("noti","message recived");
//
//        String sented = remoteMessage.getData().get("sented");
//        String user = remoteMessage.getData().get("user");
//
//        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
//        String currentUser = preferences.getString("currentuser", "none");
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (firebaseUser != null && sented.equals(firebaseUser.getUid())){
//            if (!currentUser.equals(user)) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    sendOreoNotification(remoteMessage);
//                } else {
//                    sendNotification(remoteMessage);
//                }
//            }
//        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatsAcitivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        OreoNotification oreoNotification = new OreoNotification(this);
//        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
//                defaultSound, icon);
//
//        int i = 0;
//        if (j > 0){
//            i = j;
//        }
//
//        oreoNotification.getManager().notify(i, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatsAcitivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0){
            i = j;
        }

        noti.notify(i, builder.build());
    }

    @Override
    public void onDestroy() {
        updateToken("destroy");
        Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
