package com.example.myapplicationflyaway.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Activity.ItineraryPageActivity;
import com.example.myapplicationflyaway.Fragments.SavesPageFregment;
import com.example.myapplicationflyaway.Model.Itinerary;
import com.example.myapplicationflyaway.Model.ItinerarySave;
import com.example.myapplicationflyaway.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavePageAdapter extends RecyclerView.Adapter<SavePageAdapter.ViewHolder> {

    Context context;
    private FirebaseAuth mAuth;
    ArrayList<ItinerarySave> itineraryArrayList;
    DatabaseReference reference, referenceUser;

    public SavePageAdapter(Context context, ArrayList<ItinerarySave> itineraryArrayList) {
        this.context = context;
        this.itineraryArrayList = itineraryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_card_saves,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItinerarySave save = itineraryArrayList.get(position);
        mAuth = FirebaseAuth.getInstance();
        holder.placename.setText(save.getPlaceName());
        holder.data.setText(save.getInicialDate() +" até "+ save.getFinalDate());

        String id = save.getUserId();

        referenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue().toString();
                holder.username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itinerary_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItineraryPageActivity.class);
                intent.putExtra("UserId", save.getUserId());
                intent.putExtra("ItineraryId", save.getId());
                v.getContext().startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("ItinerariesSave").child(mAuth.getCurrentUser().getUid());

                AlertDialog.Builder buider = new AlertDialog.Builder(context);
                buider.setTitle("Remover Roteiro");
                buider.setMessage("Tem certeza que deseja remover este roteiro dos salvos?");

                buider.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String key = reference.push().getKey();
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    ItinerarySave itinerarySave = snapshot1.getValue(ItinerarySave.class);
                                    if(itinerarySave.getId() == save.getId()){
                                        reference.child(snapshot1.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Roteiro Removido", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                    }

                                }
//
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
                buider.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                buider.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return itineraryArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView placename, data, username;
        ConstraintLayout itinerary_card;
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placename = itemView.findViewById(R.id.itinerary_name);
            data = itemView.findViewById(R.id.data);
            username = itemView.findViewById(R.id.username_txt);
            remove = itemView.findViewById(R.id.remove_itinerary);
            itinerary_card = itemView.findViewById(R.id.itineraryPage_save_card);
        }
    }

}
