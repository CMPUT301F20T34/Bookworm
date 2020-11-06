package com.example.bookworm;

import java.util.ArrayList;

/**
 * Represents a user class
 */
public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    private Owner owner;
    private Borrower borrower;

    public User(){ }

    public User(String username, String password, String email, String phone){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.borrower = new Borrower(new ArrayList<Book>(), new ArrayList<Request>());
        this.owner = new Owner(new ArrayList<Book>(), new ArrayList<Request>());
    }

    /**
     * Used for showing books alongside their owners
     * Only uses the minimum info to identify a user;
     * @param username username of the user;
     */
    public User(String username) {
        this.username = username;
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

    public Owner getOwner() { return this.owner; }

    public Borrower getBorrower() { return this.borrower; }
}
