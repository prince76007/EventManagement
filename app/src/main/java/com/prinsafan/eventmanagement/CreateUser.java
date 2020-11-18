package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Pattern;

import model.User;

public class CreateUser extends AppCompatActivity {

   private ArrayList<String> departments,roles;
   private Spinner spinnerDep,spinnerRole;
   private ArrayAdapter depArrayAdapter,roleArrayAdapter;
   private EditText name, email, mobileNo, password, design;
   private ImageButton userImg;
   private Bitmap imgBitmap;
   private boolean gotPic=false;
   private Uri selectedImage;
   private StorageReference storageReference;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }else {
                showToast("Please allow to get photo");
            }
        }
    }
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create User");
        setContentView(R.layout.activity_create_user);
        spinnerDep = findViewById(R.id.userDepartment);
        spinnerRole=findViewById(R.id.userRole);
        name=findViewById(R.id.userName);
        email=findViewById(R.id.userEmail);
        mobileNo=findViewById(R.id.userMobile);
        password=findViewById(R.id.userPass);
        design=findViewById(R.id.userDesignation);
        userImg=findViewById(R.id.userImage);
        storageReference= FirebaseStorage.getInstance().getReference();
        roles=new ArrayList<>();
        roles.add("Select Role");
        roles.add("admin");
        roles.add("faculty");
        roles.add("Coordinator");
        departments= new ArrayList<>();
        departments.add("Select Department");
        departments.add("IT");
        departments.add("Management");
        depArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,departments);
        roleArrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,roles);
        roleArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRole.setAdapter(roleArrayAdapter);
        depArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerDep.setAdapter(depArrayAdapter);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                   getPhoto();
                }else{
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }

            }
        });
    }

    public void getPhoto(){
        Intent intent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK && data!=null){
            try {
                selectedImage = data.getData();
                imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                gotPic=true;
                userImg.setImageBitmap(imgBitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void createUserSubmit(View view){
        if (validation()){
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        StorageReference photoRef=storageReference.child("users/"+email.getText().toString().trim().split("@")[0]+".jpg");
                        photoRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                                DatabaseReference reference=firebaseDatabase.getReference().child("users");
                                reference.child(email.getText().toString().trim().split("@")[0]).setValue(createUserObj());
                               showToast("User Created");
                                name.setText("");
                                email.setText("");
                                mobileNo.setText("");
                                password.setText("");
                                design.setText("");
                                userImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_login));
                                gotPic=false;
                                FirebaseUser firebaseUser=auth.getCurrentUser();
                                showToast("Current User "+firebaseUser.getEmail());
                            }
                        });
                    }else{
                            showToast("Something went wrong Please try again.");
                    }
                }
            });
        }
    }

    public boolean validation(){
        if(name.getText().toString().trim().length()<6){
            name.setError("At least 6 characters required");
            name.requestFocus();
            return false;
        }else if(TextUtils.isEmpty(email.getText().toString().trim()) ||
                !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("A valid email is required");
            email.requestFocus();
            return false;
        }else if(mobileNo.getText().toString().trim().length()<10 || !Patterns.PHONE.matcher(mobileNo.getText().toString().trim()).matches()){
            mobileNo.setError("A valid Phone No. is required");
            mobileNo.requestFocus();
            return false;
        }else if(password.getText().toString().trim().length()<6){
            password.setError("at least 6 characters required");
            password.requestFocus();
            return false;
        }else if (spinnerDep.getSelectedItemPosition()==0){
            showToast("Please choose Department");
            return false;
        }else if (spinnerRole.getSelectedItemPosition()==0){
            showToast("Please choose Role");
            return false;
        }else if (design.getText().toString().trim().length()<6){
            design.setError("At least 6 characters required");
            design.requestFocus();
            return false;
        }else if(!gotPic){
            showToast("Please choose photo");
            return false;
        }
        return true;
    }

    public User createUserObj(){
        return new User(spinnerDep.getSelectedItem().toString().trim(),design.getText().toString().trim(),name.getText().toString().trim(),"users/"+email.getText().toString().trim().split("@")[0]+".jpg",mobileNo.getText().toString().trim(),spinnerRole.getSelectedItem().toString().trim());
    }
}