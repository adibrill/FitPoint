package com.sport2gether11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileSettings extends AppCompatActivity{

    LocationManager locationManager;
    LocationListener locationListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private NumberPicker numpicker;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;

  //  private String username = getIntent().getStringExtra("username");
  //  private String email = getIntent().getStringExtra("email");
   // private String phonenumber = getIntent().getStringExtra("phoneNumber");
    private String userid;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);

                //add user location to firebase -
                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("position"). setValue(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLatitude()+","+ locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLongitude());

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        mAuth = FirebaseAuth.getInstance();

        spinner1 = (Spinner) findViewById(R.id.spinnersport1);
        spinner2 = (Spinner) findViewById(R.id.spinnersport2);
        spinner3 = (Spinner) findViewById(R.id.spinnersport3);

        numpicker = (NumberPicker)findViewById(R.id.numpicker);
        numpicker.setMinValue(1);
        numpicker.setMaxValue(3);
        numpicker.setDisplayedValues( new String[] { "Morning and before Noon", "Evening and night", "All Day" } );
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        //todo - bring all data of user from firbase
        mDatabase.child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User currentUser = dataSnapshot.getValue(User.class);
                SharedPreferences sports = getSharedPreferences("Sports", Context.MODE_PRIVATE);
                int spinnerIndex1 = sports.getInt(currentUser.getSport1(), 0);
                int spinnerIndex2 = sports.getInt(currentUser.getSport2(), 0);
                int spinnerIndex3 = sports.getInt(currentUser.getSport3(), 0);
                numpicker.setValue(currentUser.getTimePeriod());
                spinner1.setSelection(spinnerIndex1);
                spinner2.setSelection(spinnerIndex2);
                spinner3.setSelection(spinnerIndex3);

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

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


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

        // if davice is running sdk < 23
        if(Build.VERSION.SDK_INT < 23)
        {
          //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
        }
        else
        {

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // ask for permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            // we have permission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);

        }
    }

    public void onClickContinue(View V){

       //todo - add writing to firebase
        // write user data to firebase

        Intent intent = new Intent(ProfileSettings.this, MapAndMenu.class);
        startActivity(intent);
        finish();
    }

    public void onClickUpdateLocation(View V){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // ask for permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {


            // we have permission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
            Toast.makeText(ProfileSettings.this, "Location Updated" , Toast.LENGTH_SHORT).show();
            //Toast.makeText(ProfileSettings.this, locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).toString() , Toast.LENGTH_SHORT).show();

            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
            mDatabase.child(mAuth.getCurrentUser().getUid()).child("position").setValue(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLatitude()+","+ locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLongitude());
            //String key = mDatabase.child(mAuth.getCurrentUser().getUid()).child("position").push().getKey();
           // Log.i("key",key.toString());
            //Toast.makeText(ProfileSettings.this, key.toString() , Toast.LENGTH_SHORT).show();

        }
    }
}
