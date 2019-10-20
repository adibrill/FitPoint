package com.sport2gether11;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WorkoutForm extends AppCompatActivity {


    public String EntriesCountStr = "0";
    public int EntriesCountInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_form);
    }

    public void OnClickWorkoutFormAdded(View view)
    {
        Log.e("gothere!!!","gothere!!!");
        SharedPreferences pref = this.getSharedPreferences("Entries", MODE_WORLD_WRITEABLE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        EntriesCountStr = pref.getString("EntriesCount", EntriesCountStr);
        Log.e("EntriesCountStr-inform",EntriesCountStr);
        EntriesCountInt = Integer.parseInt(EntriesCountStr);
        //
        // todo calculate
        //
        editor.putString("WorkEntry"+Integer.toString(EntriesCountInt), Integer.toString(EntriesCountInt)+","+Integer.toString(EntriesCountInt));
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
            Toast.makeText(this, "No Entries" , Toast.LENGTH_SHORT).show();
        }


    }
}
