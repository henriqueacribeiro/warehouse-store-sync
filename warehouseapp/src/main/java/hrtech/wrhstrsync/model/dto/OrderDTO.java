package hrtech.wrhstrsync.model.dto;

import hrtech.wrhstrsync.model.ModelDTODefinition;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class OrderDTO implements ModelDTODefinition {

    private final String status;

    private final List<OrderProductDTO> products;

    public OrderDTO(String status, List<OrderProductDTO> products) {
        this.status = status;
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderProductDTO> getProducts() {
        return products;
    }

    /**
     * Converts the model object into a JSON answer
     *
     * @return JSONObject, built from domain info
     */
    @Override
    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("status", status);

        JSONArray array = new JSONArray();
        for (OrderProductDTO orderProduct : products) {
            array.put(orderProduct.toJSON());
        }
        answer.put("products", array);

        return answer;
    }
}
