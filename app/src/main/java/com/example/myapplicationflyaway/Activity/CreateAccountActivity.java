package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Model.Users;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    String username, email, password, confirmPassword;
    FirebaseDatabase db;
    DatabaseReference reference;
    ImageView back_loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.editCreateAccountEmail.getText().toString();
                username = binding.editCreateAccountUsername.getText().toString();
                password = binding.editCreateAccountPassword.getText().toString();
                confirmPassword = binding.editCreateAccountConfirmPassword.getText().toString();

                if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){

                    Users users = new Users(username, email, password, confirmPassword);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");
                    reference.child(username).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.editCreateAccountEmail.setText("");
                            binding.editCreateAccountUsername.setText("");
                            binding.editCreateAccountPassword.setText("");
                            binding.editCreateAccountConfirmPassword.setText("");
                            Toast.makeText(CreateAccountActivity.this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(CreateAccountActivity.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        back_loginPage = findViewById(R.id.back_loginPage);
        back_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }



}