package org.acme;


public class UnknownCart extends Exception {

    public UnknownCart() {
        super("No such cart");
    }

}
