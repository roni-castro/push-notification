package android.example.com.firebaseapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastManagerHandler, new IntentFilter("BROADCAST"));

        titleTextView = (TextView) findViewById(R.id.title);
        messageTextView = (TextView) findViewById(R.id.message);

        String currentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM registration token", ""+currentToken);

        // Get the content when the user clicks on a notification
        if(getIntent().getExtras() != null){
            for(String key : getIntent().getExtras().keySet()){
                if(key.equalsIgnoreCase("title")){
                    titleTextView.setText(getIntent().getStringExtra(key));
                } else if(key.equalsIgnoreCase("message")){
                    messageTextView.setText(getIntent().getStringExtra(key));
                }
            }
        }

        // [START get_deep_link]
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(getIntent())
            .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }


                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...

                    // [START_EXCLUDE]
                    // Display deep link in the UI
                    if (deepLink != null) {
                        Snackbar.make(findViewById(android.R.id.content),
                            "Found deep link!", Snackbar.LENGTH_LONG).show();
                        messageTextView.setText(deepLink.toString());
                    } else {
                        Log.d("ERROR", "getDynamicLink: no link found");
                    }
                    // [END_EXCLUDE]
                }
            })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("ERROR", "getDynamicLink:onFailure", e);
                }
            });
        // [END get_deep_link]
    }

    private BroadcastReceiver localBroadcastManagerHandler = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            titleTextView.setText(title);
            messageTextView.setText(message);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastManagerHandler);
    }
}
