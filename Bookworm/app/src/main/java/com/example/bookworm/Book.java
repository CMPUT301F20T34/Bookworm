package com.example.bookworm;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bookworm.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Book implements Serializable {
    private static ArrayList<String> validStatuses = new ArrayList<String>(Arrays.asList("available", "requested", "accepted", "borrowed"));
    private String title;
    private String author;
    private ArrayList<String> description;
    private String isbn;
    private String status;
    private String owner;
    private String ownerId;
    private String borrower;
    private String borrowerId;
//    private ArrayList<Request> requests;
    private String photograph;

    public Book() { }

    public Book(String title, String author, String description, String isbn, String status) {
        this.title = title;
        this.author = author;
        this.description = new ArrayList<>();
        this.description.addAll(Arrays.asList(description.split(" ")));
        this.isbn = isbn;
        this.status = status;
    }

    public Book(String title, String author, ArrayList<String> description, String isbn, String status) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
    }

    public Book(String title, String author, String username, String status) {
        if (!this.validStatuses.contains(status)){
            throw new IllegalArgumentException("Status must be one of available, requested, accepted, borrowed");
        }
        this.title = title;
        this.author = author;
        this.owner = username;
        this.status = status;

    }

    public Book(String owner, String ownerId) {
        this.owner = owner;
        this.ownerId = ownerId;
        this.status = "available";
    }

    /* Create Book object with minimal parameters */
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String descriptionAsString() {
        return String.join(" ", description);
    }

    /**
     * Gets the title of the book
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the book's description
     * @return String
     */
    public ArrayList<String> getDescription() {
        return description;
    }

    /**
     * Sets the description of the book
     * @param description book's description
     */
    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    /**
     * Gets the ISBN of the book
     * @return String
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the status of the book
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the book (borrowed, available, etc.)
     * @param status status of the book
     */
    public void setStatus(String status) {
        if (!validStatuses.contains(status)){
            throw new IllegalArgumentException("Status must be one of available, requested, accepted, borrowed");
        }
        this.status = status;
    }

    /**
     * Returns the owner for the book
     * @return String
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Sets the owner for the book
     * @param owner owner of the book
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the ownerId for the book
     * @return String
     */
    public String getOwnerId() {
        return this.ownerId;
    }

    /**
     * Sets the ownerId for the book
     * @param ownerId ownerId of the book
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Gets the current borrower of the book
     * @return Borrower
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Sets a new borrower of the book.
     * @param borrower new borrower of the book
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Gets the current borrower's user ID on the book
     * @return
     */
    public String getBorrowerId() {
        return borrowerId;
    }

    /**
     * sets the current borrower's user ID on the book
     * @param borrowerId
     */
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    //
//    public ArrayList<Request> getRequests() {
//        return requests;
//    }
//
//    public void setRequests(ArrayList<Request> requests) {
//        this.requests = requests;
//    }

    /**
     * Returns the photograph of the book
     * @return Image
     */
    public String getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph
     * @param photograph
     */
    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public Bitmap getDrawablePhotograph() {
        return Util.stringToBitMap(photograph);
    }

    /**
     * Deletes the image of the book without resetting it
     */
    public void deletePhotograph() {
//        Delete photograph
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}