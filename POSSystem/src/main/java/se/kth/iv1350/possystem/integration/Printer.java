package se.kth.iv1350.possystem.integration;

import se.kth.iv1350.possystem.model.Receipt;
import se.kth.iv1350.possystem.model.Item;


/**
 *External printer which prints of the receipt
 */
public class Printer {
    private double totalVAT = 0;
    private double totalPrice = 0;
    
    
    public Printer() {
    }
    
    /**
     * Prints a recepit with the sales information.
     * @param receipt The recepit.
     */
    public void print(Receipt receipt) {
        System.out.println("Printing receipt... ");
        System.out.println("Date and time of purchase: " + receipt.getTimeOfSale());
        System.out.println("Store: " + receipt.getNameOfStore());
        System.out.println("Total discount: " + receipt.getTotalDiscount());
        
        System.out.println("Items: ");
        for(Item item : receipt.getNameOfItems()){
            System.out.println("- " + item.getItemDTO().getItemName() + " " + item.getItemDTO().getPrice() + " SEK");
            totalVAT += item.getItemDTO().getVAT();
            totalPrice += item.getItemDTO().getPrice();
        } 
        System.out.println("Total price: " + totalPrice);
        System.out.println("Total VAT: " + totalVAT);
    }
}

