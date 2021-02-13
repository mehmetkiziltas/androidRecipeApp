package com.example.computerengineeringdesign2.KullaniciIslemleri.KullabiciAnaSayfa;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciFavoriAdapter;
import com.example.computerengineeringdesign2.ExtraAlanlar.KullaniciYemekDetaylariActivity;
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

public class FavorilerimActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciFavoriAdapter kullaniciFavoriAdapter;

    private RecyclerView recyclerView;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private ArrayList<String> yemekKategorisiList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__favorilerim);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        malzemelerList = new ArrayList<>();
        yapilisList = new ArrayList<>();
        yemekKategorisiList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewFavorilerim);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDataFormFirestore();

        kullaniciFavoriAdapter = new KullaniciFavoriAdapter(yemekAdiList,downloadsUrlList,malzemelerList,yapilisList,yemekKategorisiList,this);
        recyclerView.setAdapter(kullaniciFavoriAdapter);
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Favoriler");
        collectionReference.whereEqualTo("useEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FavorilerimActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

                        kullaniciFavoriAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}