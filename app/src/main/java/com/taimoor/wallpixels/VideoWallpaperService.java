package com.taimoor.wallpixels;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    private class VideoEngine extends Engine {

        private ExoPlayer exoPlayer;
        private Uri videoUri;
        private static final long PREVIEW_DURATION_MILLIS = 15 * 1000; // 15 seconds

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            exoPlayer = new ExoPlayer.Builder(VideoWallpaperService.this).build();
            exoPlayer.setVideoSurfaceHolder(surfaceHolder);

            SharedPreferences sharedPref = getSharedPreferences("WALLPIXELS_PREFS", Context.MODE_PRIVATE);
            String videoUriString = sharedPref.getString("video_wallpaper_uri", null);
            if (videoUriString != null) {
                videoUri = Uri.parse(videoUriString);
            }

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                // Start or resume playback
                if (exoPlayer != null) {
                    exoPlayer.setMediaItem(MediaItem.fromUri(videoUri));
                    exoPlayer.prepare();
                    exoPlayer.setPlayWhenReady(true);
                }
            } else {
                // Pause playback
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(false);
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (exoPlayer != null) {
                exoPlayer.release();
                exoPlayer = null;
            }
        }
    }

}