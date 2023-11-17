package com.example.myapplicationflyaway.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationflyaway.Activity.LoginActivity;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

    View view;
    TextView editar_perfil, alterar_senha, sair, usernameTextView, emailTextView;
    Button excluir_conta;
    ImageView editar_email, profile_pic_settings;
    FirebaseUser user;
    FirebaseAuth mUser;
    Dialog myDialog;
    String uid;
    View view2, view3, view4;


    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
    String passwordPattern = "^(?=.*[A-Z])" + ".{8,16}$";
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        editar_email = view.findViewById(R.id.editar_email_settings);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        sair = view.findViewById(R.id.button_sair_settings);
        excluir_conta = view.findViewById(R.id.button_excluir_conta_settings);
        editar_perfil = view.findViewById(R.id.editar_perfil_settings);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        alterar_senha = view.findViewById(R.id.alterar_senha_settings);
        profile_pic_settings = view.findViewById(R.id.profile_pic_settings);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        myDialog = new Dialog(getContext());


        getUserinfo();
        editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, EditProfileInfoFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        editar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpAlterarEmail(view4);

            }
        });

        excluir_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
                buider.setTitle("Tem certeza?");
                buider.setMessage("Deletar conta não pode ser desfeito");

                buider.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                        Toast.makeText(getContext(), "Usuário deletado com sucesso", Toast.LENGTH_SHORT).show();
                        user.delete();
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(uid).removeValue();
                        FirebaseAuth.getInstance().signOut();


                    }
                });

                buider.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                buider.show();
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(view2);
            }
        });

        alterar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChangePasswordPopUp(view3);
            }
        });

        return view;
   }

    private void getUserinfo() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usernameTextView.setText(snapshot.child("username").getValue().toString());
                emailTextView.setText(snapshot.child("email").getValue().toString());

                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profile_pic_settings);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ShowPopup(View v){
        Button confirm, cancel;

        myDialog.setContentView(R.layout.exit_popup);
        confirm = myDialog.findViewById(R.id.button_confirm);
        cancel = myDialog.findViewById(R.id.button_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void ShowChangePasswordPopUp(View v) {
        ImageButton close;
        Button save;
        EditText new_Password, confirm_Password;

        myDialog.setContentView(R.layout.change_password_popup);
        close = myDialog.findViewById(R.id.btn_close);
        save = myDialog.findViewById(R.id.btn_save_newpassword);
        EditText password = myDialog.findViewById(R.id.new_password);
        EditText confirmPassword = myDialog.findViewById(R.id.confirm_newpassword);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference useP = databaseReference.child(mAuth.getCurrentUser().getUid());
                useP.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String credentialPP = snapshot.child("password").getValue(String.class);
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), credentialPP);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(reauthTask -> {
                                    if (reauthTask.isSuccessful()) {
                                        if(password.getText().toString().isEmpty() || password.getText().toString().length()<8 || !password.getText().toString().matches(passwordPattern) ){
                                            Toast.makeText(getContext(), "Senha não segue o padrão necessário", Toast.LENGTH_SHORT).show();
                                        password.setText("");
                                        confirmPassword.setText("");
                                        }
                                        else if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                                            user.updatePassword(password.getText().toString())
                                                    .addOnCompleteListener(updatePasswordTask -> {
                                                        if (updatePasswordTask.isSuccessful()) {
                                                            databaseReference.child(mAuth.getCurrentUser().getUid()).child("password").setValue(password.getText().toString());
                                                            databaseReference.child(mAuth.getCurrentUser().getUid()).child("confirmPassword").setValue(password.getText().toString());
                                                            myDialog.dismiss();

                                                            Toast.makeText(getContext(), "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            myDialog.dismiss();
                                                            Toast.makeText(getContext(), "Erro ao atualizar a senha", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Senhas incompativeis", Toast.LENGTH_SHORT).show();
                                            password.setText("");
                                            confirmPassword.setText("");
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Erro na reautenticação", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



    public void ShowPopUpAlterarEmail(View v) {

        ImageButton close;
        Button save;
        EditText email, confirm_email;

        myDialog.setContentView(R.layout.change_email_popup);
        close = myDialog.findViewById(R.id.btn_close);
        save = myDialog.findViewById(R.id.btn_save_newEmail);
        email = myDialog.findViewById(R.id.new_email);
        confirm_email = myDialog.findViewById(R.id.confirm_newemail);



         save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference useP = databaseReference.child(mAuth.getCurrentUser().getUid());
                useP.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String credentialPP = snapshot.child("password").getValue(String.class);
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), credentialPP);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(reauthTask -> {
                                    if (reauthTask.isSuccessful()) {
                                        if(email.getText().toString().isEmpty() || !email.getText().toString().matches(emailPattern) ){
                                            Toast.makeText(getContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                                            email.setText("");
                                            confirm_email.setText("");
                                        }
                                         if(email.getText().toString().equals(confirm_email.getText().toString())) {
                                             String el = email.getText().toString();

                                            user.updateEmail(el).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                databaseReference.child(mAuth.getCurrentUser().getUid()).child("email").setValue(el);
                                                                myDialog.dismiss();
                                                                Toast.makeText(getContext(), "Email atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                myDialog.dismiss();
                                                                Toast.makeText(getContext(), "Não foi possivel alterar email!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }
                                        else {
                                            Toast.makeText(getContext(), "Email's incompativeis", Toast.LENGTH_SHORT).show();
                                            email.setText("");
                                            confirm_email.setText("");
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Erro na reautenticação", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
            }


    }