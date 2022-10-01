package hrtech.wrhstrsync.model.messaging;

import org.json.JSONObject;

/**
 * Model to be used when an order is cancelled
 */
public class OrderCancelCommunication extends CommunicationHolder {

    private final String externalOrderID;

    public OrderCancelCommunication(String storeCode, String externalOrderID) {
        super(true, storeCode, "ORDER_CANCELLATION");
        this.externalOrderID = externalOrderID;
    }

    /**
     * Converts the model object into a JSON answer
     *
     * @return JSONObject, built from domain info
     */
    @Override
    public JSONObject toJSON() {
        JSONObject answer = super.toJSON();
        answer.put("order_id", externalOrderID);
        return answer;
    }
}
