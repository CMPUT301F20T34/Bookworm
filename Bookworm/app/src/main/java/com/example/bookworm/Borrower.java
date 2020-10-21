package com.example.bookworm;

public class Borrower {
    private Owner owner;

    public Borrower(){}

    public Borrower(Owner owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner related to this borrower object
     * @param owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
