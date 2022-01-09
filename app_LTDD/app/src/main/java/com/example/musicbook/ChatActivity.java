package com.example.musicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.musicbook.ui.adapter.MessageAdapter;
import com.example.musicbook.ui.model.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    RecyclerView mMessageRV;
    TextView mName, mState;
    EditText mMessEdt;
    CircleImageView mReceiverAvatarImv;
    ImageView mSendIm, mBackImV;
    String currentime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    MessageAdapter adapter;
    List<Messages> messagesList;

    private String enteredmessage;
    String mReceiverName, mSenderName, mReceiverUID, mSenderUID, senderRoom, receiverRoom;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        mReceiverAvatarImv = findViewById(R.id.message_avatar_civ);
        mBackImV = findViewById(R.id.chat_back);
        mMessageRV = findViewById(R.id.mess_rv);
        mName = findViewById(R.id.mess_name_tv);
        mState = findViewById(R.id.mess_state_tv);
        mSendIm = findViewById(R.id.mess_send_imv);
        mMessEdt = findViewById(R.id.message_edt);

        messagesList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.ItemDecoration itemDecoration=new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, int itemPosition, @NonNull @NotNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.bottom=20;
            }
        };

        mMessageRV.setLayoutManager(linearLayoutManager);
        adapter=new MessageAdapter(messagesList,this);
        mMessageRV.setAdapter(adapter);
        mMessageRV.addItemDecoration(itemDecoration);

        Intent intent = getIntent();
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBackImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //connect to database
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        mReceiverUID = getIntent().getStringExtra("receiverUID");
        mReceiverName = getIntent().getStringExtra("receiverName");
        String uri = intent.getStringExtra("imageuri");

        mSenderUID=firebaseAuth.getUid();
        senderRoom = mSenderUID + mReceiverUID;
        receiverRoom = mReceiverUID + mSenderUID;

        mName.setText(mReceiverName);


        if (uri.isEmpty()) {
            Picasso.get().load(R.drawable.blankavatar).into(mReceiverAvatarImv);
        } else {
            try {
                Picasso.get().load(uri).into(mReceiverAvatarImv);
            } catch (Exception e) {

            }
        }

        DatabaseReference databaseReference=database.getReference().child("Chats").child(senderRoom).child("messages");
      //  adapter=new MessageAdapter(messagesList,this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Messages messages=snapshot1.getValue(Messages.class);
                    messagesList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





        mSendIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage = mMessEdt.getText().toString();
                if (enteredmessage.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Chưa nhập tin nhắn", Toast.LENGTH_SHORT).show();
                } else {
                    Date date = new Date();
                    currentime = simpleDateFormat.format(calendar.getTime());
                    Messages message = new Messages(enteredmessage, firebaseAuth.getUid(), date.getTime(), currentime);
                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("Chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(message)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            database.getReference()
                                    .child("Chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                }
                            });
                        }
                    });
                    mMessEdt.setText(null);


                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}