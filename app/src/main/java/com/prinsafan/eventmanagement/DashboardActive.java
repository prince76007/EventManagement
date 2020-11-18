package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DashboardActive extends AppCompatActivity {

   private Button createUserButton,createEventButton,createGuest,eventList,guestList,userList;
   private ImageView userPic;
private boolean exit=false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.menus,menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String[] emailSplit=user.getEmail().split("@");
        String userName=emailSplit[0];
        FirebaseDatabase  firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("users").child(userName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("role").getValue().toString().equals("admin")){
                    createUserButton.setVisibility(View.VISIBLE);
                    createEventButton.setVisibility(View.VISIBLE);
                    createGuest.setVisibility(View.VISIBLE);
                    eventList.setVisibility(View.VISIBLE);
                    guestList.setVisibility(View.VISIBLE);
                }else if (snapshot.child("role").getValue().toString().equals("Coordinator")){
                    createEventButton.setVisibility(View.VISIBLE);
                    createGuest.setVisibility(View.VISIBLE);
                    eventList.setVisibility(View.VISIBLE);
                    guestList.setVisibility(View.VISIBLE);
                }else{
                    eventList.setVisibility(View.VISIBLE);
                    guestList.setVisibility(View.VISIBLE);
                }
                String imgLoc=snapshot.child("img").getValue().toString();
                FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
                StorageReference storageReference=firebaseStorage.getReference();
                StorageReference picRef= storageReference.child(imgLoc);
                long one_mb=1024*1024;
                picRef.getBytes(one_mb).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        userPic.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_active);
        createUserButton = findViewById(R.id.createUser);
        createEventButton=findViewById(R.id.createEvent);
        createGuest=findViewById(R.id.createGuest);
        userPic=findViewById(R.id.userPic);
        eventList=findViewById(R.id.eventList);
        guestList=findViewById(R.id.guestList);


        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateUser.class);
                startActivity(intent);
            }
        });
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateEvent.class);
                startActivity(intent);
            }
        });

        createGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),CreateGuest.class);
                startActivity(intent);
            }
        });
        eventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),EventList.class);
                startActivity(intent);
            }
        });
        guestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GuestList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
            //super.onBackPressed();
        if (exit){
            finish();
        }else {
            Toast.makeText(DashboardActive.this,"Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit =true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit =false;
                }
            },3*1000);
        }
    }
}