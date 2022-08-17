package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when a product price is invalid
 */
public class InvalidProductPrice extends IllegalArgumentException {

    public InvalidProductPrice(double price) {
        super("Invalid price " + price + " while creating product");
    }
}
