package com.example.musicbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicbook.ui.user.UserFragment;
import com.google.firebase.auth.FirebaseAuth;

public class OptionActivity extends AppCompatActivity {
    ImageView mBackImv;
    FirebaseAuth firebaseAuth;
    TextView mLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        init();
    }
    public void init(){
        mBackImv=findViewById(R.id.back_imv);
        mBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mLogout=findViewById(R.id.tvLogout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(OptionActivity.this,LoginActivity.class));
            }
        });
    };
}