package com.example.musicbook.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicbook.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(@NonNull Context context) {
        super(context);
        WindowManager.LayoutParams params=getWindow().getAttributes();

        params.gravity= Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view= LayoutInflater.from(context).inflate(R.layout.custom_dialog,null);
        setContentView(view);
    }
    public void setMessage(String message){
        TextView mMessageTV;
        mMessageTV=findViewById(R.id.progess_content);
        mMessageTV.setText(message);
    }
}
