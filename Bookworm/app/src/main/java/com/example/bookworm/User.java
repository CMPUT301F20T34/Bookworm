package com.example.bookworm;

import android.app.DownloadManager;

import java.util.ArrayList;

public class User {
    String username;
    String password;
    String email;
    String phone;
    ArrayList<Book> books;
    ArrayList<Request> requests;

    public User(){ }

    public User(String username, String password, String email, String phone){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        books = new ArrayList<Book>();
        requests = new ArrayList<Request>();
    }

    public void editContact(String email, String phone){
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<Book> getBooks() { return books; };

    public ArrayList<Request> getRequests(){
        return requests;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }
}
