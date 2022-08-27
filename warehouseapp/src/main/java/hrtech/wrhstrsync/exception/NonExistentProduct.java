package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when a product does not exist
 */
public class NonExistentProduct extends IllegalArgumentException {

    public NonExistentProduct() {
        super("Invalid product found");
    }

    public NonExistentProduct(String productCode) {
        super("Product with code " + productCode + " not found");
    }
}
