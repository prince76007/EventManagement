package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Queue;

import model.Event;
import model.Guest;

public class CreateEvent extends AppCompatActivity {

    private EditText eventTitle,dateOfEvent,venue,description;
    private Spinner eventType, guest;
    private ImageButton imgButton;
    private boolean gotPic=false;
    private Uri selectedPhoto;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> guestName;
    private ArrayAdapter<String> guestAdp;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==2 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getImg();
        }else
            showToast("Please allow to get photo");
    }

    private void getImg(){
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        guestName= new ArrayList<>();
        guestName.add("Select Guest");
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference();
        DatabaseReference guests= databaseReference.child("guests");
        guests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> datas=snapshot.getChildren();
                for(DataSnapshot dataSnapshot: datas){
                    if (!dataSnapshot.getKey().equals("") && dataSnapshot.getKey()!=null)
                        guestName.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        eventTitle=findViewById(R.id.titleText);
        dateOfEvent=findViewById(R.id.dateText);
        venue=findViewById(R.id.venueText);
        description=findViewById(R.id.descriptionText);
        eventType=findViewById(R.id.spinnerEventType);
        imgButton=findViewById(R.id.eventImg);
        guest=findViewById(R.id.spinnerGuests);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    getImg();
                }else{
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                }
            }
        });
        guestAdp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, guestName);
        guestAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guest.setAdapter(guestAdp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_event);
        setTitle("Create Event");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            try {
                selectedPhoto=data.getData();
                imgButton.setImageURI(selectedPhoto);
                gotPic=true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void createEventSubmit(View view){
        if (validation()){
           FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
           StorageReference storageReference= firebaseStorage.getReference();
           StorageReference photoRef=storageReference.child("events/"+eventTitle.getText().toString().trim()+".jpg");
            photoRef.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DatabaseReference reference=firebaseDatabase.getReference().child("events");
                    reference.child(eventTitle.getText().toString().trim()).setValue(createEventObj());
                    showToast("Event Created");
                    eventTitle.setText("");
                    dateOfEvent.setText("");
                    venue.setText("");
                    description.setText("");
                    guest.setSelection(0,true);
                    eventType.setSelection(0,true);
                    imgButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_login));
                    gotPic=false;
                }
            });

        }
    }

    private boolean validation(){
        if (eventTitle.getText().toString().length()<6){
            eventTitle.setError("At least 6 character required");
            eventTitle.requestFocus();
            return false;
        }else if(eventType.getSelectedItemPosition()==0){
            showToast("Please select EventType");
            eventType.requestFocus();
            return false;
        }else if (dateOfEvent.getText().toString().length()!=10){
            dateOfEvent.setError("Valid date is required");
            dateOfEvent.requestFocus();
            return false;
        }else if (venue.getText().toString().length()<6){
            venue.setError("At least 6 character required");
            venue.requestFocus();
            return false;
        }else if(guest.getSelectedItemPosition()==0){
            showToast("Please select Guest");
            guest.requestFocus();
            return false;
        }else if (description.getText().toString().trim().length()<6){
            description.setError("At least 6 character required");
            description.requestFocus();
            return false;
        }else if (!gotPic){
            showToast("Please choose image");
            return false;
        }
        return true;
    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }


    private Event createEventObj(){
        return new Event(eventTitle.getText().toString().trim(),eventType.getSelectedItem().toString().trim(),dateOfEvent.getText().toString().trim(),venue.getText().toString().trim(),guest.getSelectedItem().toString().trim(),description.getText().toString().trim(),eventTitle.getText().toString().trim()+".jpg");
    }
}