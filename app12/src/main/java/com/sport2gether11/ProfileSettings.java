package com.sport2gether11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.RadialGradient;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.ContentResolver;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ProfileSettings extends AppCompatActivity{

    LocationManager locationManager;
    LocationListener locationListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private NumberPicker numpicker;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;



    //---------------
    private ImageView image_profile;
    private StorageReference storageReference;
    private DatabaseReference mDatabaseRef;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Button uploadPictureButton;
    private StorageTask uploadTask;
    private FirebaseUser fuser;
    private Uri downloadImageUrl;



    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        mAuth = FirebaseAuth.getInstance();


        //-----------------------
        image_profile = findViewById(R.id.userimageView);
        uploadPictureButton = (Button) findViewById(R.id.UploadPictureBtn);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        try {



            //String imageurl = "https://firebasestorage.googleapis.com/v0/b/fitpoint-e4673.appspot.com/o/uploads%2F"+mAuth.getCurrentUser().getDisplayName()+".jpg?alt=media&token=a215d873-a6cf-4796-ac26-3598e11167c0";

            StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getDisplayName() + ".jpg");

            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri downloadUrl)
                {
                    Glide.with(getApplicationContext()).load(downloadUrl).into(image_profile);
                }
            });

            Log.i("url",fileRef.getDownloadUrl().toString());

        }
        catch (Exception e)
        {
            Log.e("Couldn't load image","Couldn't load image");
        }

        fuser = FirebaseAuth.getInstance().getCurrentUser();

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
        numpicker.setDisplayedValues( new String[] { getResources().getString(R.string.MorningandbeforeNoon) ,getResources().getString(R.string.Eveningandnight) , getResources().getString(R.string.AllDay)} );

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");


        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                //uploadFile();
            }
        });
        //-----------------

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
    // --------------------------

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null){

            StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getDisplayName()+
                    "." + getFileExtension(imageUri));

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // This part is for progress bar if we want to add in the future
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 5000);
                            Toast.makeText(ProfileSettings.this,"Upload successful", Toast.LENGTH_SHORT).show();

                            Upload upload = new Upload(mAuth.getCurrentUser().getDisplayName(),fileRef.getDownloadUrl().toString());

                            //Glide.with(getApplicationContext()).load(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()).into(image_profile);
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileSettings.this,"Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

        }else {
           // Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    // --------------------------
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    // --------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && data != null && data.getData() != null){

            imageUri = data.getData();
            image_profile.setImageURI(imageUri);
            Toast.makeText(this,"Image picked!", Toast.LENGTH_SHORT).show();


        }
    }

    public void onClickContinue(View V){

        uploadFile();
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
                              Intent intent = new Intent(ProfileSettings.this, MapAndMenu.class);
                              intent.putExtra("sport1",spinner1.getSelectedItem().toString());
                              intent.putExtra("sport2",spinner2.getSelectedItem().toString());
                              intent.putExtra("sport3",spinner3.getSelectedItem().toString());
                              Log.e("settingsIntent",spinner1.getSelectedItem().toString()+","+spinner2.getSelectedItem().toString()+","+spinner3.getSelectedItem().toString());

                              startActivity(intent);
                              finish();
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
            try {


            // we have permission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
            Toast.makeText(ProfileSettings.this, "Location Updated" , Toast.LENGTH_SHORT).show();

            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
            mDatabase.child(mAuth.getCurrentUser().getUid()).child("position").setValue(locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLatitude()+","+ locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER).getLongitude());
        }
            catch (Exception e)
        {
            Toast.makeText(this, "Couldn't resolve location, get to a place with reception" , Toast.LENGTH_SHORT).show();
        }
        }
    }
}
