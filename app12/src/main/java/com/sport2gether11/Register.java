package com.sport2gether11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.sport2gether11.Login;
import com.sport2gether11.MapAndMenu;
import com.sport2gether11.R;

import java.util.List;

public class Register extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputUsername, inputPhoneNumber;
    private ProgressBar progressBar;

    private String userId;
    private String email;
    private boolean facebooksign = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername = (EditText) findViewById(R.id.username);
        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumberField);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // if user logged in, go to sign-in screen
        if (mAuth.getCurrentUser() != null) {

            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void onRegisterClicked(View view) {
        final String emailInput = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String phoneNumber = inputPhoneNumber.getText().toString().trim();
        final String username = inputUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enterusername), Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enterphone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(emailInput)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enteremail), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enterpassword), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),  getResources().getString(R.string.passtooshort), Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        //create user

        mAuth.createUserWithEmailAndPassword(emailInput, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       // SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);
                        //SharedPreferences.Editor editor = settings.edit();
                       // editor.putString("username",username);

                        //editor.commit();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        //Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){

                            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

                            fUser.updateProfile(profileUpdates);

                            User user = new User(

                                    username,
                                    phoneNumber,
                                    emailInput,
                                    1,
                                    password,
                                    1,
                                    0,
                                    "32.002985,34.943663",
                                    "Yoga",
                                    "Yoga",
                                    "Yoga"

                            );
                            //Toast.makeText(Register.this, "User created " + username, Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, getResources().getString(R.string.AuthenticationSucceeded), Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(Register.this, ProfileSettings.class);
                                        i.putExtra("username",username);
                                        i.putExtra("email",inputEmail.toString());
                                        i.putExtra("phoneNumber",phoneNumber.toString());
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(Register.this,  getResources().getString(R.string.AuthenticationFailed), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else if (!task.isSuccessful()) {
                            Toast.makeText(Register.this,  getResources().getString(R.string.AuthenticationFailed) + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        } else {


                        }
                    }
                });


    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, Login.class));
    }
}

