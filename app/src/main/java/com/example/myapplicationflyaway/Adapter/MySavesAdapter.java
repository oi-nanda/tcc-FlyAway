package com.example.myapplicationflyaway.Adapter;

import android.annotation.SuppressLint;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationflyaway.Activity.ItineraryPageActivity;
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

public class MySavesAdapter extends RecyclerView.Adapter<MySavesAdapter.ViewHolder> {

    Context context;
    private FirebaseAuth mAuth;
    ArrayList<ItinerarySave> itinerarySaveList;
    DatabaseReference referenceSaves, referenceUser, referenceListSaves;

    public MySavesAdapter(Context context, ArrayList<ItinerarySave> itinerarySaveList) {
        this.context = context;
        this.itinerarySaveList = itinerarySaveList;
    }

    @NonNull
    @Override
    public MySavesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_save_cards,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItinerarySave itinerary = itinerarySaveList.get(position);
        mAuth = FirebaseAuth.getInstance();
        holder.itineraryName.setText(itinerary.getPlaceName());
        holder.date.setText(itinerary.getInicialDate() +" até "+ itinerary.getFinalDate());

        String id = itinerary.getUserId();

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
                intent.putExtra("UserId", itinerary.getUserId());
                intent.putExtra("ItineraryId", itinerary.getId());
                v.getContext().startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceSaves = FirebaseDatabase.getInstance().getReference().child("ItinerariesSave").child(mAuth.getCurrentUser().getUid());

                AlertDialog.Builder buider = new AlertDialog.Builder(context);
                buider.setTitle("Salvar Roteiro");
                buider.setMessage("Tem certeza que deseja salvar este roteiro?");
                buider.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        referenceSaves.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String key = referenceSaves.push().getKey();
                                referenceSaves.child(key).setValue(itinerarySaveList.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Roteiro Salvo", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        return itinerarySaveList.size();
    }

    public void setFilteredList(ArrayList<ItinerarySave> filteredList) {
        this.itinerarySaveList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itineraryName, date, username;
        ImageView like;
        ConstraintLayout itinerary_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itineraryName = itemView.findViewById(R.id.itinerary_save_name);
            date = itemView.findViewById(R.id.data_save);
            username = itemView.findViewById(R.id.username_save_txt);
            like = itemView.findViewById(R.id.like_itinerary);
            itinerary_card = itemView.findViewById(R.id.itinerary_save_cards);

        }
    }
}
