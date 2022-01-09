package com.example.musicbook.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicbook.R;
import com.example.musicbook.ui.model.Status;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    List<Status> statusList;
    LayoutInflater layoutInflater;
    Context context;

    public PostAdapter(List<Status> statusList, Context context) {
        this.statusList = statusList;
        this.context = context;
    }


    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(context).inflate(R.layout.post,viewGroup,false);

        //init recyclerview



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get data
        String userImage=statusList.get(position).getImage();
        //   String userName=statusList.get(position).get;
        String userCaption=statusList.get(position).getCaption();

        //set data
        holder.mNameTv.setText("user");
        holder.mDateTV.setText(statusList.get(position).getTime());
        holder.mCaptionTv.setText(userCaption);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.blankavatar).into(holder.mAvatarIv);
        }
        catch(Exception e){

        }
        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }



    @Override
    public int getItemCount() {
        return statusList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mAvatarIv,mPictureIv;
        TextView mNameTv,mCaptionTv,mDateTV;

        public MyViewHolder(@NonNull View view) {
            super(view);

            //init views
            mAvatarIv=view.findViewById(R.id.post_avatar_imv);
            mNameTv=view.findViewById(R.id.post_name_tv);
            mDateTV=view.findViewById(R.id.post_time_tv);
            mCaptionTv=view.findViewById(R.id.post_caption_tv);

        }
    }
}