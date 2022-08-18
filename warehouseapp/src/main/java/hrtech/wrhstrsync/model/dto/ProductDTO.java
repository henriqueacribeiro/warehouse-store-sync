package hrtech.wrhstrsync.model.dto;

import hrtech.wrhstrsync.model.ModelDTODefinition;
import org.json.JSONObject;

/**
 * Representation of the product for transport
 */
public class ProductDTO implements ModelDTODefinition {

    private final String name;
    private final String code;
    private final double price;

    public ProductDTO(String name, String code, double price) {
        this.name = name;
        this.code = code;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Converts the object into its JSON representation
     *
     * @return JSON object with info about the object
     */
    @Override
    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("code", code);
        answer.put("name", name);
        answer.put("price", price);
        return answer;
    }
}
