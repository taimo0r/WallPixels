package com.taimoor.wallpixels.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Listeners.FavouritesRecyclerClickListener;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    Context context;
    private List<Hit> hits;

    FavouritesRecyclerClickListener listener;

    public FavouritesAdapter(Context context, List<Hit> hits, FavouritesRecyclerClickListener listener) {
        this.context = context;
        this.hits = hits;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.home_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.ViewHolder holder, int position) {

        Picasso.get().load(hits.get(position).getLargeImageURL()).placeholder(R.drawable.image_placeholder).into(holder.imageViewList);
        holder.homeListContainer.setOnClickListener(v -> listener.onCLick(hits, holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return hits.size();
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView homeListContainer;
        ImageView imageViewList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            homeListContainer = itemView.findViewById(R.id.home_list_container);
            imageViewList = itemView.findViewById(R.id.imageview_list);
        }
    }
}
