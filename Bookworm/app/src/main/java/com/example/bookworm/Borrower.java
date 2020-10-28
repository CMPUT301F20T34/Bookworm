package com.example.bookworm;

import java.util.ArrayList;

public class Borrower extends User implements BookUser {
    private Owner owner;

    public Borrower(){}

    public Borrower(String username, String password, String email, String phone) {
        super(username, password, email, phone);
        this.owner = owner;
    }

    /**
     * Adds the owner related to this borrower object
     * @param owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }
    public ArrayList<Book> getBooks() {
        return this.books;
    }
    public void deleteBook(Book book) {
        this.books.remove(book);
    }
}
