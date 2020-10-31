package com.example.bookworm;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Book {
    private String title;
    private String author;
    private String description;
    private String isbn;
    private String status;
    private String owner;
    private String ownerId;
    private String borrower;
//    private ArrayList<Request> requests;
    private String photograph;

    public Book() {

    }

    public Book(String owner, String ownerId) {
        this.owner = owner;
        this.ownerId = ownerId;
        this.status = "available";
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
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the book
     * @param description book's description
     */
    public void setDescription(String description) {
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