package com.prinsafan.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Adapter.EventAdapter;
import model.Event;

public class EventList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Event> eventList;
    private EventAdapter eventAdapter;
    private Event event= null;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference().child("events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> dataSnapshots= snapshot.getChildren();
                FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
                StorageReference storageReference=firebaseStorage.getReference();

                for (DataSnapshot datas: dataSnapshots){
                    StorageReference pic =storageReference.child("events/"+datas.child("gallery").getValue().toString().trim());
                    long one_mb=1024*1024;
                    pic.getBytes(one_mb).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            event = new Event();
                            event.setDate(datas.child("date").getValue().toString());
                            event.setGuestId(datas.child("guestId").getValue().toString());
                            event.setTitle(datas.child("title").getValue().toString());
                            event.setType(datas.child("type").getValue().toString());
                            event.setDesc(datas.child("desc").getValue().toString());
                            event.setVenue(datas.child("venue").getValue().toString());
                            event.setPhoto(bytes);
                            eventList.add(event);
                            eventAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventList.this,"Something went Wrong! Try again.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Event List");
        setContentView(R.layout.activity_event_list);
        eventList=new ArrayList<>();
        recyclerView=findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter=new EventAdapter(this,eventList);
        recyclerView.setAdapter(eventAdapter);



    }
}