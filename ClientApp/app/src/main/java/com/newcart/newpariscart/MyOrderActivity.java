package com.newcart.newpariscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Model.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyOrderActivity extends FragmentActivity implements OnMapReadyCallback {

    ImageView myor_back,myor_ball01,myor_ball02,myor_ball03,myor_stimg;
    LinearLayout myor_lin01,myor_lin02,myor_lay01,myor_lay03,myor_lay04;
    RelativeLayout myor_lay02;
    TextView myor_sttxt,myor_id;
    Button myor_detalhes;
    TextView mins, name, placa;

    double myLat, myLong,rlat,rlong;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    Request currentFood, myFood;

    String order_id_value = "";

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationFromAddress(this,Common.currentUser.getAddress());

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        requests = firebaseDatabase.getReference("Requests").child(Common.orderSelected);

        myor_back = findViewById(R.id.myor_back);
        myor_ball01 = findViewById(R.id.myor_ball01);
        myor_ball02 = findViewById(R.id.myor_ball02);
        myor_ball03 = findViewById(R.id.myor_ball03);
        myor_lin01 = findViewById(R.id.myor_lin01);
        myor_lin02 = findViewById(R.id.myor_lin02);
        myor_stimg = findViewById(R.id.myor_stimg);
        myor_sttxt = findViewById(R.id.myor_sttxt);
        myor_id = findViewById(R.id.myor_id);
        myor_detalhes = findViewById(R.id.myor_detalhes);
        myor_lay01 = findViewById(R.id.myor_lay01);
        myor_lay02 = findViewById(R.id.myor_lay02);
        myor_lay03 = findViewById(R.id.myor_lay03);
        myor_lay04 = findViewById(R.id.myor_lay04);
        mins = findViewById(R.id.rider_mins);
        name = findViewById(R.id.rider_name);
        placa = findViewById(R.id.rider_placa);

        order_id_value = getIntent().getStringExtra("OrderId");

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Request.class);

                //Toast.makeText(MyOrderActivity.this, Double.toString(rlat), Toast.LENGTH_SHORT).show();

                //Toast.makeText(MyOrderActivity.this, Double.toString(rlat), Toast.LENGTH_SHORT).show();

                if (currentFood.getStatus().equals("0")){
                    myor_ball01.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_ball02.setBackgroundResource(R.drawable.ic_radio_button_checked_gray_24dp);
                    myor_ball03.setBackgroundResource(R.drawable.ic_radio_button_checked_gray_24dp);
                    myor_lin01.setBackgroundColor(Color.GRAY);
                    myor_lin02.setBackgroundColor(Color.GRAY);
                    myor_stimg.setBackgroundResource(R.drawable.etapaa);
                    myor_sttxt.setText("Aguardando Confirmação do Pedido");
                    myor_lay01.setVisibility(View.VISIBLE);
                    myor_lay02.setVisibility(View.GONE);
                    myor_lay03.setVisibility(View.GONE);
                    myor_lay04.setVisibility(View.GONE);
                }
                else if (currentFood.getStatus().equals("1")){
                    myor_ball01.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_ball02.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_ball03.setBackgroundResource(R.drawable.ic_radio_button_checked_gray_24dp);
                    myor_lin01.setBackgroundColor(Color.RED);
                    myor_lin02.setBackgroundColor(Color.GRAY);
                    myor_stimg.setBackgroundResource(R.drawable.etapab);
                    myor_sttxt.setText("Pedido Confirmado");
                    myor_lay01.setVisibility(View.VISIBLE);
                    myor_lay01.setVisibility(View.VISIBLE);
                    myor_lay02.setVisibility(View.GONE);
                    myor_lay03.setVisibility(View.GONE);
                    myor_lay04.setVisibility(View.GONE);
                }
                else if (currentFood.getStatus().equals("2")){
                    myor_ball01.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_ball02.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_ball03.setBackgroundResource(R.drawable.ic_radio_button_checked_orange_24dp);
                    myor_lin01.setBackgroundColor(Color.RED);
                    myor_lin02.setBackgroundColor(Color.RED);
                    myor_stimg.setBackgroundResource(R.drawable.etapac);
                    myor_sttxt.setText("Pedido Sendo Preparado");
                    myor_lay01.setVisibility(View.VISIBLE);
                    myor_lay01.setVisibility(View.VISIBLE);
                    myor_lay02.setVisibility(View.GONE);
                    myor_lay03.setVisibility(View.GONE);
                    myor_lay04.setVisibility(View.GONE);
                }
                else if (currentFood.getStatus().equals("3")){
                    myor_lay01.setVisibility(View.GONE);
                    myor_lay02.setVisibility(View.VISIBLE);
                    myor_lay03.setVisibility(View.GONE);
                    myor_lay04.setVisibility(View.GONE);
                }
                else if (currentFood.getStatus().equals("4")){
                    myor_lay01.setVisibility(View.GONE);
                    myor_lay02.setVisibility(View.GONE);
                    myor_lay03.setVisibility(View.VISIBLE);
                    myor_lay04.setVisibility(View.GONE);
                }
                else {
                    myor_lay01.setVisibility(View.GONE);
                    myor_lay02.setVisibility(View.GONE);
                    myor_lay03.setVisibility(View.GONE);
                    myor_lay04.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myor_id.setText("Pedido #"+order_id_value);

        myor_detalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOrderActivity.this,OrderDetailsActivity.class);
                intent.putExtra("OrderId",order_id_value);
                startActivity(intent);
            }
        });

        myor_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            myLat = location.getLatitude();
            myLong = location.getLongitude();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFood = dataSnapshot.getValue(Request.class);

                if (myFood.getStatus().equals("3")){
                    String [] rloc = currentFood.getLocalizacao().split(",");
                    rlat = Double.parseDouble(rloc[0]);
                    rlong = Double.parseDouble(rloc[1]);

                    LatLng latLng = new LatLng(myLat, myLong);
                    LatLng ridell = new LatLng(rlat, rlong);

                    Location l1 = new Location("");
                    l1.setLatitude(myLat);
                    l1.setLongitude(myLong);

                    Location l2 = new Location("");
                    l2.setLatitude(rlat);
                    l2.setLatitude(rlong);

                    float distancia = l1.distanceTo(l2);

                    int speed = 40;
                    float time = distancia/speed;

                    mins.setText("Seu pedido chegará em "+ Float.toString(time) +" minutos");
                    name.setText("Entregador: "+Common.currentOrdet.getNomeEntregador());
                    placa.setText("Placa: "+Common.currentOrdet.getPlaca());

                    MarkerOptions markerRider = new MarkerOptions();
                    markerRider.position(ridell);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);

                    markerRider.title("Entregador");
                    markerRider.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    markerRider.getPosition();

                    googleMap.clear();

                    markerOptions.title("Sua localização");
                    markerOptions.getPosition();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    // Zoom in, animating the camera.
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    googleMap.addMarker(markerOptions);
                    googleMap.addMarker(markerRider);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
