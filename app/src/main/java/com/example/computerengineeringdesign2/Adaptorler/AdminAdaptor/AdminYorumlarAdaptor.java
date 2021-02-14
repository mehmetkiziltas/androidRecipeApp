package com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminYorumlarAdaptor extends RecyclerView.Adapter<AdminYorumlarAdaptor.PostHolder>{

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> KategoriList;
    private ArrayList<String> yorumYapanAdiSoyadiList;
    private ArrayList<String> yorumList;

    private FirebaseFirestore firebaseFirestore;
    private Context mContext;

    public AdminYorumlarAdaptor(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList,
                                ArrayList<String> kategoriList, ArrayList<String> yorumYapanAdiSoyadiList, ArrayList<String> yorumList, Context mContext) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        KategoriList = kategoriList;
        this.yorumYapanAdiSoyadiList = yorumYapanAdiSoyadiList;
        this.yorumList = yorumList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdminYorumlarAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_yorumlar_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminYorumlarAdaptor.PostHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        try {
            holder.yemekAdi.setText(YemekAdiList.get(position));
            holder.kategori.setText(KategoriList.get(position));
            holder.yorumYapanAdSoyad.setText(yorumYapanAdiSoyadiList.get(position));
            holder.YemekYorum.setText(yorumList.get(position));

            Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        } catch (Exception e) {
            holder.yemekAdi.setText("");
            holder.kategori.setText("");
            holder.yorumYapanAdSoyad.setText("");
            holder.YemekYorum.setText("");
        }

        holder.YorumSilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Yorumlar").whereEqualTo("yemekAdiField",YemekAdiList.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                                notifyDataSetChanged();
                                holder.yemekAdi.setText("");
                                holder.kategori.setText("");
                                holder.yorumYapanAdSoyad.setText("");
                                holder.YemekYorum.setText("");
                            }
                            Toast.makeText(mContext, "Yorum Silindi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView yemekAdi;
        TextView kategori;
        TextView yorumYapanAdSoyad;
        TextView YemekYorum;
        Button YorumSilButton;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            yemekAdi = itemView.findViewById(R.id.textViewAdminYemekYorumYemekAdi);
            imageView = itemView.findViewById(R.id.imageViewAdminYemekYorumYemekResmi);
            kategori = itemView.findViewById(R.id.textViewAdminYemekYorumYemekKategorisi);
            yorumYapanAdSoyad = itemView.findViewById(R.id.textViewAdminYemekYorumYapanAdiSoyadi);
            YemekYorum = itemView.findViewById(R.id.editTextTextAdminYemekYorum);
            YorumSilButton = itemView.findViewById(R.id.buttonAdminYemekYorumYap);

        }
    }
}
