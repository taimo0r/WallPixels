package com.taimoor.wallpixels.Activities;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.VideoWallpaperService;

public class FullScreenVideo extends AppCompatActivity {

    private FloatingActionButton fabDownloadVideo, fabWallpaperVideo;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private String user;
    private Uri uri;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initViews();
        setupPlayer();
        setupDownloadButton();
        setupWallpaperButton();
//        setupActivityResultLauncher();

    }

    private void initViews() {
        builder = new AlertDialog.Builder(this);
        fabDownloadVideo = findViewById(R.id.fab_download_video);
        fabWallpaperVideo = findViewById(R.id.fab_wallpaper_video);
        playerView = findViewById(R.id.full_screen_player);
    }


    private void setupPlayer() {
        String url = getIntent().getStringExtra("video");
        user = getIntent().getStringExtra("user");
        uri = Uri.parse(url);

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(MediaItem.fromUri(uri));
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(false);
    }

    private void setupDownloadButton() {
        fabDownloadVideo.setOnClickListener(view -> showDownloadConfirmationDialog());
    }

    private void showDownloadConfirmationDialog() {
        builder.setTitle("Confirmation")
                .setMessage("Do you want to download this video to your gallery?")
                .setPositiveButton("Yes", (dialog, which) -> downloadVideo())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void setupWallpaperButton() {

        saveVideoUriToPreferences(uri);

        fabWallpaperVideo.setOnClickListener(view -> {
            Intent intent = new Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, VideoWallpaperService.class));
            startActivity(intent);

        });

    }

    private void saveVideoUriToPreferences(Uri uri) {
        SharedPreferences sharedPref = getSharedPreferences("WALLPIXELS_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("video_wallpaper_uri", uri.toString());
        editor.apply();
    }

    private void downloadVideo() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("WallPixels_Live_" + user + "_Pixabay" )
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "WallPixels_" + user + ".mp4");

        downloadManager.enqueue(request);
        Toast.makeText(FullScreenVideo.this, "Downloading Started!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null; // Help the garbage collector
        }
    }
}
