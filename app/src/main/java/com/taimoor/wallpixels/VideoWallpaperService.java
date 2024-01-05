package com.taimoor.wallpixels;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine(this);
    }

    private class VideoEngine extends Engine {

        private ExoPlayer exoPlayer;
        private final Context context;

        VideoEngine(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            exoPlayer = new ExoPlayer.Builder(context).build();

            SharedPreferences sharedPref = getSharedPreferences("WALLPIXELS_PREFS", Context.MODE_PRIVATE);
            String videoUriString = sharedPref.getString("video_wallpaper_uri", null);

            if (videoUriString != null) {
                Uri videoUri = Uri.parse(videoUriString);
                exoPlayer.setMediaItem(MediaItem.fromUri(videoUri));
                exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                exoPlayer.prepare();
                exoPlayer.setPlayWhenReady(true);
            }

            // Set surface for video output
            exoPlayer.setVideoSurface(surfaceHolder.getSurface());

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (exoPlayer != null) {
                exoPlayer.setPlayWhenReady(visible);
            }
        }

        @Override
        public void onDestroy() {
            if (exoPlayer != null) {
                exoPlayer.release();
                exoPlayer = null;
            }
            super.onDestroy();
        }
    }

}