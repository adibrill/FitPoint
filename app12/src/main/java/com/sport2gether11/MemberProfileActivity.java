package com.sport2gether11;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MemberProfileActivity extends AppCompatActivity {

    private TextView PartnerNameTextView;
    private String partner_username;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CalendarView calendarView;
    private TimePicker timePicker;
    public boolean workoutexists;
    private String dateselected = Calendar.getInstance().getTime().toString();
    private String timeselected = "";
    private Spinner worktype;
    List<String> spinnerArray = new ArrayList<String>();
    private String mysport1 = "a";
    private String mysport2 = "b";
    private String mysport3 = "c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        partner_username =getIntent().getStringExtra("PartnerUserName");
        PartnerNameTextView = (TextView)findViewById(R.id.PartnerName);
        PartnerNameTextView.setText(partner_username);
        mAuth = FirebaseAuth.getInstance();
        TextView bestHours = (TextView)findViewById(R.id.BestHoursData);
        TextView level = (TextView)findViewById(R.id.UserLevelData);
        TextView Preferences = (TextView)findViewById(R.id.PreferencesData);
        worktype = (Spinner)findViewById(R.id.workouttypespinner);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");


        //String myssports[]=Preferences.getText().toString().split(",");
        mysport1 = getIntent().getStringExtra("sport1");
        mysport2 = getIntent().getStringExtra("sport2");
        mysport3 = getIntent().getStringExtra("sport3");

        Log.e("sport123",mysport1 +","+mysport2 +","+mysport3);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()) {
                           for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                               User partner = npsnapshot.getValue(User.class);

                               if(partner.getUserName().toString().equals(partner_username))
                               {

                                   String sports[] = getResources().getStringArray(R.array.Sports);
                                    switch(partner.getTimePeriod())
                                    {
                                        case 1:
                                            bestHours.setText(getResources().getString(R.string.MorningandbeforeNoon));
                                            break;
                                        case 2:
                                            bestHours.setText(getResources().getString(R.string.Eveningandnight));
                                            break;
                                        case 3:
                                            bestHours.setText(getResources().getString(R.string.AllDay));
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

                                    if(mysport1 != null && mysport2 != null && mysport3 != null)
                                    {

                                        if(partner.getSport1().equals(mysport1) || partner.getSport1().equals(mysport2) || partner.getSport1().equals(mysport3))
                                        {
                                            if(!spinnerArray.contains(partner.getSport1())) {
                                                spinnerArray.add(partner.getSport1());
                                                }
                                        }

                                        if(partner.getSport2().equals(mysport1) || partner.getSport2().equals(mysport2) || partner.getSport2().equals(mysport3))
                                        {
                                            if(!spinnerArray.contains(partner.getSport2())) {
                                                spinnerArray.add(partner.getSport2());
                                            }

                                        }

                                        if(partner.getSport3().equals(mysport1) || partner.getSport3().equals(mysport2) || partner.getSport3().equals(mysport3))
                                        {
                                            if(!spinnerArray.contains(partner.getSport3())) {
                                                spinnerArray.add(partner.getSport3());
                                            }
                                        }
                                    }
                                    else
                                    {
                                        spinnerArray.add(partner.getSport1());

                                        if(!spinnerArray.contains(partner.getSport2())) {
                                            spinnerArray.add(partner.getSport2());
                                        }
                                        if(!spinnerArray. contains(partner.getSport3())) {
                                            spinnerArray.add(partner.getSport3());
                                        }
                                    }
                               }
                           }
                       }
                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {
                }


           });


         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinneritem , spinnerArray);
             adapter.add(getResources().getString(R.string.Choosehere));
        adapter.setDropDownViewResource(R.layout.spinner_deopdown_item);
         worktype.setAdapter(adapter);
          worktype.setSelection(0);


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

        worktype = (Spinner)findViewById(R.id.workouttypespinner);
        Workout workout1 = new Workout(partner_username, mAuth.getCurrentUser().getDisplayName(), "pending",dateselected+" " + timeselected,"Yoga");

        mDatabase = FirebaseDatabase.getInstance().getReference("Workouts");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean flag = true;


                    if (dataSnapshot.exists()) {
                        for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                            Workout work1 = npsnapshot.getValue(Workout.class);

                            if (work1.getSender().equals(mAuth.getCurrentUser().getDisplayName())&& work1.getReceiver().equals(partner_username) && work1.getStatus().equals("pending")) {
                                flag = false;

                            }
                        }


                        if(flag) {
                            FloatingActionButton fab = findViewById(R.id.fab);
                            fab.setOnClickListener(view -> {

                                Spinner clickspinner = (Spinner) findViewById(R.id.workouttypespinner);
                                String workTypes = clickspinner.getSelectedItem().toString();
                                workout1.setWorkOutType(workTypes);
                                //Log.i("workTypes",workTypes);



                                if (workTypes.equals(getResources().getString(R.string.Choosehere))) {
                                    Snackbar.make(view, getResources().getString(R.string.chooseaworkouttype), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {


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

                                    workout1.setTimeStamp(dateselected + " " + timeselected);

                                    mDatabase.child("Workouts").child(generatedString).setValue(workout1);

                                    Snackbar.make(view, getResources().getString(R.string.Workoutregistered), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.workoutalreadyexistswiththispartner), Toast.LENGTH_SHORT).show();
                           // Snackbar.make(, getResources().getString(R.string.Workoutregistered), Snackbar.LENGTH_LONG)
                            //        .setAction("Action", null).show();

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

}
