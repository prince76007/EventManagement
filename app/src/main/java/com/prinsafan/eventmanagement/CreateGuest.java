package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import model.Guest;

public class CreateGuest extends AppCompatActivity {

    private EditText name, adds, phoneNo, email, company, designation, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Guest");
        setContentView(R.layout.activity_create_guest);
        name=findViewById(R.id.guestName);
        adds=findViewById(R.id.guestAdds);
        phoneNo=findViewById(R.id.guestPhone);
        email=findViewById(R.id.guestEmail);
        company=findViewById(R.id.guestCompany);
        designation=findViewById(R.id.guestDesign);
        description=findViewById(R.id.guestDescription);
    }

    public void createGuestSubmit(){
        if (validation()){
            FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference().child("guests");
            databaseReference.child(name.getText().toString().trim()).setValue(createGuestModel()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CreateGuest.this,"Guest Created Successfully",Toast.LENGTH_LONG).show();
                        name.setText("");
                        adds.setText("");
                        phoneNo.setText("");
                        email.setText("");
                        company.setText("");
                        designation.setText("");
                        description.setText("");
                    }else {
                        Toast.makeText(CreateGuest.this,"Something went Wrong! Try again",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean validation(){
        if (name.getText().toString().trim().length()<6){
            name.setError("At least 6 characters required");
            name.requestFocus();
            return false;
        }else if(adds.getText().toString().trim().length()<6){
            adds.setError("At least 6 characters required");
            adds.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(email.getText().toString().trim()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("Valid Email is required");
            email.requestFocus();
            return false;
        }else if (phoneNo.getText().toString().trim().length()<10 || !Patterns.PHONE.matcher(phoneNo.getText().toString().trim()).matches()){
            phoneNo.setError("Valid Phone no is required");
            phoneNo.requestFocus();
            return false;
        }else if (company.getText().toString().trim().length()<6){
            company.setError("At least 6 characters required");
            company.requestFocus();
            return false;
        }else if (designation.getText().toString().trim().length()<6){
            designation.setError("At least 6 characters required");
            designation.requestFocus();
            return false;
        }else if (description.getText().toString().trim().length()<6){
            description.setError("At least 6 characters required");
            description.requestFocus();
            return false;
        }
        return true;
    }

    private Guest createGuestModel(){
        return new Guest(email.getText().toString().trim(),name.getText().toString().trim(),adds.getText().toString().trim(),phoneNo.getText().toString().trim(),company.getText().toString().trim(),description.getText().toString().trim(),designation.getText().toString().trim());
    }

}