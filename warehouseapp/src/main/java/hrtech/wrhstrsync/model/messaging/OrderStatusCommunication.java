package hrtech.wrhstrsync.model.messaging;

import org.json.JSONObject;

/**
 * Model to be used when an order change its status
 */
public class OrderStatusCommunication extends CommunicationHolder {

    private final String externalOrderID;
    private final int newStatusID;

    public OrderStatusCommunication(boolean success, String branchCode, String externalOrderID, int newStatusID) {
        super(success, branchCode, "STATUS_UPDATE");
        this.externalOrderID = externalOrderID;
        this.newStatusID = newStatusID;
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
        answer.put("status", newStatusID);
        return answer;
    }
}
