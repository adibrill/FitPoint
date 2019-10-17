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
import android.graphics.RadialGradient;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class ProfileSettings extends AppCompatActivity{

    LocationManager locationManager;
    LocationListener locationListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private NumberPicker numpicker;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;

    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        mAuth = FirebaseAuth.getInstance();


        spinner1 = (Spinner) findViewById(R.id.spinnersport1);
        spinner2 = (Spinner) findViewById(R.id.spinnersport2);
        spinner3 = (Spinner) findViewById(R.id.spinnersport3);
        RadioGroup rg = (RadioGroup)findViewById(R.id.RGroup);
        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);
        numpicker = (NumberPicker)findViewById(R.id.numpicker);
        numpicker.setMinValue(1);
        numpicker.setMaxValue(3);
        numpicker.setDisplayedValues( new String[] { "Morning and before Noon", "Evening and night", "All Day" } );

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  if(dataSnapshot.exists()) {
                      for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {

                          User currentUser = dataSnapshot.getValue(User.class);
                          if(currentUser.getUserName().equals(mAuth.getCurrentUser().getDisplayName().toString()))
                          {
                                String sports[] = getResources().getStringArray(R.array.Sports);

                              int spinnerIndex1 = Arrays.asList(sports).indexOf(currentUser.getSport1());
                              int spinnerIndex2 =  Arrays.asList(sports).indexOf(currentUser.getSport2());
                              int spinnerIndex3 =  Arrays.asList(sports).indexOf(currentUser.getSport3());

                              Log.e("spinner",spinnerIndex1 + " " + spinnerIndex2 + " " + spinnerIndex3);

                              numpicker.setValue(currentUser.getTimePeriod());
                              spinner1.setSelection(spinnerIndex1);
                              spinner2.setSelection(spinnerIndex2);
                              spinner3.setSelection(spinnerIndex3);

                              switch(currentUser.getLevel())
                              {
                                  case 1:
                                      rb1.setChecked(true);
                                      break;
                                  case 2:
                                      rb2.setChecked(true);
                                      break;
                                  case 3:
                                      rb3.setChecked(true);
                                      break;
                              }

                              numpicker.setValue(currentUser.getTimePeriod());
                                break;

                          }
                      }
                  }
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


                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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

        mAuth = FirebaseAuth.getInstance();
        spinner1 = (Spinner) findViewById(R.id.spinnersport1);
        spinner2 = (Spinner) findViewById(R.id.spinnersport2);
        spinner3 = (Spinner) findViewById(R.id.spinnersport3);
        RadioGroup rg = (RadioGroup)findViewById(R.id.RGroup);
        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);
        numpicker = (NumberPicker)findViewById(R.id.numpicker);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  if(dataSnapshot.exists()) {
                      for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {

                          User currentUser = dataSnapshot.getValue(User.class);
                          if (currentUser.getUserName().equals(mAuth.getCurrentUser().getDisplayName().toString())) {
                              currentUser.setSport1(spinner1.getSelectedItem().toString());
                              currentUser.setSport2(spinner2.getSelectedItem().toString());
                              currentUser.setSport3(spinner3.getSelectedItem().toString());
                              currentUser.setTimePeriod(numpicker.getValue());

                              if(rb1.isSelected())
                              {
                                  currentUser.setLevel(1);
                              }
                              else
                              {
                                  if(rb2.isSelected())
                                  {
                                      currentUser.setLevel(2);
                                  }
                                  else
                                  {
                                      currentUser.setLevel(3);
                                  }
                              }
                              mDatabase.child(dataSnapshot.getKey()).setValue(currentUser);
                          break;
                          }

                      }
                  }


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


                // write user data to firebase

                Intent intent = new Intent(ProfileSettings.this, MapAndMenu.class);
        //intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);

                //add user location to firebase -
                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.child(mAuth.getCurrentUser().getUid()).child("position").setValue(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLatitude()+","+ locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLongitude());
            }
        }
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

            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
            mDatabase.child(mAuth.getCurrentUser().getUid()).child("position").setValue(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLatitude()+","+ locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLongitude());

        }
    }
}
