package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when a quantity is invalid
 */
public class InvalidQuantity extends IllegalArgumentException {

    public InvalidQuantity(String invalidQuantityValue) {
        super("Invalid quantity for product (" + invalidQuantityValue + ")");
    }

    public InvalidQuantity(String productName, String invalidQuantityValue) {
        super("Invalid quantity for product " + productName + " (" + invalidQuantityValue + ")");
    }
}
