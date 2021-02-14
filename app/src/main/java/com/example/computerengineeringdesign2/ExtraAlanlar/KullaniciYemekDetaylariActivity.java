package com.example.computerengineeringdesign2.ExtraAlanlar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciYemekDetaylariAdaptor;
import com.example.computerengineeringdesign2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class KullaniciYemekDetaylariActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciYemekDetaylariAdaptor kullaniciYemekDetaylariAdaptor;

    private RecyclerView recyclerView;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yemekKategorisiList;
    private ArrayList<String> kullaniciYorumListFB;
    private ArrayList<String> yorumYapanAdiSoyadiListFB;

    private String gelenKategoriDeger;
    private String gelenYemekAdiDeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_yemek_detaylari);

        degerleriGetir();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        malzemelerList = new ArrayList<>();
        yapilisList = new ArrayList<>();
        yemekKategorisiList = new ArrayList<>();
        kullaniciYorumListFB = new ArrayList<>();
        yorumYapanAdiSoyadiListFB = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewYemekDetay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDataFormFirestore();
        kullaniciYemekDetaylariAdaptor = new KullaniciYemekDetaylariAdaptor(yemekAdiList,downloadsUrlList,malzemelerList,yapilisList,yemekKategorisiList,yorumYapanAdiSoyadiListFB,kullaniciYorumListFB,this);
        recyclerView.setAdapter(kullaniciYemekDetaylariAdaptor);
    }

    private void getDataFormFirestore() {
        yemekAdiList.removeAll(yemekAdiList);
        downloadsUrlList.removeAll(downloadsUrlList);
        malzemelerList.removeAll(malzemelerList);
        yapilisList.removeAll(yapilisList);
        yemekKategorisiList.removeAll(yemekKategorisiList);
        kullaniciYorumListFB.removeAll(kullaniciYorumListFB);
        yorumYapanAdiSoyadiListFB.removeAll(yorumYapanAdiSoyadiListFB);
        CollectionReference collectionReference = firebaseFirestore.collection(gelenKategoriDeger);
        collectionReference.whereEqualTo("yemekAdiField", gelenYemekAdiDeger).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(KullaniciYemekDetaylariActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekAdi = (String) data.get("yemekAdiField");
                        String downloadsUrl = (String) data.get("downloadUrl");
                        String malzemeler = (String) data.get("malzemelerField");
                        String yapilisi = (String) data.get("yemekYapilisField");
                        String yemekKategorisi = (String) data.get("yemekKategorisi");

                        yemekAdiList.add(yemekAdi);
                        downloadsUrlList.add(downloadsUrl);
                        malzemelerList.add(malzemeler);
                        yapilisList.add(yapilisi);
                        yemekKategorisiList.add(yemekKategorisi);


                        CollectionReference colReff = firebaseFirestore.collection("Yorumlar");
                        colReff.whereEqualTo("yemekAdiField", yemekAdi).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(KullaniciYemekDetaylariActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String kullaniciAdiSoyadi = (String) data.get("YorumYapanAdSoyad");
                                        String kullaniciYorumu = (String) data.get("YorumField");

                                        kullaniciYorumListFB.add(kullaniciYorumu);
                                        yorumYapanAdiSoyadiListFB.add(kullaniciAdiSoyadi);
                                        kullaniciYemekDetaylariAdaptor.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void degerleriGetir() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gelenKategoriDeger = bundle.getString("kategoriAdi");
            gelenYemekAdiDeger = bundle.getString("yemekDetayAdi");
        }
    }
}