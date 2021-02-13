package com.example.computerengineeringdesign2.KullaniciIslemleri.KullaniciBuGunNeOlsun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor.YemekTarifiSecmeAdapter;
import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciNeliOlsunYemekSecmeAdaptor;
import com.example.computerengineeringdesign2.ExtraAlanlar.KullaniciYemekDetaylariActivity;
import com.example.computerengineeringdesign2.ExtraAlanlar.YemekTarifiSecmeActivity;
import com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa.KullaniciNeliOlsunYemekDetaylariActivity;
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

public class KullaniciNeliOlsunActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciNeliOlsunYemekSecmeAdaptor kullaniciNeliOlsunYemekSecmeAdaptor;
    private KullaniciNeliOlsunYemekSecmeAdaptor.RecyclerViewClickListener listener;

    private RecyclerView recyclerView;
    private ArrayList<String> YemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> yemekKategorisiList;

    private String gelenYemekKategorisiDeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_neli_olsun);

        degerleriGetir();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        YemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        yemekKategorisiList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewKullaniciNeliOlsun);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listener = new KullaniciNeliOlsunYemekSecmeAdaptor.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(KullaniciNeliOlsunActivity.this, KullaniciYemekDetaylariActivity.class);
                intent.putExtra("yemekDetayAdi",YemekAdiList.get(position));
                intent.putExtra("kategoriAdi",yemekKategorisiList.get(position));
                startActivity(intent);
            }
        };
        getDataFormFirestore();
        kullaniciNeliOlsunYemekSecmeAdaptor = new KullaniciNeliOlsunYemekSecmeAdaptor(YemekAdiList,downloadsUrlList,yemekKategorisiList,this, listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.reorder_24px));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(kullaniciNeliOlsunYemekSecmeAdaptor);
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(KullaniciNeliOlsunActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekKategorisi = (String) data.get("yemekKategorisi");

                        CollectionReference colRef = firebaseFirestore.collection(yemekKategorisi);
                        colRef.whereEqualTo("yemekIcerigi",gelenYemekKategorisiDeger).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(KullaniciNeliOlsunActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String yemekKategorisi = (String) data.get("yemekKategorisi");
                                        String yemekAdi = (String) data.get("yemekAdiField");
                                        String url = (String) data.get("downloadUrl");

                                        YemekAdiList.add(yemekAdi);
                                        downloadsUrlList.add(url);
                                        yemekKategorisiList.add(yemekKategorisi);
                                        kullaniciNeliOlsunYemekSecmeAdaptor.notifyDataSetChanged();
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
            gelenYemekKategorisiDeger = bundle.getString("YemekNeliOlsun");
        }
    }
}