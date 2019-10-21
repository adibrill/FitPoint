package com.sport2gether11.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sport2gether11.MapAndMenu;
import com.sport2gether11.MemberProfileActivity;
import com.sport2gether11.ProfileSettings;
import com.sport2gether11.R;
import com.sport2gether11.User;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    SupportMapFragment SMF;
    private HomeViewModel homeViewModel;
    GoogleMap gmap;
    MapView mapview;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private double mylon = 0;
    private double mylat = 0;
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


    public boolean onLongMarkerClick(final Marker marker) {



        return true;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        User myuser = npsnapshot.getValue(User.class);

                        mAuth = FirebaseAuth.getInstance();
                        if(myuser.getUserName().equals(mAuth.getCurrentUser().getDisplayName()))
                        {
                            Log.e("EQUALS",myuser.getUserName() + ","+ mAuth.getCurrentUser().getDisplayName());
                            String s1 = myuser.getSport1();
                            String s2 = myuser.getSport2();
                            String s3 = myuser.getSport3();
                            //Log.e("mysp",dataSnapshot.toString());

                            Log.e("Preferences",s1+","+s2+","+s3);

                            Intent marker_intent =new Intent(getActivity(), MemberProfileActivity.class);
                            marker_intent.putExtra("PartnerUserName",marker.getSnippet());
                            marker_intent.putExtra("sport1",s1);
                            marker_intent.putExtra("sport2",s2);
                            marker_intent.putExtra("sport3",s3);
                            //Toast.makeText( getActivity(), "got here map marker click!" , Toast.LENGTH_SHORT).show();
                            startActivity(marker_intent);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String currentLocation;
        gmap = googleMap;
        googleMap.setOnMarkerClickListener(this);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        mAuth = FirebaseAuth.getInstance();
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
            String currentLocation;
            @Override
            public void onLocationChanged(Location location) {
              /*
               if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // ask for permission
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                }
                else {

                    locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationlistener);
                    currentLocation = locationmanager.getLastKnownLocation(locationmanager.GPS_PROVIDER).getLatitude()+","+ locationmanager.getLastKnownLocation(locationmanager.GPS_PROVIDER).getLongitude();
                    mylat = Double.parseDouble(currentLocation.substring(0,currentLocation.indexOf(',')));
                    mylon = Double.parseDouble(currentLocation.substring(currentLocation.indexOf(',')+1));
                }

                //Toast.makeText( getActivity(), "got here!" , Toast.LENGTH_SHORT).show();

                LatLng _location = new LatLng(mylat,mylon);
                // Log.i("_location",_location.toString());
                CameraPosition _currentposition = CameraPosition.builder().target(_location).zoom(16).bearing(0).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(_currentposition));
                MapsInitializer.initialize(getContext());


                //Log.i("Location",location.toString());
            */

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

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // ask for permission
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else {


            try {
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationlistener);
                currentLocation = locationmanager.getLastKnownLocation(locationmanager.GPS_PROVIDER).getLatitude()+","+ locationmanager.getLastKnownLocation(locationmanager.GPS_PROVIDER).getLongitude();
                mylat = Double.parseDouble(currentLocation.substring(0,currentLocation.indexOf(',')));
                mylon = Double.parseDouble(currentLocation.substring(currentLocation.indexOf(',')+1));

            }
            catch (Exception e)
            {
                Toast.makeText( getActivity(), getActivity().getResources().getString(R.string.noreceprion) , Toast.LENGTH_SHORT).show();
            }

        }


        LatLng _location = new LatLng(mylat,mylon);
       // Log.i("_location",_location.toString());
        CameraPosition _currentposition = CameraPosition.builder().target(_location).zoom(16).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(_currentposition));
        MapsInitializer.initialize(getContext());
    }


    private void showData(User user,GoogleMap _gmap) {


        String latitude = user.getPosition().substring(0,user.getPosition().indexOf(','));
        String longtitude = user.getPosition().substring(user.getPosition().indexOf(',')+1);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");


        try {
            StorageReference fileRef = storageReference.child(user.getUserName() + ".jpg");

            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(uri.toString())
                            .listener(new RequestListener<Bitmap>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, 150, 150, false);
                                    Bitmap finalBitmap;
                                    //////crop image

                                    if(resizedBitmap.getWidth() != 150 || resizedBitmap.getHeight() != 150)
                                        finalBitmap = Bitmap.createScaledBitmap(resizedBitmap, 150, 150, false);
                                    else
                                        finalBitmap = resizedBitmap;
                                    Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                                            finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(output);

                                    final Paint paint = new Paint();
                                    final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

                                    paint.setAntiAlias(true);
                                    paint.setFilterBitmap(true);
                                    paint.setDither(true);
                                    canvas.drawARGB(0, 0, 0, 0);
                                    paint.setColor(Color.parseColor("#BAB399"));
                                    canvas.drawCircle(finalBitmap.getWidth() / 2+0.7f, finalBitmap.getHeight() / 2+0.7f,
                                            finalBitmap.getWidth() / 2+0.1f, paint);
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                                    canvas.drawBitmap(finalBitmap, rect, rect, paint);



                                    ////done cropping image

                                    MarkerOptions mo = new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude)))
                                            .title(user.getUserName())
                                            .snippet(user.getUserName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(output));

                                    _gmap.addMarker(mo).showInfoWindow();

                                    return true;

                                }
                            }).preload();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Drawable d = getResources().getDrawable(R.drawable.th);
                    Bitmap empty_bitmap = ((BitmapDrawable)d).getBitmap();
                    Bitmap finalBitmap;
                    //////crop image

                    if(empty_bitmap.getWidth() != 150 || empty_bitmap.getHeight() != 150)
                        finalBitmap = Bitmap.createScaledBitmap(empty_bitmap, 150, 150, false);
                    else
                        finalBitmap = empty_bitmap;
                    Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                            finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(output);

                    final Paint paint = new Paint();
                    final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

                    paint.setAntiAlias(true);
                    paint.setFilterBitmap(true);
                    paint.setDither(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    paint.setColor(Color.parseColor("#BAB399"));
                    canvas.drawCircle(finalBitmap.getWidth() / 2+0.7f, finalBitmap.getHeight() / 2+0.7f,
                            finalBitmap.getWidth() / 2+0.1f, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(finalBitmap, rect, rect, paint);



                    ////done cropping image

                    MarkerOptions mo = new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude)))
                            .title(user.getUserName())
                            .snippet(user.getUserName())
                            .icon(BitmapDescriptorFactory.fromBitmap(output));

                    _gmap.addMarker(mo).showInfoWindow();
                }
            });
        }
        catch (Exception e)
        {

        }
    }
}

