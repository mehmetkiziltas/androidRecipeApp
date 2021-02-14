package com.example.computerengineeringdesign2.KullaniciIslemleri.KullaniciBuGunNeOlsun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciBuGunDetayAdapter;
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

public class KullaniciBuGunNeolsunDetayActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciBuGunDetayAdapter kullaniciBuGunNeliOlsunDetayAdapter;

    private RecyclerView recyclerView;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;

    private String gelenKategoriDeger;
    private String gelenYemekAdiDeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_bu_gun_neolsun_detay);
        degerleriGetir();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        malzemelerList = new ArrayList<>();
        yapilisList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewKullaniciBuGunNeOlsunYemekDetay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kullaniciBuGunNeliOlsunDetayAdapter = new KullaniciBuGunDetayAdapter(yemekAdiList, downloadsUrlList, malzemelerList,yapilisList);
        recyclerView.setAdapter(kullaniciBuGunNeliOlsunDetayAdapter);

        getDataFormFirestore();
    }

    private void getDataFormFirestore() {
        yemekAdiList.removeAll(yemekAdiList);
        downloadsUrlList.removeAll(downloadsUrlList);
        malzemelerList.removeAll(malzemelerList);
        yapilisList.removeAll(yapilisList);
        CollectionReference collectionReference = firebaseFirestore.collection(gelenKategoriDeger);
        collectionReference.whereEqualTo("yemekAdiField", gelenYemekAdiDeger).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(KullaniciBuGunNeolsunDetayActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekAdi = (String) data.get("yemekAdiField");
                        String downloadsUrl = (String) data.get("downloadUrl");
                        String malzemeler = (String) data.get("malzemelerField");
                        String yapilisi = (String) data.get("yemekYapilisField");

                        yemekAdiList.add(yemekAdi);
                        downloadsUrlList.add(downloadsUrl);
                        malzemelerList.add(malzemeler);
                        yapilisList.add(yapilisi);

                        kullaniciBuGunNeliOlsunDetayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void degerleriGetir() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gelenKategoriDeger = bundle.getString("yemekKategoriField");
            gelenYemekAdiDeger = bundle.getString("yemekAdiField");

        }
    }
}