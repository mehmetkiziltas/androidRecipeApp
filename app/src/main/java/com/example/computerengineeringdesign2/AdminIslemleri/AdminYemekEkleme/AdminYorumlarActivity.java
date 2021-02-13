package com.example.computerengineeringdesign2.AdminIslemleri.AdminYemekEkleme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor.AdminYorumlarAdaptor;
import com.example.computerengineeringdesign2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminYorumlarActivity extends AppCompatActivity {

    private AdminYorumlarAdaptor adminYorumlarAdaptor;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerView recyclerView;

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> KategoriList;
    private ArrayList<String> yorumYapanAdiSoyadiList;
    private ArrayList<String> yorumList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_yorumlar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        YemekAdiList = new ArrayList<>();
        imageViewList = new ArrayList<>();
        KategoriList = new ArrayList<>();
        yorumYapanAdiSoyadiList = new ArrayList<>();
        yorumList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewAdminYorumlar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDataFormFirestore();

        adminYorumlarAdaptor = new AdminYorumlarAdaptor(YemekAdiList,imageViewList, KategoriList, yorumYapanAdiSoyadiList, yorumList, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.reorder_24px));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adminYorumlarAdaptor);
    }

    private void getDataFormFirestore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Yorumlar");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AdminYorumlarActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String yemekAdi = (String) data.get("yemekAdiField");
                        String downloadsUrl = (String) data.get("downloadUrl");
                        String yorumYapanAdSoyad = (String) data.get("YorumYapanAdSoyad");
                        String Yorumlar = (String) data.get("YorumField");
                        String kategori = (String) data.get("yemekKategorisi");

                        YemekAdiList.add(yemekAdi);
                        imageViewList.add(downloadsUrl);
                        KategoriList.add(kategori);
                        yorumYapanAdiSoyadiList.add(yorumYapanAdSoyad);
                        yorumList.add(Yorumlar);
                        adminYorumlarAdaptor.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}