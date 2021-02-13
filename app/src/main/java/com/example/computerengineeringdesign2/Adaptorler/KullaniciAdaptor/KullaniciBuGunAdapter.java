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

public class KullaniciBuGunAdapter extends RecyclerView.Adapter<KullaniciBuGunAdapter.PostHolder>{

    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private RecyclerViewClickListener listener;

    public KullaniciBuGunAdapter(ArrayList<String> yemekAdiList, ArrayList<String> imageViewList, RecyclerViewClickListener listener) {
        YemekAdiList = yemekAdiList;
        this.imageViewList = imageViewList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KullaniciBuGunAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kullanici_bu_gun_ne_olsun_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciBuGunAdapter.PostHolder holder, int position) {
        try {
            holder.yemekAdi.setText(YemekAdiList.get(position));
            Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView yemekAdi;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            yemekAdi = itemView.findViewById(R.id.textViewKullaniciBuGunNeOlsun);
            imageView = itemView.findViewById(R.id.imageViewKullaniciBuGunNeOlsun);
        }

        @Override
        public void onClick(View v) {
            try {
                if (listener != null) {
                    listener.onClick(v,getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
