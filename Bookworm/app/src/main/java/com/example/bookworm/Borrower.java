package com.example.bookworm;

import java.util.ArrayList;

public class Borrower implements BookUser {
    private ArrayList<Book> books;
    private ArrayList<Request> requests;

    public Borrower() { }

    public Borrower(ArrayList<Book> books, ArrayList<Request> requests) {
        this.books = books;
        this.requests = requests;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }
    public ArrayList<Book> getBooks() { return this.books; }
    public ArrayList<Request> getRequests() { return this.requests; }
    public void deleteBook(Book book) {
        this.books.remove(book);
    }
}
