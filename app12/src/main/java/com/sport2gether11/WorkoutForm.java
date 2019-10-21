package com.sport2gether11;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class WorkoutForm extends AppCompatActivity {

    public String EntriesCountStr = "0";
    public int EntriesCountInt;
    NumberPicker HoursPick;
     NumberPicker MinutesPick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_form);
        HoursPick = findViewById(R.id.pickhours);
        MinutesPick = findViewById(R.id.pickminutes);
        MinutesPick.setMinValue(0);
        MinutesPick.setMaxValue(59);
        HoursPick.setMinValue(0);
        HoursPick.setMaxValue(2);
    }

    public void OnClickWorkoutFormAdded(View view)
    {

        SharedPreferences pref = this.getSharedPreferences("Entries", MODE_WORLD_WRITEABLE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String LastEntriesCountStrarr[];
        int hours = HoursPick.getValue();
        int minutes = MinutesPick.getValue();

        double points =  (((hours*60)/15)*0.2) + ((minutes/15)*0.2);

        EntriesCountStr = pref.getString("EntriesCount", EntriesCountStr);
        String LastEntriesCountStr = "";
        Log.e("EntriesCountStr-inform",EntriesCountStr);
        EntriesCountInt = Integer.parseInt(EntriesCountStr);

        LastEntriesCountStr = pref.getString("WorkEntry"+Integer.toString(EntriesCountInt-1), LastEntriesCountStr);
        LastEntriesCountStrarr = LastEntriesCountStr.split(",");
        //
        // todo calculate
        //
        editor.putString("WorkEntry"+Integer.toString(EntriesCountInt), Integer.toString(EntriesCountInt)+","+Double.toString(points + Double.parseDouble(LastEntriesCountStrarr[1])));
        Log.e("WorkEntry-inform",Integer.toString(EntriesCountInt+1)+","+Integer.toString(EntriesCountInt+1));
        ////

        //

        EntriesCountInt+=1;
        editor.putString("EntriesCount", Integer.toString(EntriesCountInt));
        editor.apply();
        Log.e("EntriesCount-after",Integer.toString(EntriesCountInt));


        finish();

    }

    public void OnClickDeleteLastWorkout(View view) {
        SharedPreferences pref = this.getSharedPreferences("Entries", MODE_WORLD_WRITEABLE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        EntriesCountStr = pref.getString("EntriesCount", EntriesCountStr);
        EntriesCountInt = Integer.parseInt(EntriesCountStr);

        if(EntriesCountInt > 0)
        {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            editor.remove("WorkEntry"+Integer.toString(EntriesCountInt-1));
                            editor.putString("EntriesCount",Integer.toString((EntriesCountInt-1)));
                            editor.apply();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //do nothing
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.NoEntries) , Toast.LENGTH_SHORT).show();
        }


    }
}
