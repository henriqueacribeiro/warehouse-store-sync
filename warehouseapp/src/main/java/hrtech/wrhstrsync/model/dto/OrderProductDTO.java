package hrtech.wrhstrsync.model.dto;

import hrtech.wrhstrsync.model.ModelDTODefinition;
import org.json.JSONObject;

public class OrderProductDTO implements ModelDTODefinition {

    private final ProductDTO product;

    private final int quantity;

    public OrderProductDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Converts the model object into a JSON answer
     *
     * @return JSONObject, built from domain info
     */
    @Override
    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        if (product != null) {
            answer.put("product", product.toJSON());
            answer.put("quantity", quantity);
        }
        return answer;
    }
}
