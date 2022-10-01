package hrtech.wrhstrsync.exception;

/**
 * To be used when an error synchronizing a store/warehouse arises
 */
public class StoreWarehouseSynchronizationError extends IllegalArgumentException {

    public StoreWarehouseSynchronizationError(SyncErrorType errorType) {
        super("Error while " + errorType.getTaskDescription());
    }

    public StoreWarehouseSynchronizationError(SyncErrorType errorType, String extraDetail) {
        super("Error while " + errorType.getTaskDescription() + ": " + extraDetail);
    }

    public enum SyncErrorType {
        ERROR_ON_STORE_TO_WAREHOUSE_MESSAGE("analyzing message from store to warehouse"),
        UNKNOWN_SUBJECT_STORE_TO_WAREHOUSE_MESSAGE("analyzing subject: store to warehouse subject invalid"),
        INITIAL_SYNC("performing initial synchronization"),
        ORDER_STATUS_UPDATE_SYNC("updating order status to store"),
        ORDER_FROM_STORE_SYNC("creating order from store");

        private final String taskDescription;

        SyncErrorType(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public String getTaskDescription() {
            return taskDescription;
        }
    }
}
