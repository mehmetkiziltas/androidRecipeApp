package com.example.computerengineeringdesign2.Adaptorler.AdminAdaptor;

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

public class YemekTarifiSecmeAdapter extends RecyclerView.Adapter<YemekTarifiSecmeAdapter.PostHolder>{
    private ArrayList<String> YemekAdiList;
    private ArrayList<String> imageViewList;
    private RecyclerViewClickListener listener;

    public YemekTarifiSecmeAdapter(ArrayList<String> kategoriAdiList, ArrayList<String> imageViewList, RecyclerViewClickListener listener) {
        this.YemekAdiList = kategoriAdiList;
        this.imageViewList = imageViewList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YemekTarifiSecmeAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.yemek_tarif_secme_row,parent,false);
        return new PostHolder(view);
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull YemekTarifiSecmeAdapter.PostHolder holder, int position) {
        holder.yemekAdi.setText(YemekAdiList.get(position));
        Picasso.get().load(imageViewList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return YemekAdiList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView yemekAdi;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            yemekAdi = itemView.findViewById(R.id.textViewYemekAdiRow);
            imageView = itemView.findViewById(R.id.imageViewYemekResmiRow);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v,getAdapterPosition());
            }
        }
    }
}
