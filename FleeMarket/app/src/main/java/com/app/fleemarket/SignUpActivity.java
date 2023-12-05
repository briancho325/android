package com.app.fleemarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fleemarket.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.signupBtn.setOnClickListener(v -> {
            String name = binding.nickName.getText().toString();
            String email = binding.emailET.getText().toString();
            String password = binding.passwordET.getText().toString();
            String password2 = binding.passwordConfirmET.getText().toString();

            if (password.length() < 8) {
                Toast.makeText(this, "비밀빈호는 8자 이상이어야합니다.", Toast.LENGTH_SHORT).show();
            } else if (name.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(password2)) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            signup(name, email, password);
        });
    }

    private void signup(String name, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}