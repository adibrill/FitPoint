package com.sport2gether11.ui.home;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import com.sport2gether11.R;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    SupportMapFragment SMF;
    private HomeViewModel homeViewModel;
    GoogleMap gmap;
    MapView mapview;
    //View mview;

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

        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Blahblah").snippet("blah"));

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        MapsInitializer.initialize(getContext());
    }
}
