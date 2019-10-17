package com.sport2gether11;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

public class MemberProfileActivity extends AppCompatActivity {

    private TextView PartnerNameTextView;
    private String partner_username;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CalendarView calendarView;
    private TimePicker timePicker;
    private String dateselected = Calendar.getInstance().getTime().toString();
    private String timeselected = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        partner_username =getIntent().getStringExtra("PartnerUserName");
        mAuth = FirebaseAuth.getInstance();
        TextView bestHours = (TextView)findViewById(R.id.BestHoursData);
        TextView level = (TextView)findViewById(R.id.UserLevelData);
        TextView Preferences = (TextView)findViewById(R.id.PreferencesData);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()) {
                           for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                               User partner = npsnapshot.getValue(User.class);

                               if(partner.getUserName().toString().equals(partner_username))
                               {
                                    switch(partner.getTimePeriod())
                                    {
                                        case 1:
                                            bestHours.setText("Morning and before Noon");
                                            break;
                                        case 2:
                                            bestHours.setText("Evening and night");
                                            break;
                                        case 3:
                                            bestHours.setText("All day");
                                            break;
                                    }
                                    switch(partner.getLevel())
                                    {
                                        case 1:
                                            level.setText("1-2");
                                            break;
                                        case 2:
                                            level.setText("2-4");
                                            break;
                                        case 3:
                                            level.setText("5+");
                                            break;
                                    }
                                   Preferences.setText(partner.getSport1().toString()+","+partner.getSport2().toString()+","+partner.getSport3().toString());
                               }
                           }
                       }
                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {
                }
           });


        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                dateselected = year+"/"+(month+1)+"/"+day;
            }
        });

        timePicker = findViewById(R.id.timepick);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hours, int minutes) {
                timeselected = Integer.toString(hours) + ":" + Integer.toString(minutes);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Workout workout1 = new Workout(partner_username, mAuth.getCurrentUser().getDisplayName(), "pending",dateselected+" " + timeselected);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Workout registered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 30;
                Random random = new Random();
                StringBuilder buffer = new StringBuilder(targetStringLength);
                for (int i = 0; i < targetStringLength; i++) {
                    int randomLimitedInt = leftLimit + (int)
                            (random.nextFloat() * (rightLimit - leftLimit + 1));
                    buffer.append((char) randomLimitedInt);
                }
                String generatedString = buffer.toString();

                workout1.setTimeStamp(dateselected+" " + timeselected);
                //Log.e("timeselected ",dateselected+" " + timeselected);
               mDatabase.child("Workouts").child(generatedString).setValue(workout1);

            }
        });

        PartnerNameTextView = (TextView)findViewById(R.id.PartnerName);
        PartnerNameTextView.setText(partner_username);

    }

}
