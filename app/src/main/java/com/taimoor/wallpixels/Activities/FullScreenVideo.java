package com.taimoor.wallpixels.Activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.FileUtils;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.VideoWallpaperService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class FullScreenVideo extends AppCompatActivity {

    private FloatingActionButton fabDownloadVideo, fabWallpaperVideo;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private String url, user;
    private Uri uri;

    private ActivityResultLauncher<Intent> setWallpaper;
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
        url = getIntent().getStringExtra("video");
        user = getIntent().getStringExtra("user");
        uri = Uri.parse(url);

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // Create a MediaItem from the URI
        MediaItem mediaItem = MediaItem.fromUri(uri);

        // Create a clipping media source to play only the first 15 seconds
        long fifteenSecondsInMicroseconds = 15 * 1000000L; // 15 seconds in microseconds
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSource.Factory(this))
                .createMediaSource(mediaItem);
        ClippingMediaSource clippedSource = new ClippingMediaSource(mediaSource, 0, fifteenSecondsInMicroseconds);

        // Prepare and play the clipped media source
        exoPlayer.setMediaSource(clippedSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
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
        SharedPreferences sharedPref = getSharedPreferences("WALLPIXELS_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("video_wallpaper_uri", uri.toString());
        editor.apply();

        fabWallpaperVideo.setOnClickListener(view -> {
            Toast.makeText(FullScreenVideo.this, "Please Download video to Set as wallpaper", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, VideoWallpaperService.class));
            startActivity(intent);

        });

    }

//    private void setupActivityResultLauncher() {
//        setWallpaper = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Uri selectedUri = result.getData().getData();
//                        if (selectedUri != null) {
//                            String sourcePath = FileUtils.getPath(this, selectedUri);
//                            String destinationPath = getFilesDir() + "/file.mp4";
//                            FileUtils.copyFile(sourcePath, destinationPath);
//                            VideoWallpaperService.setToWallpaper(this);
//                        }
//                    }
//                });
//    }

    private void downloadVideo() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("WallPixels_" + user)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM, "WallPixels_" + user + ".mp4");

        downloadManager.enqueue(request);
        Toast.makeText(FullScreenVideo.this, "Downloading Started!", Toast.LENGTH_SHORT).show();
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        setWallpaper.launch(intent);

    }

    public void copyFile(File fromFile, File toFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            fileChannelInput = fileInputStream.getChannel();
            fileChannelOutput = fileOutputStream.getChannel();
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (fileChannelInput != null)
                    fileChannelInput.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
                if (fileChannelOutput != null)
                    fileChannelOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(final Context context, final Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
