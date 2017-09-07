package android.example.com.firebaseapplication;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Cesar on 12/08/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String currentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh:", currentToken);
    }
}
