package com.example.computerengineeringdesign2.Adaptorler.KullaniciAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computerengineeringdesign2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KullaniciEnCokBegenilenYemekAdaptor extends RecyclerView.Adapter<KullaniciEnCokBegenilenYemekAdaptor.PostHolder>{

    private ArrayList<String> kategoriAdiList;
    private ArrayList<String> imageViewList;
    private ArrayList<String> yemekAdiList;
    private ArrayList<String> malzemelerList;
    private ArrayList<String> yapilisList;

    public KullaniciEnCokBegenilenYemekAdaptor(ArrayList<String> kategoriAdiList, ArrayList<String> imageViewList,
                                               ArrayList<String> yemekAdiList, ArrayList<String> malzemelerList, ArrayList<String> yapilisList) {
        this.kategoriAdiList = kategoriAdiList;
        this.imageViewList = imageViewList;
        this.yemekAdiList = yemekAdiList;
        this.malzemelerList = malzemelerList;
        this.yapilisList = yapilisList;
    }

    @NonNull
    @Override
    public KullaniciEnCokBegenilenYemekAdaptor.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_en_cok_begenilen_yemek_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciEnCokBegenilenYemekAdaptor.PostHolder holder, int position) {
        try {
            holder.kategoriAdi.setText(kategoriAdiList.get(position));
            holder.yemekAdi.setText(yemekAdiList.get(position));
            holder.malzemeler.setText(malzemelerList.get(position));
            holder.yapilis.setText(yapilisList.get(position));
            Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        } catch (Exception e) {
            holder.kategoriAdi.setText("");
            holder.yemekAdi.setText("");
            holder.malzemeler.setText("");
            holder.yapilis.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return kategoriAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView kategoriAdi, yemekAdi;
        EditText yapilis, malzemeler;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            kategoriAdi = itemView.findViewById(R.id.textViewKullaniciEnCokBegenilenYemekKategorisi);
            yemekAdi = itemView.findViewById(R.id.textViewKullaniciEnCokBegenilenYemekAdi);
            malzemeler = itemView.findViewById(R.id.textViewKullaniciEnCokBegenilenYemekMalzemeler);
            yapilis = itemView.findViewById(R.id.textViewKullaniciEnCokBegenilenYemekYapilisDetay);
            imageView = itemView.findViewById(R.id.imageViewKullaniciEnCokBegenilenYemekResmi);

        }
    }
}
