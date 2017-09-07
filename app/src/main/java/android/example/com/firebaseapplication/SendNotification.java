package android.example.com.firebaseapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

/**
 * Created by Cesar on 14/08/2017.
 */

public class SendNotification extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String title, message, imageUrl;

    public SendNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title != null ? title : "";
        this.message = message != null ? message : "" ;
        this.imageUrl = imageUrl  != null ? imageUrl : "";
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if(imageUrl.isEmpty()){
            return null;
        } else {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        Intent intent = new Intent(mContext, TabActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("img_url", imageUrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationBuilder.setLights(Color.RED, 3000, 3000);
        notificationBuilder.setContentIntent(pendingIntent);
        if(result != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result));
        }

        Notification notif = notificationBuilder.build();
        notificationManager.notify(0, notif);
    }
}








//    @Override
//    protected void onPostExecute(Bitmap result) {
//
//        super.onPostExecute(result);
//        try {
//            // When the app is not in background
//            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx);
//            notificationBuilder.setContentTitle("Titulo da notificação");
//            notificationBuilder.setContentText("Texto da notificação");
//            notificationBuilder.setAutoCancel(true);
//            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//            notificationBuilder.setSound(soundUri);
//            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//            notificationBuilder.setLights(Color.RED, 3000, 3000);
//            notificationBuilder.setContentIntent(contentIntent);
//            Notification notification = notificationBuilder.build();
//
//            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result));
//            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notification);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }