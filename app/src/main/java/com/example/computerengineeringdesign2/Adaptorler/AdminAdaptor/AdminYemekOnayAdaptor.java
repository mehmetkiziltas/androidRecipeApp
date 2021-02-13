package com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

public class AdminYemekOnayAdaptor extends RecyclerView.Adapter<AdminYemekOnayAdaptor.PostHolder> {

    private static final String TAG = "AdminYemekOnayAdaptor";
    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> yemekIcerikList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> kategoriList;
    private Context mContext;

    public AdminYemekOnayAdaptor(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList, ArrayList<String> yemekIcerikList,
                                 ArrayList<String> malzemelerList, ArrayList<String> yapilisList, ArrayList<String> kategoriList, Context mContext) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
        this.kategoriList = kategoriList;
        this.mContext = mContext;
        this.yemekIcerikList = yemekIcerikList;
    }

    @NonNull
    @Override
    public AdminYemekOnayAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_yemek_onay_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminYemekOnayAdaptor.PostHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        holder.yemekAdi.setText(YemekAdiList.get(position));
        holder.malzemeler.setText(malzemelerList.get(position));
        holder.yapilis.setText(yapilisList.get(position));
        holder.yemekIcerik.setText(yemekIcerikList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        holder.YemekOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> postData = new HashMap<>();
                postData.put("downloadUrl", imageViewList.get(position));
                postData.put("malzemelerField", malzemelerList.get(position));
                postData.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                postData.put("yemekAdiField", YemekAdiList.get(position));
                postData.put("yemekYapilisField", yapilisList.get(position));
                postData.put("yemekKategorisi", kategoriList.get(position));
                postData.put("yemekIcerigi", yemekIcerikList.get(position));
                postData.put("begeniSayisi",0);

                firebaseFirestore.collection(kategoriList.get(position)).add(postData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        firebaseFirestore.collection("Onaylanmamıs Listesi").whereEqualTo("yemekAdiField",YemekAdiList.get(position))
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                    Log.d(TAG, "onComplete: ");
                                    Toast.makeText(mContext, "Onay Başarılı", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
            }
        });
        holder.YemekSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Onaylanmamıs Listesi").whereEqualTo("yemekAdiField",YemekAdiList.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView yemekAdi;
        TextView malzemeler,yemekIcerik;
        TextView yapilis;
        Button YemekOnay;
        Button YemekSil;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            yemekIcerik = itemView.findViewById(R.id.textViewAdminYmekOnayYemekOzelligi);
            yemekAdi = itemView.findViewById(R.id.textAdminYemekOnayYemekAdi);
            imageView = itemView.findViewById(R.id.imageViewAdminYemekOnayYemekResmi);
            malzemeler = itemView.findViewById(R.id.textViewAdminYemekOnayYemekMalzemeler);
            yapilis = itemView.findViewById(R.id.textViewAdminYemekOnayYapilisDetay);
            YemekOnay = itemView.findViewById(R.id.AdminOnaylaButton);
            YemekSil = itemView.findViewById(R.id.AdminSilButton);
        }
    }
}
