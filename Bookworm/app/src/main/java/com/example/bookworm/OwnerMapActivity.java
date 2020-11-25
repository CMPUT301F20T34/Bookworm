package com.example.bookworm;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class OwnerMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView textview;
    public double x;
    public double y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        MarkerOptions marker = new MarkerOptions().position(edmonton).title("Book Handover Location");
        Marker mPos = mMap.addMarker(marker);
        marker.draggable(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(edmonton, 15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng latLng) {
                    mPos.setPosition(latLng);
                    x = mPos.getPosition().latitude;
                    y = mPos.getPosition().longitude;
                    String info = String.format(Locale.CANADA,"%f, %f", x,y);
                    textview.setText(info);
                }
            }
        );
    }
}