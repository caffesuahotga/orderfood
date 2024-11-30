package com.example.orderfood.services;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        // Nhận SupportMapFragment và thông báo khi sẵn sàng sử dụng.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Thiết lập vị trí ban đầu
        LatLng initialPosition = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 10));

        // Giả lập cập nhật tọa độ liên tục
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Giả lập tọa độ mới của hai người
                double lat1 = -34 + Math.random();
                double lng1 = 151 + Math.random();
                double lat2 = -34 - Math.random();
                double lng2 = 151 - Math.random();

                updateLocation(lat1, lng1, lat2, lng2);

                // Tiếp tục cập nhật sau mỗi 5 giây
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    // Cập nhật vị trí của hai người
    public void updateLocation(double lat1, double lng1, double lat2, double lng2) {
        if (mMap != null) {
            mMap.clear();

            LatLng position1 = new LatLng(lat1, lng1);
            LatLng position2 = new LatLng(lat2, lng2);

            mMap.addMarker(new MarkerOptions().position(position1).title("Person 1"));
            mMap.addMarker(new MarkerOptions().position(position2).title("Person 2"));

            // Di chuyển camera để bao gồm cả hai điểm
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds.Builder().include(position1).include(position2).build(), 100));
        }
    }
}
