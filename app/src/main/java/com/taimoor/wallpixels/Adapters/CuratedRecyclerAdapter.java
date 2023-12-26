package com.taimoor.wallpixels.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Listeners.onRecyclerClickListener;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.util.List;

public class CuratedRecyclerAdapter extends RecyclerView.Adapter<CuratedRecyclerAdapter.ViewHolder> {
    Context context;
    List<Hit> list;
    onRecyclerClickListener listener;

    public CuratedRecyclerAdapter(Context context, List<Hit> list, onRecyclerClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getLargeImageURL()).into(holder.imageViewList);
        holder.homeListContainer.setOnClickListener(view -> listener.onCLick(list.get(holder.getAdapterPosition())));
        holder.username.setText("By: " + list.get(position).getUser());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView homeListContainer;
        ImageView imageViewList;
        Chip username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            homeListContainer = itemView.findViewById(R.id.home_list_container);
            imageViewList = itemView.findViewById(R.id.imageview_list);
            username = itemView.findViewById(R.id.userName);

        }
    }
}


