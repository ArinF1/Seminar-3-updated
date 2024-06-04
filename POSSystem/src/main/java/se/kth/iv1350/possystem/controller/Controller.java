package se.kth.iv1350.possystem.controller;

import java.util.List;
import se.kth.iv1350.possystem.integration.ExternalAccountingSystem;
import se.kth.iv1350.possystem.integration.ExternalInventorySystem;
import se.kth.iv1350.possystem.integration.Printer;
import se.kth.iv1350.possystem.model.Item;
import se.kth.iv1350.possystem.model.Payment;
import se.kth.iv1350.possystem.model.Sale;
import se.kth.iv1350.possystem.model.SaleDTO;

/**
 * The controller class of the POS system which handle communication
 * between view, model, and external systems
 */
public class Controller {
    private ExternalAccountingSystem accounting;
    private ExternalInventorySystem inventory;
    private Printer printer; 
    private Sale sale;
    
    
    /**
	 * Creats instance of the controller object.
	 * @param printer external system to print receipt
	 * @param accounting external accounting system.
	 * @param inventory external inventory system.
	 */
    public Controller(Printer printer, ExternalAccountingSystem accounting, ExternalInventorySystem inventory) {
        this.printer = printer;
        this.accounting = accounting;
        this.inventory = inventory;
        inventory.addItem();
    }
    
    /**
	 * Starts a new sale, first method to be called when starting a sale
	 * @return getSaleInformation returns the information of the sale
	 */
    public SaleDTO startSale() {
        this.sale = new Sale();
        return sale.getSaleInformation();
    }
    
    /**
     * Adds an item to the sale by scanningen the barcode.
     * @param barCode a bar code which identifies an item
     * @param quantity The amount of the a single item a customer is buying
     * @return SaleDTO, 
     */
    public SaleDTO enterItem(int barCode, int quantity) {
    	Item item = inventory.search(barCode);
    	if(item == null){
            return null;
        }
    	if(item.getStoreQuantity() >= quantity) {
            sale.addItem(item, quantity);
    	}
        
        else {
            return null;
    	}
        
    	return this.sale.getSaleInformation();
    }
    
        /**
	 * Ends a sale is called when all items has been scanned. And updates stores inventory
	 * @return a SaleDTO is being returned with information about the sale
	 */
    public SaleDTO endSale() {
    	List<Integer> itemsQuantity = sale.getCustomerItemsQuantity();
        List<Item> items = sale.getItems();

        for (Item item : items) {
            for (Item item2 : this.inventory.getStoreItems()) {
                if (item == item2) {
                       this.inventory.getStoreItems().get(this.inventory.getStoreItems().indexOf(item2)).updateQuantity(itemsQuantity.get(items.indexOf(item)));
                    }
		}
            }
	
    	return this.sale.getSaleInformation();
    }
    
    /**
	 * Calculates change and updates the ecternal accounting system
	 * @param amount describes the amount paid by the customer
	 * @return change amount of change the customer should receive
	 */
    public Payment pay(double amount,  double totalPrice) {
        double change = amount - totalPrice;
        String paymentMessage = "Approved";
    
        if(change >= 0){
            this.accounting.update(amount - change);
        }
        else{
            paymentMessage = "To little money";
            change = 0;
        }
        return new Payment(change, paymentMessage);
    }

    
    /**
     * printer prints the receipt for the sale.
     */
    public void print() {
    	printer.print(this.sale.getReceipt(sale));
    }
}
