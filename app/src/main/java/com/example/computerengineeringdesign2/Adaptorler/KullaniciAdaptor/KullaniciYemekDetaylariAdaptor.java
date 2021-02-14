package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.content.Context;
import android.content.Intent;
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

import com.example.computerengineeringdesign2.ExtraAlanlar.KullaniciYorumYapmaAlaniActivity;
import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KullaniciYemekDetaylariAdaptor extends RecyclerView.Adapter<KullaniciYemekDetaylariAdaptor.PostHolder> {
    private static final String TAG = "KullaniciYemekDetaylari";

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yemekKategoriList;
    private ArrayList<String> yorumAdiSoyadiList;
    private ArrayList<String> kullaniciYorumuList;

    private ArrayList<String> kullaniciYemekAdiYorumYapildiMiKontrolList;
    private ArrayList<String> begeniSayisiListFB;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private String documentId;
    private int cagirmaSayisi = 0;
    private String kategoriIsmi;
    private FirebaseUser firebaseUser;

    @Exclude
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

    public KullaniciYemekDetaylariAdaptor(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList,
                                          ArrayList<String> malzemelerList, ArrayList<String> yapilisList, ArrayList<String> yemekKategoriList, ArrayList<String> yorumAdiSoyadiList,
                                          ArrayList<String> kullaniciYorumuList, Context mContext) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
        this.yemekKategoriList = yemekKategoriList;
        this.yorumAdiSoyadiList = yorumAdiSoyadiList;
        this.kullaniciYorumuList = kullaniciYorumuList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_yemek_detay_row, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.yemekAdi.setText(YemekAdiList.get(position));
        holder.malzemeler.setText(malzemelerList.get(position));
        holder.yapilis.setText(yapilisList.get(position));
        if (!yorumAdiSoyadiList.isEmpty()) {
            holder.adSoyad.setText(yorumAdiSoyadiList.get(position));
            holder.yorum.setText(kullaniciYorumuList.get(position));
        } else {
            holder.adSoyad.setText("Yorum Yok");
            holder.yorum.setText("Yorum Yok");
        }
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);

        holder.YemekLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Favoriler")
                        .whereEqualTo("yemekAdiField", YemekAdiList.get(position))
                        .whereEqualTo("useEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.e(TAG, "onEvent: ", error);
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String yemekAdi = (String) data.get("yemekAdiField");
                                        kullaniciYemekAdiYorumYapildiMiKontrolList.add(yemekAdi);
                                    }
                                    if (!kullaniciYemekAdiYorumYapildiMiKontrolList.contains(YemekAdiList.get(position))) {
                                        HashMap<String, Object> postData = new HashMap<>();
                                        postData.put("downloadUrl", imageViewList.get(position));
                                        postData.put("useEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                        postData.put("yemekKategorisi", yemekKategoriList.get(position));
                                        postData.put("yemekAdiField", YemekAdiList.get(position));
                                        postData.put("malzemelerField", malzemelerList.get(position));
                                        postData.put("yemekYapilisField", yapilisList.get(position));

                                        firebaseFirestore.collection("Favoriler").add(postData)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(mContext, "Favorilere Kaydedildi", Toast.LENGTH_SHORT).show();

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
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, e.getMessage().toString() + "Hata!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(mContext, "Zaten Beğendin", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
//        begeniSayisi

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KullaniciYorumYapmaAlaniActivity.class);
                intent.putExtra("yemekAdiField", YemekAdiList.get(position));
                intent.putExtra("yemekKategorisi", yemekKategoriList.get(position));
                intent.putExtra("downloadUrl", imageViewList.get(position));
                mContext.startActivity(intent);
            }
        });
    }
    private void BegeniArttir(String isim,String id) {
        System.out.println("getDocumentId() : " + id  + "getKategoriIsmi() : " + isim);
        firebaseFirestore.collection(isim).document(id).update("begeniSayisi", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Ymek Beğeni Sayısı Arttırıldı.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView yemekAdi, adSoyad, yorum;
        TextView malzemeler;
        TextView yapilis;
        ImageView YemekLike;
        Button mButton;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            yemekAdi = itemView.findViewById(R.id.textViewYemekDetayAdi);
            imageView = itemView.findViewById(R.id.imageViewYemekDetayResmi);
            malzemeler = itemView.findViewById(R.id.textViewYemekDetayMalzemeler);
            yapilis = itemView.findViewById(R.id.textViewYemekYapilisDetay);
            YemekLike = itemView.findViewById(R.id.imageViewYemekLikeButton);
            adSoyad = itemView.findViewById(R.id.textViewKullaniciYorumYapanAdiSoyadi);
            yorum = itemView.findViewById(R.id.textViewKullaniciYemekYorumu);
            mButton = itemView.findViewById(R.id.ButtonKullaniciYorumlari);
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            kullaniciYemekAdiYorumYapildiMiKontrolList = new ArrayList<>();
            begeniSayisiListFB = new ArrayList<>();
        }
    }
}
