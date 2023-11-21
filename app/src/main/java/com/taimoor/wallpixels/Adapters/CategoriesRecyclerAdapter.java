package com.taimoor.wallpixels.Adapters;

import static android.graphics.Color.GREEN;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taimoor.wallpixels.Listeners.ItemClickListener;
import com.taimoor.wallpixels.Models.CategoriesModel;
import com.taimoor.wallpixels.R;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.Viewholder> {

    Context context;
    List<CategoriesModel> list;
    private ItemClickListener clickListener;

    int selected_position = 0;

    public CategoriesRecyclerAdapter(Context context, List<CategoriesModel> list, ItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item,parent,false);


        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.categoryTxt.setText(list.get(position).getCategoryName());

        holder.categoryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(list.get(position).getCategoryName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView categoryTxt;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            categoryTxt = itemView.findViewById(R.id.categories_item_txt);

        }
    }
}
