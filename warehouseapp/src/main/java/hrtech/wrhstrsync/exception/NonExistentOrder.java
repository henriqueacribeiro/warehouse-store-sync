package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when an order does not exist
 */
public class NonExistentOrder extends IllegalArgumentException {

    public NonExistentOrder() {
        super("The order was not found");
    }

    public NonExistentOrder(String orderCode) {
        super("Order with identifier " + orderCode + " not found");
    }
}
