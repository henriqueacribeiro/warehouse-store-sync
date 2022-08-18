package hrtech.wrhstrsync.model.product;

import hrtech.wrhstrsync.exception.InvalidProductCode;
import hrtech.wrhstrsync.exception.InvalidProductName;
import hrtech.wrhstrsync.exception.InvalidProductPrice;
import hrtech.wrhstrsync.model.ModelDefinition;
import hrtech.wrhstrsync.model.dto.ProductDTO;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Optional;

/**
 * Product representation
 */
@Entity
public class Product implements ModelDefinition<ProductDTO> {

    @Id
    private final String code;
    private final String name;
    private final double price;

    protected Product() {
        name = "";
        code = "";
        price = 0.0;
    }

    protected Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Converts the object into its DTO representation
     *
     * @return DTO representation of the object
     */
    @Override
    public ProductDTO toDTO() {
        return new ProductDTO(name, code, price);
    }

    /**
     * Factory to build a Product instance
     */
    public static class ProductFactory {

        /**
         * Validates the name to be used for the product
         *
         * @param nameToUse name to be set. must be non-blank
         * @throws InvalidProductName if the product name is invalid
         */
        private static void validateNameToUse(String nameToUse) throws InvalidProductName {
            if (nameToUse.isBlank()) {
                throw new InvalidProductName(nameToUse);
            }
        }

        /**
         * Validates the code to be used for the product
         *
         * @param codeToUse code to be set. must start with A or B (case-sensitive), followed by 5 numerical characters
         * @throws InvalidProductCode if the product code is invalid
         */
        private static void validateCodeToUse(String codeToUse) throws InvalidProductCode {
            if (!codeToUse.matches("^([AB])(\\d){5}$")) {
                throw new InvalidProductCode(codeToUse);
            }
        }

        private static void validatePriceToUse(double price) throws InvalidProductPrice {
            if (price <= 0.0) {
                throw new InvalidProductPrice(price);
            }
        }

        /**
         * Product builder, accepts parameters and validates them
         *
         * @param name  name of the product. must be valid
         * @param code  code of the product. must be valid
         * @param price price of the product. must be valid
         * @return product built, if parameters are valid, on optional; empty if any parameter is invalid
         */
        public static Optional<Product> buildProduct(String name, String code, double price) throws InvalidProductCode, InvalidProductName, InvalidProductPrice {
            validateNameToUse(name);
            validateCodeToUse(code);
            validatePriceToUse(price);
            return Optional.of(new Product(code, name, price));
        }
    }
}
