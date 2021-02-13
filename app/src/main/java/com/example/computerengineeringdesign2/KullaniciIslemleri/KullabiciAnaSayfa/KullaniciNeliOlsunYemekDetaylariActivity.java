package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciNeliOlsunDetayAdaptor;
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

public class KullaniciNeliOlsunYemekDetaylariActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private KullaniciNeliOlsunDetayAdaptor kullaniciNeliOlsunDetayAdaptor;
    private KullaniciNeliOlsunDetayAdaptor.RecyclerViewClickListener listener;

    private ArrayList<String> yemekAdiListFB;
    private ArrayList<String> kategoriAdiListFB;
    private ArrayList<String> imageViewListFB;
    private ArrayList<String> malzemelerListFB;
    private ArrayList<String> yapilisListFB;
    private ArrayList<String> yorumYapanAdiSoyadiListFB;
    private ArrayList<String> yapilanYorumListFB;

    private String gelenKategoriDeger;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_neli_olsun_yemek_detaylari);

        degerleriGetir();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiListFB = new ArrayList<>();
        kategoriAdiListFB = new ArrayList<>();
        imageViewListFB = new ArrayList<>();
        malzemelerListFB = new ArrayList<>();
        yapilisListFB = new ArrayList<>();
        yorumYapanAdiSoyadiListFB = new ArrayList<>();
        yapilanYorumListFB = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewKullaniciNeliOlsunDetay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kullaniciNeliOlsunDetayAdaptor = new KullaniciNeliOlsunDetayAdaptor(yemekAdiListFB, kategoriAdiListFB, imageViewListFB,malzemelerListFB,
                yapilisListFB,yorumYapanAdiSoyadiListFB,yapilanYorumListFB,this,listener);
        recyclerView.setAdapter(kullaniciNeliOlsunDetayAdaptor);

        getDataFormFirestore();
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(KullaniciNeliOlsunYemekDetaylariActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekKategorisi = (String) data.get("yemekKategorisi");

                        CollectionReference colRef = firebaseFirestore.collection(yemekKategorisi);
                        colRef.whereEqualTo("yemekIcerigi",gelenKategoriDeger).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(KullaniciNeliOlsunYemekDetaylariActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String yemekKategorisi = (String) data.get("yemekKategorisi");
                                        String yemekAdi = (String) data.get("yemekAdiField");
                                        String url = (String) data.get("downloadUrl");
                                        String malzemeler = (String) data.get("malzemelerField");
                                        String yapilis = (String) data.get("yemekYapilisField");

                                        yemekAdiListFB.add(yemekAdi);
                                        kategoriAdiListFB.add(yemekKategorisi);
                                        imageViewListFB.add(url);
                                        malzemelerListFB.add(malzemeler);
                                        yapilisListFB.add(yapilis);

                                        CollectionReference colRefYorumCekme = firebaseFirestore.collection("Yorumlar");
                                        colRefYorumCekme.whereEqualTo("yemekAdiField",yemekAdi).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    Toast.makeText(KullaniciNeliOlsunYemekDetaylariActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                                if (value != null) {
                                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                        Map<String, Object> data = snapshot.getData();
                                                        String adSoyad = (String) data.get("YorumYapanAdSoyad");
                                                        String yapilanYorum = (String) data.get("YorumField");

                                                        yorumYapanAdiSoyadiListFB.add(adSoyad);
                                                        yapilanYorumListFB.add(yapilanYorum);
                                                        kullaniciNeliOlsunDetayAdaptor.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        });

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
            gelenKategoriDeger = bundle.getString("YemekNeliOlsun");
        }
    }
}