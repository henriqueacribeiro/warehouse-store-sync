package hrtech.wrhstrsync.exception;

/**
 * Exception to be thrown when the status change is illegal
 */
public class IllegalOrderStatusChange extends IllegalArgumentException {

    public IllegalOrderStatusChange(String previousStatusText, String nextStatusText) {
        super("Cannot go from " + previousStatusText + " to " + nextStatusText);
    }
}
