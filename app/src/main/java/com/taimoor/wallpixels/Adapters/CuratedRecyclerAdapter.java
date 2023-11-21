package com.taimoor.wallpixels.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Listeners.onRecyclerClickListener;
import com.taimoor.wallpixels.Models.Photo;
import com.taimoor.wallpixels.R;

import java.util.List;

public class CuratedRecyclerAdapter extends RecyclerView.Adapter<CuratedRecyclerAdapter.ViewHolder> {
    Context context;
    List<Photo> list;
    onRecyclerClickListener listener;

    public CuratedRecyclerAdapter(Context context, List<Photo> list, onRecyclerClickListener listener) {
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
        Picasso.get().load(list.get(position).getSrc().getLarge()).placeholder(R.drawable.image).into(holder.imageViewList);

        holder.homeListContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCLick(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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


