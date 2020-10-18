package com.example.bookworm;

import java.util.ArrayList;

public interface BookUser {
    public void addBook(Book book);
    public ArrayList<Book> getBooks();
    public void deleteBook(Book book);
}
