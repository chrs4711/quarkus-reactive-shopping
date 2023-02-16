package org.acme;

public class UnknownProduct extends Exception {

    UnknownProduct() {
        super("No such product");
    }
}
