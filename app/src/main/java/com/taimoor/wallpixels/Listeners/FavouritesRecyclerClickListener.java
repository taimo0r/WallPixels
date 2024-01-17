package com.taimoor.wallpixels.Listeners;

import android.view.View;


import com.taimoor.wallpixels.Models.Hit;

import java.util.List;

public interface FavouritesRecyclerClickListener {

    View.OnClickListener onCLick(List<Hit> photo, int selectedPosition);

}
