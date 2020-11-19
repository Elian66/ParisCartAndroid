package com.newcart.pariscartrestaurant.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.newcart.pariscartrestaurant.HelpActivity;
import com.newcart.pariscartrestaurant.HomeActivity;
import com.newcart.pariscartrestaurant.Model.Adicional;
import com.newcart.pariscartrestaurant.Model.Food;
import com.newcart.pariscartrestaurant.Model.Request;
import com.newcart.pariscartrestaurant.Model.Restaurant;
import com.newcart.pariscartrestaurant.R;

public class Common {

    public static Restaurant currentUser;
    public static String myCategory = "";
    public static String catName = "";
    public static Restaurant restSelected;
    public static String foodName = "";
    public static Food foodSelected;
    public static String addName = "";
    public static Adicional addSelected;
    public static String orderSelected;
    public static Request currentOrdet;
    public static String currentToken = "";
    public static String NOTI_TITLE = "title";
    public static String NOTI_CONTENT = "content";
    public static String TOKEN_REF = "Tokens";
    public static String segundoNumCad = "";
    public static String filtroOrder = "0";

    public static String isCartEmpty = "0";

    public  static final int PICK_IMAGE_REQUEST = 71;

    //AUTO SIGN IN
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

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

    public static void showNotification(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        intent = new Intent(context, HomeActivity.class);

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
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.paris_logo));
        if (pendingIntent!=null){
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }

}
