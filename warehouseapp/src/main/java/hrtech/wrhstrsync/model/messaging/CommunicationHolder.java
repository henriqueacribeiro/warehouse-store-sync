package hrtech.wrhstrsync.model.messaging;

import org.json.JSONObject;

/**
 * Class that defines what other communication classes must have
 */
public abstract class CommunicationHolder {

    private final boolean success;
    private final String branchCode;
    private final String subject;

    public CommunicationHolder(boolean success, String branchCode, String subject) {
        this.success = success;
        this.branchCode = branchCode;
        this.subject = subject;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getSubject() {
        return subject;
    }

    /**
     * Converts the model object into a JSON answer
     *
     * @return JSONObject, built from domain info
     */
    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("success", success);
        answer.put("branch_code", branchCode);
        answer.put("subject", subject);
        return answer;
    }
}
