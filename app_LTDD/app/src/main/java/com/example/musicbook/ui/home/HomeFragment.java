package com.example.musicbook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbook.PostActivity;
import com.example.musicbook.ChatListActivity;
import com.example.musicbook.R;
import com.example.musicbook.SearchActivity;
import com.example.musicbook.databinding.FragmentHomeBinding;
import com.example.musicbook.ui.adapter.PostAdapter;
import com.example.musicbook.ui.model.Messages;
import com.example.musicbook.ui.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment {
    List<Status> statusList;
    ImageView mSearchImV, mChatImV;
    RecyclerView mRecyclerView;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView post;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();


//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        mChatImV = root.findViewById(R.id.chat_imv);
        mSearchImV = root.findViewById(R.id.iv_search);
        mRecyclerView = root.findViewById(R.id.postView);

        statusList = new ArrayList<>();
        PostAdapter postAdapter = new PostAdapter(statusList, root.getContext());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Status");
        //  adapter=new MessageAdapter(messagesList,this);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                statusList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Status status = snapshot1.getValue(Status.class);
                    statusList.add(status);
                }
                Collections.shuffle(statusList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.setAdapter(postAdapter);

        post = root.findViewById(R.id.textPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });
        mSearchImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        mChatImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }
        });
        return root;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    public void onSearchClick(View view){
//        Toast.makeText(getActivity(), "Button Search Click", Toast.LENGTH_SHORT).show();
//    }
}