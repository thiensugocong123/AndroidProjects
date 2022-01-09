package com.example.musicbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicbook.ui.custom.CustomProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Button mRegisterBtn;
    EditText mEmailEt,mPasswordEt,mRePasswordEt;
    CustomProgressDialog progressDialog;
    TextView mtvHaveAccount;
    ImageView mbacktoLogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //init()
        mEmailEt=findViewById(R.id.edit_account_reg);
        mPasswordEt=findViewById(R.id.edt_pass_regis);
        mRePasswordEt=findViewById(R.id.edt_re_pass_regis);
        mRegisterBtn=findViewById(R.id.button_register);
        mtvHaveAccount=findViewById(R.id.tv_haveaccount);
        mbacktoLogin=findViewById(R.id.img_backtologin);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog=new CustomProgressDialog(this);
        progressDialog.setMessage("Registering User....");
        mbacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        mtvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmailEt.getText().toString().trim();
                String password=mPasswordEt.getText().toString().trim();
                String repassword=mRePasswordEt.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focuss to email edittext
                    mEmailEt.setError("Invaid Email");
                    mEmailEt.setFocusable(true);
                }
                else if(password.length()<6){
                    //set error and focuss to password edittext
                    mPasswordEt.setError("Password length at least 6 characters");
                    mPasswordEt.setFocusable(true);
                }
                else if(!repassword.equals(password)){
                    mRePasswordEt.setError("Mật khẩu không trùng");
                    mRePasswordEt.setFocusable(true);
                }
                else{
                    registerUser(email,password);
                }
            }
        });

        //handle register on click

    }
    public void registerUser(String email,String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start register activity

                            FirebaseUser user = mAuth.getCurrentUser();

                            //Get user email and uid from Auth
                            String email=user.getEmail();
                            String uid=user.getUid();
                            //When user is registed store user info into firebase realtime database too
                            //Using HashMap
                            HashMap<Object,String> hashMap=new HashMap<>();
                            //put info into hash
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("job","");
                            hashMap.put("school","");
                            hashMap.put("location","");
                            hashMap.put("cover","");
                            //firebase database instance
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            //path to store user data
                            DatabaseReference reference=database.getReference("Users");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registered....\n", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                //error,dismiss progress dialog and get and show the error message
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}