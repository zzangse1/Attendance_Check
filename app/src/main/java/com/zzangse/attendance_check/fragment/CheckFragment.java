package com.zzangse.attendance_check.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zzangse.attendance_check.UserAccount;
import com.zzangse.attendance_check.databinding.FragmentCheckBinding;

public class CheckFragment extends Fragment {
    private FragmentCheckBinding checkBinding;
    private DatabaseReference databaseRef;
    private UserAccount userAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUserInfo(String userId) {
        databaseRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child(userId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // 해당 userID에 해당하는 데이터가 존재하는 경우
                    userAccount = snapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        // 사용자 정보를 가져와서 처리
                        String name = userAccount.getName();
                        String email = userAccount.getId();
                        String birth = userAccount.getBirth();
                        // 필요한 작업을 수행
                        checkBinding.tvTest.setText(name + "\n" + email + "\n" + birth);
                    }
                } else {
                    // 해당 userID에 해당하는 데이터가 없는 경우
                    Log.d("TAG", "No such user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 읽기가 취소된 경우의 처리
                Log.e("TAG", "Database error: " + error.getMessage());
            }
        });
    }

    private void getUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            Log.e("TAG", "userId: " + userId);
            getUserInfo(userId);
        } else {
            Toast.makeText(getActivity(),"getUserId false",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        checkBinding = FragmentCheckBinding.inflate(inflater);
        return checkBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserId();
    }
}