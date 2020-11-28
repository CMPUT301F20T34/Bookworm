package com.example.bookworm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Contains all information of a request, from the book to the creator and timestamp.
 */
public class Request {
    private Book book;
    private Date timestamp;
    private User creator;
    private String status;
    private double lat;
    private double lng;

    public Request(){ }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Request(Book book, User creator, String status){
        this.book = book;
        this.timestamp = new Date();
        this.creator = creator;
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public User getCreator() {
        return creator;
    }

    public String getStatus() {
        return status;
    }

    public double getLat() { return lat; }

    public double getLng() { return lng; }

    public void setStatus(String status){
        this.status = status;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setLat(double lat) { this.lat = lat; }

    public void setLng(double lng) { this.lng = lng; }
}
