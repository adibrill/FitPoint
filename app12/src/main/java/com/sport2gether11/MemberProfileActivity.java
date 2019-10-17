package com.sport2gether11;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.nio.charset.Charset;
import java.util.Random;

public class MemberProfileActivity extends AppCompatActivity {

    private TextView PartnerNameTextView;
    private String partner_username;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CalendarView calendarView;
    private TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        partner_username =getIntent().getStringExtra("PartnerUserName");
        mAuth = FirebaseAuth.getInstance();

        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timepick);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Workout workout1 = new Workout(partner_username, mAuth.getCurrentUser().getDisplayName(), "pending",calendarView.getDate() +"12:00");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Workout registered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));


                mDatabase.child("Workouts").child(generatedString).setValue(workout1);

            }
        });

        PartnerNameTextView = (TextView)findViewById(R.id.PartnerName);
        PartnerNameTextView.setText(partner_username);



    }

}
