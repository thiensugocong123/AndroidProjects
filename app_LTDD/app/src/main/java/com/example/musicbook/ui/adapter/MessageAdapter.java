package com.example.musicbook.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbook.ChatActivity;
import com.example.musicbook.R;
import com.example.musicbook.ui.model.Messages;
import com.example.musicbook.ui.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    List<Messages> messagesList;
    Context context;
    final static int ITEM_SEND = 1;
    final static int ITEM_RECEIVE = 2;

    public MessageAdapter(List<Messages> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        if (holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.tv_Message.setText(messages.getMessage());
            viewHolder.time_Message.setText(messages.getCurrenttime());
        }
        else
        {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.tv_Message.setText(messages.getMessage());
            viewHolder.time_Message.setText(messages.getCurrenttime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSender())) {
            return ITEM_SEND;

        } else return ITEM_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Message;
        TextView time_Message;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            time_Message = itemView.findViewById(R.id.time_mess);
            tv_Message = itemView.findViewById(R.id.sendermessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Message;
        TextView time_Message;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            time_Message = itemView.findViewById(R.id.time_mess_rec);
            tv_Message = itemView.findViewById(R.id.receivemessage);
        }
    }
}
