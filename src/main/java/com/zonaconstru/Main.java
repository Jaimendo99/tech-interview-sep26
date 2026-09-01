package com.zonaconstru;


public class Main {
    public static void main(String[] args) {
        // TODO: initialize the ItemServiceImp
        ItemService itemService = new ItemServiceImp(null, null);

        //TODO: use ItemServiceImp to start the test

        while (!Thread.currentThread().isInterrupted()) {
            // TODO: main loop to complete test

            try {
                Thread.sleep(1000); // wait 10 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
