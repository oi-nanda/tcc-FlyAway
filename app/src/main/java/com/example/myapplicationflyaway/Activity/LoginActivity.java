package com.example.myapplicationflyaway.Activity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myapplicationflyaway.MainActivity;
import com.example.myapplicationflyaway.Model.Users;
import com.example.myapplicationflyaway.R;
import com.example.myapplicationflyaway.Util.ConfigBD;
import com.example.myapplicationflyaway.databinding.ActivityLoginPageBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginPageBinding binding;
    AppCompatButton googleAuth;

    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    EditText passwordEditText;
    String email, password;
    boolean passwordVisible;
    String username,senha, confirmPassword, telefone, bio, localizacao;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        telefone = "(XX) XXXXX-XXXX";
        bio = "Bio Vazia";
        localizacao = "Localização não informada";
        senha = "google";
        confirmPassword = "google";


        mAuth = ConfigBD.FirebaseAutenticacao();
        passwordEditText = binding.editPassword;

        googleAuth = findViewById(R.id.googleAuth);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);


        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent,1234);
            }
        });
        binding.textClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.editEmail.getText().toString();
                password = binding.editPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Compos não preenchidos",
                            Toast.LENGTH_SHORT).show();
                }else{
                    login(email,password);
                }
            }
        });

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=passwordEditText.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = passwordEditText.getSelectionEnd();
                        if(passwordVisible){
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_eye_off, 0);

                            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }
                        else{
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_eye, 0);

                            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        passwordEditText.setSelection(selection);
                        return true;
                    }
                }


                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1234){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    username = account.getDisplayName();
                                    email = account.getEmail();
                                    Users users = new Users(username, email, password, confirmPassword,telefone, bio, localizacao, null);
                                    database = FirebaseDatabase.getInstance();
                                    reference = database.getReference("Users");
                                    String id = mAuth.getUid();

                                    reference.child(id).setValue(users);


                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }catch(ApiException e){
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void HomeActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String excecao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não esta cadastrado";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email ou Senha incorretos";
                            }catch (Exception e){
                                excecao = "Erro ao logar na aplicação" + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), excecao,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}