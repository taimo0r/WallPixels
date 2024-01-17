package com.taimoor.wallpixels.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.taimoor.wallpixels.Database.VideosConverter;

import java.io.Serializable;

@Entity(tableName = "favourites")
public class Hit implements Serializable {

    @PrimaryKey
    private long id;
    private String pageURL;
    private String type;
    private String tags;

    // Fields common to both photos and videos
    private long views;
    private long downloads;
    private int likes;
    private int comments;
    private long user_id;
    private String user;
    private String userImageURL;

    // Fields specific to photos
    private String previewURL;
    private int previewWidth;
    private int previewHeight;
    private String webformatURL;
    private int webformatWidth;
    private int webformatHeight;
    private String largeImageURL;
    private int imageWidth;
    private int imageHeight;
    private long imageSize;
    private int collections;

    // Fields specific to videos
    private int duration; // Duration in seconds
    private String picture_id;
    @TypeConverters(VideosConverter.class)
    private Videos videos;

    // Getters and Setters for all fields


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public int getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(int webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public int getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(int webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }
}

