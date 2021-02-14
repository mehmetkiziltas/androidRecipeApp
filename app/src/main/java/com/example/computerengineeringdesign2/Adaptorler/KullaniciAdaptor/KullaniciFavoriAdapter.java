package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciFavoriAdapter extends RecyclerView.Adapter<KullaniciFavoriAdapter.PostHolder> {

    private static final String TAG = "KullaniciFavoriAdapter";
    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yemekKategoriList;
    private int cagirmaSayisi = 0;
    private String documentId;
    private String kategoriIsmi;
    private Context mContext;

    public String getKategoriIsmi() {
        return kategoriIsmi;
    }

    public void setKategoriIsmi(String kategoriIsmi) {
        this.kategoriIsmi = kategoriIsmi;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

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
        View view = layoutInflater.inflate(R.layout.kullanici_favorileri_detay_row, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciFavoriAdapter.PostHolder holder, int position) {
        try {
            holder.yemekAdi.setText(YemekAdiList.get(position));
            holder.malzemeler.setText(malzemelerList.get(position));
            holder.yapilis.setText(yapilisList.get(position));
            Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        } catch (Exception e) {
            holder.yemekAdi.setText("");
            holder.malzemeler.setText("");
            holder.yapilis.setText("");
        }

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection(yemekKategoriList.get(position)).whereEqualTo("yemekAdiField", YemekAdiList.get(position)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        if (value != null) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                setDocumentId(snapshot.getId());
                                setKategoriIsmi(yemekKategoriList.get(position));
                                System.out.println("getDocumentId() : " + getDocumentId()  + "getKategoriIsmi() : " + getKategoriIsmi());
                                break;
                            }
                            if (cagirmaSayisi == 0) {
                                for (cagirmaSayisi = 0; cagirmaSayisi<1;cagirmaSayisi++ ) {
                                    BegeniArttir(getKategoriIsmi(),getDocumentId());
                                }
                            }
                        }
                    }
                });
                firebaseFirestore.collection("Favoriler").whereEqualTo("yemekAdiField", YemekAdiList.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                                Toast.makeText(mContext, "Silindi", Toast.LENGTH_SHORT).show();
                                YemekAdiList.remove(YemekAdiList.get(position));
                                notifyDataSetChanged();
                            }
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

    private void BegeniArttir(String isim, String id) {
        System.out.println("getDocumentId() : " + id + "getKategoriIsmi() : " + isim);
        firebaseFirestore.collection(isim).document(id).update("begeniSayisi", FieldValue.increment(-1)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Ymek Beğeni Sayısı Azaltıldı!!.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onComplete: ", task.getException());
                }
            }
        });
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
