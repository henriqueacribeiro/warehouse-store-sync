package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when a product name is invalid
 */
public class InvalidProductName extends IllegalArgumentException {

    public InvalidProductName(String productName) {
        super("Invalid name for product (" + productName + ")");
    }
}
