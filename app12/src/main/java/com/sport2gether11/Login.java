package com.sport2gether11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    // change try 1
    public void onClickLogin(View v) {
        Intent i = new Intent(Login.this, MapAndMenu.class);
        startActivity(i);
    }
}
