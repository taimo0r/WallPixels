package com.taimoor.wallpixels.Models;

public class CategoriesModel {

    String categoryName;

    public CategoriesModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
