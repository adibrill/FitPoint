package com.sport2gether11;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport2gether11.ui.notifications.NotificationsFragment;

public class MapAndMenu extends AppCompatActivity{
    MapView mapView;
    GoogleMap googleMap;
    View mView;
    private FirebaseAuth mAuth;
    public FragmentManager fmanager;
    private DatabaseReference mDatabase;
    private String sport1;
    private String sport2;
    private String sport3;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.barmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.LogoutButton:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(MapAndMenu.this, Login.class);
                startActivity(intToMain);
                break;

            case R.id.ProfileUserSettings:
                Intent i = new Intent(MapAndMenu.this, ProfileSettings.class);
                startActivity(i);
                break;
        }

        return true;
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        return mView;
    }


    public void onClickMember(View v) {
        Intent i = new Intent(this,MemberProfileActivity.class);
        startActivity(i);
    }

    public void OnclickApproveWorkout(View v){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Log.e("viewvvvvv",v.toString());

        final TextView timestampview =(TextView)v.getRootView().findViewById(R.id.WorkoutTime);
        final TextView partnerNameview = (TextView)v.getRootView().findViewById(R.id.PartnerName);
        final TextView typeview =(TextView)v.getRootView().findViewById(R.id.workouttypedata);
        final String type = typeview.getText().toString();
        String myname = mAuth.getCurrentUser().getDisplayName();

        final String timestamp = timestampview.getText().toString();
        final String partnername = partnerNameview.getText().toString();



        mDatabase.child("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Workout l = npsnapshot.getValue(Workout.class);


                        Log.e("myname ",myname + "," + l.getReceiver().toString());
                        Log.e("partnername ",partnername + "," + l.getSender().toString());
                        Log.e("timestamp ",timestamp + "," +l.getTimeStamp() );
                        Log.e("type ",type + "," +l.getWorkOutType() );
                        Log.e("pending ",l.getStatus());


                        if((myname.equals(l.getReceiver().toString())&& partnername.equals(l.getSender().toString()))) {
                            if (l.getTimeStamp().equals(timestamp) && l.getStatus().equals(("pending"))&& l.getWorkOutType().equals((type))) {
                                l.setStatus("approved");

                                mDatabase.child("Workouts").child(npsnapshot.getKey()).setValue(l);
                                Log.e("approved","approved");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // mDatabase.child("Workouts").child(generatedString).setValue(workout1);

        Toast.makeText(this, getResources().getString(R.string.Approved), Toast.LENGTH_SHORT).show();
    }

    public void OnclickCancelWorkout(View v)
    {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final TextView timestampview =(TextView)v.getRootView().findViewById(R.id.WorkoutTime);
        final TextView typeview =(TextView)v.getRootView().findViewById(R.id.workouttypedata);
        final TextView partnerNameview = (TextView)v.getRootView().findViewById(R.id.PartnerName);
        String myname = mAuth.getCurrentUser().getDisplayName();

        final String timestamp = timestampview.getText().toString();
        final String partnername = partnerNameview.getText().toString();
        final String type = typeview.getText().toString();



        mDatabase.child("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Workout l = npsnapshot.getValue(Workout.class);


                        Log.e("myname ",myname + "," + l.getReceiver().toString());
                        Log.e("partnername ",partnername + "," + l.getSender().toString());
                        Log.e("timestamp ",timestamp + "," +l.getTimeStamp() );
                        Log.e("type ",type + "," +l.getWorkOutType() );
                        Log.e("pending ",l.getStatus());


                        if((myname.equals(l.getReceiver().toString())&& partnername.equals(l.getSender().toString()))) {
                            if (l.getTimeStamp().equals(timestamp) && l.getStatus().equals(("pending"))&& l.getWorkOutType().equals((type))) {

                                l.setStatus("cancelled");
                                mDatabase.child("Workouts").child(npsnapshot.getKey()).setValue(l);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // mDatabase.child("Workouts").child(generatedString).setValue(workout1);
        Toast.makeText(this, getResources().getString(R.string.Cancelled), Toast.LENGTH_SHORT).show();


    }


}
