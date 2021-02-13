package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciBuGunDetayAdapter extends RecyclerView.Adapter<KullaniciBuGunDetayAdapter.PostHolder> {

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;

    public KullaniciBuGunDetayAdapter(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList, ArrayList<String> malzemelerList, ArrayList<String> yapilisList) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
    }

    @NonNull
    @Override
    public KullaniciBuGunDetayAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_bu_gun_ne_olsun_yemek_detay_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciBuGunDetayAdapter.PostHolder holder, int position) {
        holder.yemekAdi.setText(YemekAdiList.get(position));
        holder.malzemeler.setText(malzemelerList.get(position));
        holder.yapilis.setText(yapilisList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView yemekAdi;
        TextView malzemeler;
        TextView yapilis;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            yemekAdi = itemView.findViewById(R.id.textViewBuGunNeOlsunYemekDetayAdi);
            imageView = itemView.findViewById(R.id.imageViewBuGunNeOlsunYemekDetayResmi);
            malzemeler = itemView.findViewById(R.id.textViewBuGunNeOlsunYemekDetayMalzemeler);
            yapilis = itemView.findViewById(R.id.textViewBuGunNeOlsunYemekYapilisDetay);
        }
    }
}
