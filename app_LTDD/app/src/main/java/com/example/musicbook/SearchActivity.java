package com.example.musicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicbook.databinding.ActivitySearchBinding;
import com.example.musicbook.ui.adapter.ListUserAdapter;
import com.example.musicbook.ui.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ImageView mSearchImv, mBackImv;
    RecyclerView recyclerView;
    List<User> userList;
    EditText mSearchEdt;

    //firebase
    FirebaseUser fUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        init();

    }

    public void init() {
        recyclerView = findViewById(R.id.search_rcv);
        mBackImv = findViewById(R.id.imv_back_search);
        mSearchImv = findViewById(R.id.imv_search_search);
        mSearchEdt=findViewById(R.id.edt_search);
        userList = new ArrayList<>();

        ListUserAdapter listUserAdapter = new ListUserAdapter(userList, this);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setAdapter(listUserAdapter);

        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userList=getUserList(String.valueOf(s));
                Log.d("Editalbe", String.valueOf(s));
                listUserAdapter.notifyDataSetChanged();

            }
        });

        mSearchImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList=getUserList(mSearchEdt.getText().toString());
                listUserAdapter.notifyDataSetChanged();
            }
        });
        mBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<User> getUserList(String name) {

        //get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //get path of database name "Users" containg user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    //get all users except current signed in user
                    if (!user.getUid().equals(fUser.getUid())) {
                        if(user.getName().toLowerCase().trim().contains(name.toLowerCase().trim())){
                            userList.add(user);
                            Log.d("User", user.getName());
                            Log.d("Userlist", String.valueOf(userList.size()));
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return userList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}