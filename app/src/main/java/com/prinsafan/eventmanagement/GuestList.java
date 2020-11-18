package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.GuestAdapter;
import model.Guest;

public class GuestList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Guest> guests;
    private GuestAdapter guestAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        guests= new ArrayList<>();
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("guests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> dataSnapshots=snapshot.getChildren();
                Guest guest;
                for (DataSnapshot dataSnapshot: dataSnapshots){
                    guest =new Guest();
                    guest.setEmail(dataSnapshot.child("email").getValue().toString());
                    guest.setFullName(dataSnapshot.child("fullName").getValue().toString());
                    guests.add(guest);
                }
                guestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GuestList.this,"Something went wrong. Try again",Toast.LENGTH_LONG).show();
            }
        });
        guestAdapter =new GuestAdapter(this,guests);
        recyclerView.setAdapter(guestAdapter);
        guestAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_guest_list);
        setTitle("Guest List");
        recyclerView = findViewById(R.id.guestRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}