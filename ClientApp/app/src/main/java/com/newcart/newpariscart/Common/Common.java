package com.newcart.newpariscart.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.newcart.newpariscart.Model.Food;
import com.newcart.newpariscart.Model.Request;
import com.newcart.newpariscart.Model.Restaurant;
import com.newcart.newpariscart.Model.User;
import com.newcart.newpariscart.R;

public class Common {

    //public static String myPhone = "";
    public static User currentUser;
    public static String currentToken = "";
    public static String myCategory = "";
    public static String catName = "";
    public static Restaurant restSelected;
    public static String foodName = "";
    public static Food foodSelected;
    public static String orderSelected;
    public static Request currentOrdet;

    public static String primeiroNumCad = "";
    public static String segundoNumCad = "";

    public static String NOTI_TITLE = "title";
    public static String NOTI_CONTENT = "content";
    public static String TOKEN_REF = "Tokens";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static String PHONE_TEXT = "userPhone";

    //AUTO SIGN IN
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static String isCartEmpty = "0";

    public static final String DELETE = "Apagar";

    public static String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Aguardando Confirmação"; // #F00
        }
        if (status.equals("1")){
            return "Serviço Aceito"; // #0D0
        }
        if (status.equals("2")){
            return "Sendo Preparado"; //#00F
        }
        if (status.equals("3")){
            return "À Caminho"; //#00F
        }
        if (status.equals("4")){
            return "Finalizado"; //#00F
        }
        else
            return "Cancelado"; //#000
    }

    public static String createTopicOrder() {
        return new StringBuilder("/topics/new_order").toString();
    }

    public static void showNotification(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null){
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String NOTIFICATION_CHANNEL_ID = "paris_cart";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "ParisCart",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("ParisCart");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.paris_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.paris_logo));
        if (pendingIntent!=null){
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }
}
