package com.example.musicbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicbook.ui.model.Messages;
import com.example.musicbook.ui.model.Post;
import com.example.musicbook.ui.model.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private Status status;

    private TextView textContent;
    private Button btnPost;

    String currentime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }
    public void init(){
        textContent=findViewById(R.id.textContent);
        btnPost=findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textContent.getText()==""){
                    Toast.makeText(PostActivity.this, "Vui long dien noi dung", Toast.LENGTH_SHORT).show();
                }else{
                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    currentime = simpleDateFormat.format(calendar.getTime());
                    Status status = new Status(FirebaseAuth.getInstance().getUid(), textContent.getText().toString(),currentime);
                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("Status")
                            .push().setValue(status)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    }});
                    finish();
                }

            }
        });
    };
}
