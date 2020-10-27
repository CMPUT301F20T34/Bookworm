package com.example.bookworm;

import java.util.ArrayList;

public class Library {

    private ArrayList<Owner> owners;
    private ArrayList<Borrower> borrowers;
    private ArrayList<Book> books;
    private ArrayList<Request> requests;

    public Library(){
        owners = new ArrayList<Owner>();
        borrowers = new ArrayList<Borrower>();
        books = new ArrayList<Book>();
        requests = new ArrayList<Request>();
    }

    /**
    public User getProfile(String username){
        //Returns the profile of a user with a given username
    }

    public ArrayList<Book> keywordSearch(String keyword){
        //Returns a list of books matching a keyword
    }

    public String scanBook(String isbn){
        //Scans a book to view its details
    }

    public void giveBook(String isbn){
        //Gives a book to a borrower when scanned by an owner
    }

    public void returnBook(String isbn){
        //Gives a book back to its owner when scanned by a borrower
    }
     */

    public ArrayList<Owner> getOwners() {
        return owners;
    }

    public ArrayList<Borrower> getBorrowers() {
        return borrowers;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Request> getRequests() { return requests; }

    public void addOwner(Owner owner){
        owners.add(owner);
    }

    public void addBorrower(Borrower borrower){
        borrowers.add(borrower);
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void addRequest(Request request){
        requests.add(request);
    }

    public void setOwners(ArrayList<Owner> owners) {
        this.owners = owners;
    }

    public void setBorrowers(ArrayList<Borrower> borrowers) {
        this.borrowers = borrowers;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void setRequests(ArrayList<Request> requests){
        this.requests = requests;
    }
}
