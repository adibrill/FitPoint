package com.sport2gether11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private boolean facebook = false;
    EditText userName, userPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        btnSignIn = findViewById(R.id.login_button);
        userName = findViewById(R.id.login_userName);
        userPassword = findViewById(R.id.login_password);

        // -----------------------------------------------------------
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    // toast and go to next activity, user is logged in.
                    Intent intent = new Intent(Login.this, MapAndMenu.class);
                    intent.putExtra("username",userName.getText());
                    startActivity(intent);
                }
                else{
                    // user not logged in
                    Toast.makeText(Login.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        };
        // -----------------------------------------------------------

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent intent = new Intent(Login.this, MapAndMenu.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        // Listen to button clicks from login button
/*           btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (name.isEmpty()){
                    userName.setError("Please enter user name");
                    userName.requestFocus();
                }
                else if (password.isEmpty()){
                    userPassword.setError("Please enter password");
                    userPassword.requestFocus();
                }
                else if (name.isEmpty() && password.isEmpty()){
                    Toast.makeText(Login.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if (!(name.isEmpty() && password.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(name, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login error, please login again", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                Intent intent = new Intent(Login.this, MapAndMenu.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);

        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currenctUser = mAuth.getCurrentUser();

        if(currenctUser != null){
            Log.d(TAG, "Currently Signed in: " + currenctUser.getEmail());
            Toast.makeText(Login.this, "Currently Logged in: " + currenctUser.getEmail(), Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this, MapAndMenu.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token){
        Log.d(TAG, "handleFacebookAccessToken: " + token);
        if(mAuth.getCurrentUser() == null)
        {
            facebook = false;
        }

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d(TAG, "Credentials: " + credential);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user1 = mAuth.getCurrentUser();


                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user1.getDisplayName()).build();

                                user1.updateProfile(profileUpdates);

                                User user = new User(

                                        user1.getDisplayName(),
                                        user1.getPhoneNumber(),
                                        user1.getEmail(),
                                        1,
                                        "",
                                        1,
                                        0,
                                        "32.002985,34.943663",
                                        "yoga",
                                        "yoga",
                                        "yoga"

                                );

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(user1.getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()){
                                            //Toast.makeText(Register.this, "Registration Succeded!", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(Login.this, ProfileSettings.class);

                                            startActivity(i);
                                        }
                                        else{
                                            Toast.makeText(Login.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                                Toast.makeText(Login.this, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                });

        // new user
      // if(facebook == false)
       //{
       //    Intent newFacebookIntent = new Intent(Login.this, ProfileSettings.class);
       //    startActivity(newFacebookIntent);
       //    finish();
     //  }


    }

    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void onClickSignUp(View V){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void onClickLogin(View v) {

        String name = userName.getText().toString();
        final String password = userPassword.getText().toString();

        if (name.isEmpty()){
            userName.setError("Please enter user name");
            userName.requestFocus();
            return;
        }
        else if (password.isEmpty()){
            userPassword.setError("Please enter password");
            userPassword.requestFocus();
            return;
        }
        else if (name.isEmpty() && password.isEmpty()){
            Toast.makeText(Login.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(name, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(Login.this, MapAndMenu.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }
}
