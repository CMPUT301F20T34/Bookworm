package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OwnerMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String EXTRA_LOCATION = "";
    private GoogleMap mMap;
    private TextView location;
    private Button confirm;
    private LatLng handover;
    private String username;
    private String isbn;
    private Request selectedRequest;
    private Context context = this;
    private double x;
    private double y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        location = findViewById(R.id.location_info);
        confirm = findViewById(R.id.location_confirm);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        isbn = intent.getStringExtra("isbn");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Intent intent = new Intent();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng edmonton = new LatLng(53.52, -113.52);
        handover = edmonton;
        MarkerOptions marker = new MarkerOptions().position(edmonton).title("Book Handover Location");
        Marker mPos = mMap.addMarker(marker);
        marker.draggable(true);
        x = mPos.getPosition().latitude;
        y = mPos.getPosition().longitude;
        String info = String.format(Locale.CANADA,"%f, %f", x,y);
        location.setText(info);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(edmonton, 15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng latLng) {
                    handover = latLng;
                    mPos.setPosition(latLng);
                    x = mPos.getPosition().latitude;
                    y = mPos.getPosition().longitude;
                    String newLoc = String.format(Locale.CANADA,"%f, %f", x,y);
                    location.setText(newLoc);
                }
            }
        );
    }

    /**
     * Confirm the handover location
     * @param view          the Confirm button
     */
    public void goBack(View view) {
        finish();
    }
}