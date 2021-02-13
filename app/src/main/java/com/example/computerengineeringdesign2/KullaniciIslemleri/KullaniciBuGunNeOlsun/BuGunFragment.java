package com.example.computerengineeringdesign2.KullaniciIslemleri.KullaniciBuGunNeOlsun;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor.KullaniciBuGunAdapter;
import com.example.computerengineeringdesign2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class BuGunFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private KullaniciBuGunAdapter kullaniciBuGunNeliOlsunAdapter;
    private KullaniciBuGunAdapter.RecyclerViewClickListener listener;

    private RecyclerView recyclerView;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> downloadsUrlList;
    private ArrayList<String> kategoriAdiList;
    private ArrayList<String> kategoriListFromFB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bu_gun, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        yemekAdiList = new ArrayList<>();
        downloadsUrlList = new ArrayList<>();
        kategoriAdiList = new ArrayList<>();
        kategoriListFromFB = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewKullaniciBuGunNeliOlsun);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getDataFormFirestore();

        kullaniciBuGunNeliOlsunAdapter = new KullaniciBuGunAdapter(yemekAdiList, downloadsUrlList, listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.reorder_24px));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(kullaniciBuGunNeliOlsunAdapter);
        listener = new KullaniciBuGunAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), KullaniciBuGunNeolsunDetayActivity.class);
                intent.putExtra("yemekAdiField", yemekAdiList.get(position));
                intent.putExtra("yemekKategoriField", kategoriListFromFB.get(position));
                startActivity(intent);
            }
        };
        return view;
    }

    private void getDataFormFirestore() {
        yemekAdiList.removeAll(yemekAdiList);
        downloadsUrlList.removeAll(downloadsUrlList);
        CollectionReference collectionReference = firebaseFirestore.collection("Kategoriler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String kategoriAdi = (String) data.get("yemekKategorisi");
                        kategoriAdiList.add(kategoriAdi);
                    }
                    Random rand = new Random();
                    int [] array= new int[kategoriAdiList.size()];
                    int num, sameVal = 0;
                    for (int i = 0; i < array.length; i++){
                        sameVal = 0;
                        num = rand.nextInt(kategoriAdiList.size());
                        for (int j = 0; j < i; j++){
                            if (num == array[j]){
                                sameVal--;
                            }
                        }
                        if (sameVal <= -1){
                            i--;
                        }
                        else{
                            array[i] = num;
                        }
                    }
                    for (int k = 0; k < array.length; k++){
                        System.out.println("kategoriAdiList.get(array[k]) : " + kategoriAdiList.get(array[k]) + " array[k] :  "  + array[k]);
                        CollectionReference reference = firebaseFirestore.collection(kategoriAdiList.get(array[k]));
                        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String YemekAdi = (String) data.get("yemekAdiField");
                                        String downloadsUrl = (String) data.get("downloadUrl");
                                        String kategoriFromFB = (String) data.get("yemekKategorisi");

                                        System.out.println(" yemekAdiList.add(YemekAdi) : " + YemekAdi);
                                        kategoriListFromFB.add(kategoriFromFB);
                                        yemekAdiList.add(YemekAdi);
                                        downloadsUrlList.add(downloadsUrl);
                                        kullaniciBuGunNeliOlsunAdapter.notifyDataSetChanged();
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
