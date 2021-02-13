package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciFavoriAdapter extends RecyclerView.Adapter<KullaniciFavoriAdapter.PostHolder>{

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yemekKategoriList;
    private Context mContext;

    private FirebaseFirestore firebaseFirestore;

    public KullaniciFavoriAdapter(ArrayList<String> yemekAdiList,
                                  ArrayList<String> imageViewList, ArrayList<String> malzemelerList, ArrayList<String> yapilisList, ArrayList<String> yemekKategoriList, Context mContext) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
        this.yemekKategoriList = yemekKategoriList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public KullaniciFavoriAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_favorileri_detay_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciFavoriAdapter.PostHolder holder, int position) {
        holder.yemekAdi.setText(YemekAdiList.get(position));
        holder.malzemeler.setText(malzemelerList.get(position));
        holder.yapilis.setText(yapilisList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Favoriler").whereEqualTo("yemekAdiField",YemekAdiList.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                                YemekAdiList.remove(YemekAdiList.get(position));
                            }
                            DocumentReference docRef = firebaseFirestore.collection(yemekKategoriList.get(position)).document(YemekAdiList.get(position));
                            docRef.update("begeniSayisi", FieldValue.increment(-1));
                            Toast.makeText(mContext, "Yemek Tarifi Favorilerden Silindi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return yemekKategoriList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView yemekAdi;
        TextView malzemeler;
        TextView yapilis;
        Button mButton;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            yemekAdi = itemView.findViewById(R.id.textViewKullaniciFavorileriDetayAdi);
            imageView = itemView.findViewById(R.id.imageViewKullaniciFavorileriDetayResmi);
            malzemeler = itemView.findViewById(R.id.textViewKullaniciFavorileriDetayMalzemeler);
            yapilis = itemView.findViewById(R.id.textViewKullaniciFavorileriYapilisDetay);
            mButton = itemView.findViewById(R.id.buttonFavorilerdenSil);
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
    }
}
