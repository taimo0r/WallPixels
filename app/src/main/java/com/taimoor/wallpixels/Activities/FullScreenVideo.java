package com.taimoor.wallpixels.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.Utils;
import com.taimoor.wallpixels.VideoWallpaperService;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullScreenVideo extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    ImageButton favBtn, downloadBtn, wallpaperBtn;
    CircleImageView userImage;
    Hit video;
    private String user;
    private Uri uri, wallpaperUri;
    Dialog dialog;
    Button cancelBtn, confirmBtn;
    TextView descTextDialog, username, downloads, views, likes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initViews();
        setupPlayer();
        setupDialogBox();
        setupDownloadButton();
        setupWallpaperButton();

    }

    private void initViews() {
        playerView = findViewById(R.id.full_screen_player);
        downloadBtn = findViewById(R.id.download_btn);
        wallpaperBtn = findViewById(R.id.wallpaper_btn);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.userName);
        downloads = findViewById(R.id.downloadCount);
        views = findViewById(R.id.viewsCount);
        likes = findViewById(R.id.likesCount);
    }


    private void setupPlayer() {
        video = (Hit) getIntent().getSerializableExtra("video");
        String url = video.getVideos().getSmall().getUrl();
        String wallpaperUrl = video.getVideos().getLarge().getUrl();
        user = video.getUser();
        uri = Uri.parse(url);
        wallpaperUri = Uri.parse(wallpaperUrl);

        if (video.getUserImageURL() != null && !video.getUserImageURL().isEmpty()){
            Picasso.get().load(video.getUserImageURL()).error(R.drawable.user_icon).into(userImage);
        }else {
            userImage.setImageResource(R.drawable.user_icon);
        }

        username.setText(user);
        downloads.setText(Utils.convertToKilo(video.getDownloads()));
        views.setText(Utils.convertToKilo(video.getViews()));
        likes.setText(Utils.convertToKilo(video.getLikes()));

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(MediaItem.fromUri(uri));
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(false);
    }

    private void setupDialogBox() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        cancelBtn = dialog.findViewById(R.id.cancel_button);
        confirmBtn = dialog.findViewById(R.id.confirm_action);
        descTextDialog = dialog.findViewById(R.id.descText);
    }

    private void setupDownloadButton() {
        downloadBtn.setOnClickListener(view -> showDownloadConfirmationDialog());
    }

    private void showDownloadConfirmationDialog() {

        descTextDialog.setText("Do you want to download this video to your gallery?");

        confirmBtn.setOnClickListener(view -> {
            downloadVideo();
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }

    private void setupWallpaperButton() {

        saveVideoUriToPreferences(wallpaperUri);

        wallpaperBtn.setOnClickListener(view -> {
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
                .setTitle("WallPixels_Live_" + user + "_Pixabay")
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
            exoPlayer = null;
        }
    }
}
