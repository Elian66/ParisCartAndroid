package com.localplatze.parisrider;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaActivity extends FragmentActivity {

    Button cheguei, finalizar, menu;
    LinearLayout mapa_entrega;
    String status = "0";

    double myLat, myLong, toLat, toLong;
    private static final int REQUEST_CODE = 101;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_mapa);

        toLat = -2.62817;
        toLong = -56.73611;

        mapView = findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments


                    }
                });

            }
        });

        /*FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.myLocationButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ContextCompat.checkSelfPermission( MapaActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions( MapaActivity.this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            REQUEST_CODE );
                }

            }
        });*/

        mapa_entrega = findViewById(R.id.mapa_entrega);
        cheguei = findViewById(R.id.mapa_cheguei);
        finalizar = findViewById(R.id.mapa_finalizar);
        menu = findViewById(R.id.mapa_menu);
        mapa_entrega.setVisibility(View.GONE);

        cheguei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("0")) {
                    cheguei.setBackgroundResource(R.drawable.borda_gray);
                    finalizar.setBackgroundResource(R.drawable.borda_aberto);
                    status.equals("1");
                }
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("1")) {
                    cheguei.setBackgroundResource(R.drawable.borda_gray);
                    finalizar.setBackgroundResource(R.drawable.borda_gray);
                    mapa_entrega.setVisibility(View.VISIBLE);
                    status.equals("2");
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
