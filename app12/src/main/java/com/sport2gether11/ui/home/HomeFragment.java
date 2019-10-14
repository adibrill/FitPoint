package com.sport2gether11.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sport2gether11.R;
import com.sport2gether11.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    SupportMapFragment SMF;
    private HomeViewModel homeViewModel;
    GoogleMap gmap;
    MapView mapview;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private LocationManager locationmanager;
    private LocationListener locationlistener;

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    private boolean mapSupported = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);



        View mview = inflater.inflate(R.layout.fragment_home,container,false);
        //mv.onCreate(savedInstanceState);
        SMF = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.Fittersmap);
        mapview = (MapView)mview.findViewById(R.id.Fittersmap);
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);
        return mview;
    }

    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);

    }

    private void initializeMap(){

        if (gmap == null && mapSupported){

            mapview = (MapView) getActivity().findViewById(R.id.Fittersmap);
            mapview.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);


        // add other users
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User currentuser = dataSnapshot.getValue(User.class);
                showData(currentuser,googleMap);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        locationmanager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng _location = new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(_location));
                MapsInitializer.initialize(getContext());
                //Log.i("Location",location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(32.085437291653534, 34.954061563709956)).zoom(16).bearing(0).build();
       googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        MapsInitializer.initialize(getContext());
    }


    private void showData(User user,GoogleMap _gmap) {


        String latitude = user.getPosition().substring(0,user.getPosition().indexOf(','));
        String longtitude = user.getPosition().substring(user.getPosition().indexOf(',')+1);



        _gmap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude))).title(user.getUserName()).snippet("aaa"));

    }
}

