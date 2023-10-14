package com.example.myapplicationflyaway.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationflyaway.MainActivity;
import com.example.myapplicationflyaway.Model.Users;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    String username, email, password, confirmPassword;
    FirebaseDatabase db;
    DatabaseReference reference;
    ImageView back_loginPage;
    ProgressDialog progressDialog;
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
    String passwordPattern = "^(?=.*[A-Z])" + ".{8,16}$";
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();


        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
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

    private void performAuth() {
        email = binding.editCreateAccountEmail.getText().toString();
        username = binding.editCreateAccountUsername.getText().toString();
        password = binding.editCreateAccountPassword.getText().toString();
        confirmPassword = binding.editCreateAccountConfirmPassword.getText().toString();

        if(!email.matches(emailPattern)){
            binding.editCreateAccountEmail.setError("Email inválido");
        }
        else if(password.isEmpty() || password.length()<8 || !password.matches(passwordPattern) ){
            binding.editCreateAccountPassword.setError("Senha não segue os padrões necessários");
        }
        else if(!password.equals(confirmPassword)){
            binding.editCreateAccountConfirmPassword.setError("Senhas devem ser iguais");
        }
        else if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(CreateAccountActivity.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
        } else{
            progressDialog.setMessage("Realizando cadastro de usuário...");
            progressDialog.setTitle("Cadastro");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();



            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
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
                            }
                        });

                        progressDialog.dismiss();
                        sendUserToMainPage();
                        Toast.makeText(CreateAccountActivity.this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(CreateAccountActivity.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToMainPage() {
        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}