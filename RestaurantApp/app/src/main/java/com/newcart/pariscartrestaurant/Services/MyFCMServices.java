package com.newcart.pariscartrestaurant.Services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Model.TokenModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFCMServices extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> dataRecv = remoteMessage.getData();
        if (dataRecv!=null){
            Common.showNotification(this,new Random().nextInt()
            ,dataRecv.get(Common.NOTI_TITLE)
            ,dataRecv.get(Common.NOTI_CONTENT)
            ,null);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (Common.currentUser!=null)
            updateToken(this,s);
    }

    private void updateToken(final Context context, String newToken){
        if (Common.currentUser!=null){
            FirebaseDatabase.getInstance()
                    .getReference(Common.TOKEN_REF)
                    .child(Common.currentUser.getRestId())
                    .setValue(new TokenModel(Common.currentUser.getRestId(),newToken))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
