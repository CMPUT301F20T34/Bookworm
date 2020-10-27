package com.example.bookworm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;

public class Request {
    private Book book;
    private Date timestamp;
    private Borrower creator;
    private String status;

    public Request(){ }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Request(Book book, Borrower creator, String status){
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

    public Borrower getCreator() {
        return creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCreator(Borrower creator) {
        this.creator = creator;
    }
}
