package com.example.bookworm;

import android.media.Image;

public class Book {
    private String title;
    private String author;
    private String description;
    private String isbn;
    private String status;
//    private Owner owner;
//    private Borrower borrower;
//    private ArrayList<Request> requests;
    private Image photograph;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public Owner getOwner() {
//        return owner;
//    }
//
//    public void setOwner(Owner owner) {
//        this.owner = owner;
//    }
//
//    public Borrower getBorrower() {
//        return borrower;
//    }
//
//    public void setBorrower(Borrower borrower) {
//        this.borrower = borrower;
//    }
//
//    public ArrayList<Request> getRequests() {
//        return requests;
//    }
//
//    public void setRequests(ArrayList<Request> requests) {
//        this.requests = requests;
//    }

    public Image getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Image photograph) {
        this.photograph = photograph;
    }

    public void deleteImage() {
        this.photograph = null;
    }
}
