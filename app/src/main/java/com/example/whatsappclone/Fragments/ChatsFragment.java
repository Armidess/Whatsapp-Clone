package com.example.whatsappclone.Fragments;

import static com.example.whatsappclone.databinding.FragmentChatsBinding.inflate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.Adapters.UserAdapter;
import com.example.whatsappclone.databinding.FragmentChatsBinding;
import com.example.whatsappclone.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {



    public ChatsFragment() {

    }

    FragmentChatsBinding binding;
    ArrayList<User> list=new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentChatsBinding.inflate(inflater, container, false);
        UserAdapter adapter = new UserAdapter(list,getContext());
        database = FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(auth.getCurrentUser().getUid()!= dataSnapshot.getKey()) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setUserID(dataSnapshot.getKey());
                        list.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        return binding.getRoot();

    }
}