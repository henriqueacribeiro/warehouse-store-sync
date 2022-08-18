package hrtech.wrhstrsync.model.global;

import hrtech.wrhstrsync.model.ModelDefinition;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Model to be used on Controller responses
 *
 * @param <O> type of object that will be returned
 */
public class Response<O extends ModelDefinition> implements Serializable {

    /**
     * Success of called operation
     */
    private final ResponseValue success;
    /**
     * Info that may be pertinent (error messages, for example)
     */
    private final String information;
    /**
     * Object that can possibly be returned
     */
    private final O objectToReturn;

    public Response(ResponseValue success, String information, O objectToReturn) {
        this.success = success;
        this.information = information;
        this.objectToReturn = objectToReturn;
    }

    public boolean isSuccess() {
        return success.isSuccess();
    }

    public boolean failureWasClientError() {
        return success.getId() == 1;
    }

    public boolean failureWasServerError() {
        return success.getId() == 2;
    }

    /**
     * Converts the model object into a JSON answer
     *
     * @return JSONObject, built from domain info
     */
    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("success", success.isSuccess());
        if (!information.isBlank()) {
            answer.put("message", information);
        }
        if (objectToReturn != null) {
            answer.put("object", objectToReturn.toJSON());
        }
        return answer;
    }

    public String getInformation() {
        return information;
    }

    public O getObjectToReturn() {
        return objectToReturn;
    }

    public enum ResponseValue {
        SUCCESS(0, true), CLIENT_FAIL(1, false), SERVER_FAIL(2, false);

        private final int id;
        private final boolean success;

        ResponseValue(int id, boolean success) {
            this.id = id;
            this.success = success;
        }

        public int getId() {
            return id;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
