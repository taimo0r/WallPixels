package com.taimoor.wallpixels.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.taimoor.wallpixels.Listeners.VideoRecyclerClickListener;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.util.List;

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.Viewholder> {

    Context context;
    List<Hit> list;
    VideoRecyclerClickListener listener;

    public VideosRecyclerAdapter(Context context, List<Hit> list, VideoRecyclerClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);


        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        String url = list.get(position).getVideos().getSmall().getUrl();
        String user = list.get(position).getUser();
        Uri uri = Uri.parse(url);


        MediaItem mediaItem = MediaItem.fromUri(uri);
        ExoPlayer exoPlayer = new ExoPlayer.Builder(context).build();
        holder.playerView.setPlayer(exoPlayer);

        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(false);

        holder.playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onVideoClick(url, user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        PlayerView playerView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.exo_player_view);
        }
    }

    @Override
    public void onViewRecycled(@NonNull Viewholder holder) {
        if (holder.playerView.getPlayer() != null) {
            holder.playerView.getPlayer().release();
        }
    }

    public void updateVideos(List<Hit> newVideos) {
        list.clear();
        list.addAll(newVideos);
        notifyDataSetChanged();
    }


}
