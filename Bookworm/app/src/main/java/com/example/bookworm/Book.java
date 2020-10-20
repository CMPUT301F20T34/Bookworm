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

    public Book(String title, String author, String description, String status) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
//        Create ISBN
    }

    /* Create Book object with minimal parameters */
    public Book (String title, String author, String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
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

    /**
     * Returns the photograph of the book
     * @return Image
     */
    public Image getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph
     * @param photograph
     */
    public void setPhotograph(Image photograph) {
        this.photograph = photograph;
    }

    /**
     * Deletes the image of the book without resetting it
     */
    public void deletePhotograph() {
//        Delete photograph
    }
}