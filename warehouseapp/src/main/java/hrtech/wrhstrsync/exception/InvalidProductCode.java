package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when a product code is invalid
 */
public class InvalidProductCode extends IllegalArgumentException {

    public InvalidProductCode(String productCode) {
        super("Invalid code for product (" + productCode + ")");
    }
}
