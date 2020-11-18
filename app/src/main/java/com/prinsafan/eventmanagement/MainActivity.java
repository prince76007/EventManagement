package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;


public class MainActivity extends AppCompatActivity {
    private EditText email, pass;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=auth.getCurrentUser();
        if (firebaseUser!=null){
            showToast("Welcome "+firebaseUser.getEmail());
            gotoDash();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getTitle().toString()+" -LogIn");
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.logInEmail);
        pass=findViewById(R.id.logInPassword);
        Button logInButton=findViewById(R.id.logInButton);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && pass.getText().toString().length() > 5) {
                    logInButton.setClickable(true);
                } else {
                    logInButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && email.getText().toString().length() > 5) {
                    logInButton.setClickable(true);
                } else {
                    logInButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void logIn(View view){
        String emailText=email.getText().toString().trim();
        String passText=pass.getText().toString().trim();
        auth.signInWithEmailAndPassword(emailText,passText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showToast("LogIn Successful");
                    gotoDash();
                }else {
                    showToast(task.getException().getMessage());
                }
            }
        });

    }

    private void gotoDash(){
        Intent intent= new Intent(getApplicationContext(),DashboardActive.class);
        startActivity(intent);
        finish();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

    }
    private void showToast(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}