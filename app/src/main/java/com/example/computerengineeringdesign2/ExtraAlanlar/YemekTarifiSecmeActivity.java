package com.example.computerengineeringdesign2.ExtraAlanlar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor.YemekTarifiSecmeAdapter;
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

public class YemekTarifiSecmeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private YemekTarifiSecmeAdapter yemekTarifiSecmeAdapter;
    private YemekTarifiSecmeAdapter.RecyclerViewClickListener listener;

    private RecyclerView recyclerView;
    private ArrayList<String> YemekAdiList;
    private ArrayList<String> downloadsUrlList;

    private String gelenYemekKategorisiDeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_tarifi_secme);
        degerleriGetir();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        YemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        recyclerView = findViewById(R.id.recylerViewYemekTarifiSecme);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listener = new YemekTarifiSecmeAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(YemekTarifiSecmeActivity.this, KullaniciYemekDetaylariActivity.class);
                intent.putExtra("yemekDetayAdi",YemekAdiList.get(position));
                intent.putExtra("kategoriAdi",gelenYemekKategorisiDeger);
                startActivity(intent);
            }
        };
        getDataFormFirestore();
        yemekTarifiSecmeAdapter = new YemekTarifiSecmeAdapter(YemekAdiList,downloadsUrlList,listener);
        recyclerView.setAdapter(yemekTarifiSecmeAdapter);

    }

    public void degerleriGetir() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gelenYemekKategorisiDeger = bundle.getString("yemekKategorisi");
        }
    }

    private void getDataFormFirestore() {
        YemekAdiList.removeAll(YemekAdiList);
        downloadsUrlList.removeAll(downloadsUrlList);
        System.out.println("gelenYemekKategorisiDeger : " + gelenYemekKategorisiDeger);
        CollectionReference collectionReference = firebaseFirestore.collection(gelenYemekKategorisiDeger);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(YemekTarifiSecmeActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekAdi = (String) data.get("yemekAdiField");
                        String downloadsUrl = (String) data.get("downloadUrl");

                        YemekAdiList.add(yemekAdi);
                        downloadsUrlList.add(downloadsUrl);
                        yemekTarifiSecmeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}