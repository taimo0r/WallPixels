package com.taimoor.wallpixels.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Adapters.ImagePagerAdapter;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.Utils;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WallPaperActivity extends AppCompatActivity {

    ViewPager2 viewPagerImages;
    CircleImageView userImage;
    ImageButton downloadBtn, wallpaperBtn, favBtn;
    Dialog dialog;
    Button cancelBtn, confirmBtn;
    int newPosition;
    TextView descTextDialog, username, downloads, views, likes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wall_paper);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPagerImages = findViewById(R.id.viewPagerImages);
        downloadBtn = findViewById(R.id.download_btn);
        wallpaperBtn = findViewById(R.id.wallpaper_btn);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.userName);
        downloads = findViewById(R.id.downloadCount);
        views = findViewById(R.id.viewsCount);
        likes = findViewById(R.id.likesCount);

        dialog = new Dialog(WallPaperActivity.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        cancelBtn = dialog.findViewById(R.id.cancel_button);
        confirmBtn = dialog.findViewById(R.id.confirm_action);
        descTextDialog = dialog.findViewById(R.id.descText);

        List<Hit> photo = (List<Hit>) getIntent().getSerializableExtra("photo");
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, photo);
        viewPagerImages.setAdapter(adapter);

        // Set current item to the selected image
        int currentPosition = getIntent().getIntExtra("selectedImagePosition", 0);
        viewPagerImages.setCurrentItem(currentPosition, false);

        updatedImageData(currentPosition, photo);

        viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                newPosition = position;
                updatedImageData(position, photo);
            }

        });

        downloadBtn.setOnClickListener(view -> {

            descTextDialog.setText("Do you want to download this wallpaper to your Gallery?");

            confirmBtn.setOnClickListener(view12 -> {
                downloadImg(newPosition, photo);
                dialog.dismiss();
            });
            cancelBtn.setOnClickListener(view1 -> dialog.dismiss());

            dialog.show();
        });


        wallpaperBtn.setOnClickListener(view -> {

            descTextDialog.setText("Do you want to set this image as your wallpaper?");

            confirmBtn.setOnClickListener(view14 -> {
                setWallpaper(newPosition, photo);
                dialog.dismiss();
            });
            cancelBtn.setOnClickListener(view13 -> dialog.dismiss());

            dialog.show();
        });

    }

    private void updatedImageData(int currentPosition, List<Hit> photo){

        if (photo.get(currentPosition).getUserImageURL() != null && !photo.get(currentPosition).getUserImageURL().isEmpty()){
            Picasso.get().load(photo.get(currentPosition).getUserImageURL()).error(R.drawable.user_icon).into(userImage);
        }else {
            userImage.setImageResource(R.drawable.user_icon);
        }


        username.setText(photo.get(currentPosition).getUser());
        downloads.setText(Utils.convertToKilo(photo.get(currentPosition).getDownloads()));
        views.setText(Utils.convertToKilo(photo.get(currentPosition).getViews()));
        likes.setText(Utils.convertToKilo(photo.get(currentPosition).getLikes()));

    }


    private void setWallpaper(int currentPosition, List<Hit> photo){

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Glide.with(this)
                .asBitmap()
                .load(photo.get(currentPosition).getWebformatURL()).placeholder(R.drawable.image_placeholder)
                .override(width, height)
                .centerCrop()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void downloadImg(int currentPosition, List<Hit> photo) {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(photo.get(currentPosition).getWebformatURL());


        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("WallPixels_" + photo.get(currentPosition).getUser() + "_Pixabay")
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "WallPixels_" + photo.get(currentPosition).getUser() + ".jpg");

        downloadManager.enqueue(request);

        Toast.makeText(WallPaperActivity.this, "Downloading Started!", Toast.LENGTH_SHORT).show();

    }
}