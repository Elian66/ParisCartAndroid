package com.localplatze.parisrider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.localplatze.parisrider.Common.Common;
import com.localplatze.parisrider.Model.FCMResponse;
import com.localplatze.parisrider.Model.FCMSendData;
import com.localplatze.parisrider.Model.Motorista;
import com.localplatze.parisrider.Model.Request;
import com.localplatze.parisrider.Model.TokenModel;
import com.localplatze.parisrider.Remote.IFCMService;
import com.localplatze.parisrider.Remote.RetrofitFCMClient;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.functions.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    CardView home_bottom,home_novo;
    Button aceitar, recusar;
    ImageView on, off, oc;
    TextView home_nenhum, status, entregas, rnome,rend,rcliente,rpreco;

    Double toLat, toLong;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IFCMService ifcmService;

    String requestId, restaurantId, clientId, locatId;

    LocationCallback locationCallback;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //toLat = -2.62817;
        //toLong = -56.73611;

        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        status = findViewById(R.id.home_status);
        entregas = findViewById(R.id.home_entregas);
        rnome = findViewById(R.id.home_rnome);
        rend = findViewById(R.id.home_rend);
        rcliente = findViewById(R.id.home_rcliente);
        rpreco = findViewById(R.id.home_rpreco);

        aceitar = findViewById(R.id.home_aceitar);
        recusar = findViewById(R.id.home_recusar);
        home_bottom = findViewById(R.id.home_bottom);
        home_nenhum = findViewById(R.id.home_nenhum);
        home_novo = findViewById(R.id.home_novo);
        on = findViewById(R.id.btn_on);
        off = findViewById(R.id.btn_off);
        oc = findViewById(R.id.btn_oc);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Motoristas");
        DatabaseReference request = database.getReference("Requests");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getNumero()).exists()){
                    final Motorista user = dataSnapshot.child(Common.currentUser.getNumero()).getValue(Motorista.class);

                    if (Common.currentUser.getStatus().equals("0")){
                        status.setText("Você está online");
                    }else if (Common.currentUser.getStatus().equals("1")){
                        status.setText("Você está offline");
                    }else {
                        status.setText("Você está ocupado");
                    }
                    entregas.setText(user.getCorridas());

                    if (user.getRequestId().equals("")){
                        home_bottom.setVisibility(View.GONE);
                        home_novo.setVisibility(View.GONE);
                        home_nenhum.setVisibility(View.VISIBLE);
                    }else {
                        home_bottom.setVisibility(View.VISIBLE); //DEVOLVER PRA VISIBLW
                        home_novo.setVisibility(View.VISIBLE);
                        home_nenhum.setVisibility(View.GONE);

                        request.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(Common.currentUser.getRequestId()).exists()){
                                    final Request req = dataSnapshot.child(Common.currentUser.getRequestId()).getValue(Request.class);

                                    requestId = Common.currentUser.getRequestId();
                                    restaurantId = req.getRestaurant();
                                    clientId = req.getPhone();

                                    req.setNomeEntregador(Common.currentUser.getNome());
                                    req.setPlaca(Common.currentUser.getPlaca());

                                    //TODO: req.setLocalizacao(Common...);
                                    req.setStatus("3");

                                    getLocationFromAddress(HomeActivity.this,req.getAddress());

                                    Toast.makeText(HomeActivity.this, req.getAddress(), Toast.LENGTH_SHORT).show();
                                    rnome.setText(req.getName());
                                    rend.setText("Entrega: "+req.getAddress());
                                    rcliente.setText("Contato: "+req.getPhone());
                                    rpreco.setText(req.getTotal());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> passwordUpdate = new HashMap<>();
                passwordUpdate.put("requestId","");
                passwordUpdate.put("status","1");

                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Motoristas");
                user.child(Common.currentUser.getNumero())
                        .updateChildren(passwordUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                FirebaseDatabase.getInstance()
                        .getReference(Common.TOKEN_REF)
                        .child(restaurantId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    //TODO: NOTIFICATIONS
                                    TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                                    Map<String,String> notiData = new HashMap<>();
                                    notiData.put(Common.NOTI_TITLE,"ParisCart - Motorista Recusou");
                                    notiData.put(Common.NOTI_CONTENT,"Pedido: "+Common.convertCodeToStatus("5"));

                                    FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                                    compositeDisposable.add(ifcmService.sendNotification(sendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<FCMResponse>() {
                                                           @Override
                                                           public void accept(FCMResponse fcmResponse) throws Exception {
                                                               if (fcmResponse.getSuccess() == 1){
                                                                   Toast.makeText(HomeActivity.this, "Status Atualizado", Toast.LENGTH_SHORT).show();
                                                               }else{
                                                                   Toast.makeText(HomeActivity.this, "Falha ao Notificar Cliente", Toast.LENGTH_SHORT).show();
                                                               }

                                                           }
                                                       }, new Consumer<Throwable>() {
                                                           @Override
                                                           public void accept(Throwable throwable) throws Exception {
                                                               Toast.makeText(HomeActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                            ));

                                }else {
                                    Toast.makeText(HomeActivity.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        aceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on.setImageDrawable(getResources().getDrawable(R.drawable.radio_cinza_24dp));
                off.setImageDrawable(getResources().getDrawable(R.drawable.radio_cinza_24dp));
                oc.setImageDrawable(getResources().getDrawable(R.drawable.radio_amarelo_24dp));
                home_bottom.setVisibility(View.VISIBLE);
                home_novo.setVisibility(View.GONE);
                home_nenhum.setVisibility(View.VISIBLE);

                notifyRestaurant(requestId, restaurantId);
                notifyClient(requestId, clientId);

                locationCallback = new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        if(locationResult == null) {
                            return;
                        }
                        Location driverLocation = locationResult.getLastLocation();

                        if (true) { // TODO: check if button is toggled
                            final String value_lat = String.valueOf(driverLocation.getLatitude());
                            final String value_lng = String.valueOf(driverLocation.getLongitude());

                            locatId = value_lat+value_lng;
                            request.child(restaurantId).child("localizacao").setValue(value_lat+value_lng);
                        }
                    }
                };

                Map<String, Object> passwordUpdate = new HashMap<>();
                passwordUpdate.put("nomeEntregador",Common.currentUser.getNome());
                passwordUpdate.put("placa",Common.currentUser.getPlaca());
                passwordUpdate.put("status","3");
                passwordUpdate.put("localizacao",locatId);

                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Requests");
                user.child(Common.currentUser.getRequestId())
                        .updateChildren(passwordUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                requestLocationUpdates();

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+Double.toString(toLat)+","+Double.toString(toLong)+"&mode=1"));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager())!= null){
                    startActivity(intent);
                }

                //Intent intent = new Intent(HomeActivity.this,MapaActivity.class);
                //startActivity(intent);
            }
        });
    }

    public void notifyRestaurant(String requestId, String restaurantId){
        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REF)
                .child(restaurantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //TODO: NOTIFICATIONS
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put(Common.NOTI_TITLE,"ParisCart - Motorista Aceitou");
                            notiData.put(Common.NOTI_CONTENT,"Pedido: "+Common.convertCodeToStatus("3"));

                            FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                                   @Override
                                                   public void accept(FCMResponse fcmResponse) throws Exception {
                                                       if (fcmResponse.getSuccess() == 1){
                                                           Toast.makeText(HomeActivity.this, "Status Atualizado", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(HomeActivity.this, "Falha ao Notificar Cliente", Toast.LENGTH_SHORT).show();
                                                       }

                                                   }
                                               }, new Consumer<Throwable>() {
                                                   @Override
                                                   public void accept(Throwable throwable) throws Exception {
                                                       Toast.makeText(HomeActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                    ));

                        }else {
                            Toast.makeText(HomeActivity.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void notifyClient(String requestId, String clientId){
        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REF)
                .child(clientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //TODO: NOTIFICATIONS
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put(Common.NOTI_TITLE,"ParisCart - Pedido Atualizado");
                            notiData.put(Common.NOTI_CONTENT,"Pedido: "+Common.convertCodeToStatus("3"));

                            FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                                   @Override
                                                   public void accept(FCMResponse fcmResponse) throws Exception {
                                                       if (fcmResponse.getSuccess() == 1){
                                                           Toast.makeText(HomeActivity.this, "Status Atualizado", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(HomeActivity.this, "Falha ao Notificar Cliente", Toast.LENGTH_SHORT).show();
                                                       }

                                                   }
                                               }, new Consumer<Throwable>() {
                                                   @Override
                                                   public void accept(Throwable throwable) throws Exception {
                                                       Toast.makeText(HomeActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                    ));

                        }else {
                            Toast.makeText(HomeActivity.this, "Error: Token not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            toLat = location.getLatitude();
            toLong = location.getLongitude();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

        //Specify how often your app should request the device’s location//

        request.setInterval(100000);

        //Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    //Get a reference to the database, so your app can perform read and write operations//

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests")
                            .child(requestId);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Map<String, Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("localizacao",Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude()));

                        ref.updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
            }, null);
        }
    }
}
