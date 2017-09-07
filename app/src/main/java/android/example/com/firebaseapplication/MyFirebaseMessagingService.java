package android.example.com.firebaseapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
/**
 * Created by Cesar on 12/08/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    // onMessageReceived callback only when your app is in the foreground. If the app is in the
    // background or closed then a notification message is shown in the notification center
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if(remoteMessage.getData().size() > 0) {
            final String title = remoteMessage.getData().get("title");
            final String message = remoteMessage.getData().get("message");
            final String imgUrl = remoteMessage.getData().get("img_url");
            // Update the Main Activity content
            Intent broadcastIntent = new Intent("BROADCAST");
            broadcastIntent.putExtra("title", title + " Broadcast");
            broadcastIntent.putExtra("message", message+ " Broadcast");
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(broadcastIntent);

            // When the app is not in background
            // Click on the notification opens tab activity
            final Context context = this;
            Handler uiHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    new SendNotification(context, title, message, imgUrl).execute();
                }
            };
            uiHandler.post(myRunnable);
        }
    }
}
