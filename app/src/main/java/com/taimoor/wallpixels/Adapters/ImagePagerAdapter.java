package com.taimoor.wallpixels.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ViewHolder> {

    private final List<Hit> images;
    private final Context context;

    public ImagePagerAdapter(Context context, List<Hit> images) {
        this.context = context;
        this.images = images;
    }


    @NonNull
    @Override
    public ImagePagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePagerAdapter.ViewHolder holder, int position) {
        Picasso.get().load(images.get(position).getLargeImageURL()).placeholder(R.drawable.image_placeholder).into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
