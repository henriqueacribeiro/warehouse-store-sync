package hrtech.wrhstrsync.model.order;

import hrtech.wrhstrsync.exception.InvalidQuantity;
import hrtech.wrhstrsync.exception.NonExistentOrder;
import hrtech.wrhstrsync.exception.NonExistentProduct;
import hrtech.wrhstrsync.model.ModelDefinition;
import hrtech.wrhstrsync.model.dto.OrderProductDTO;
import hrtech.wrhstrsync.model.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Table(name = "order_product")
public class OrderProduct implements Serializable, ModelDefinition<OrderProductDTO> {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private final Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private final Product product;

    @Column(name = "quantity", nullable = false)
    private final int quantity;

    protected OrderProduct() {
        order = null;
        product = null;
        quantity = 0;
    }

    protected OrderProduct(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Method that converts the model into DTO
     *
     * @return representation of current object in DTO format
     */
    @Override
    public OrderProductDTO toDTO() {
        return new OrderProductDTO(product.toDTO(), quantity);
    }

    /**
     * Factory to build an OrderProduct instance
     */
    public static class OrderProductFactory {

        /**
         * Validates the order to be used for the object
         *
         * @param orderToUse order to be used
         * @throws NonExistentOrder if the order is invalid
         */
        private static void validateOrderToUse(Order orderToUse) throws NonExistentOrder {
            if (orderToUse == null) {
                throw new NonExistentOrder();
            }
        }

        /**
         * Validates the product for the order
         *
         * @param productToUse product to be used
         * @throws NonExistentOrder if the order is invalid
         */
        private static void validateProductToUse(Product productToUse) throws NonExistentProduct {
            if (productToUse == null) {
                throw new IllegalArgumentException("Invalid product for order creation");
            }
        }

        /**
         * Validates quantity of the ordered product
         *
         * @param quantityToUse quantity to be used
         * @throws InvalidQuantity if the quantity is less than one
         */
        private static void validateQuantityToUse(int quantityToUse) throws InvalidQuantity {
            if (quantityToUse < 1) {
                throw new InvalidQuantity(String.valueOf(quantityToUse));
            }
        }

        /**
         * Order product builder, accepts parameters and validates them
         *
         * @param order    order of the line
         * @param product  product
         * @param quantity quantity
         * @return Order product built, if parameters are valid; empty on error
         */
        public static Optional<OrderProduct> buildOrderProduct(Order order, Product product, int quantity) {
            try {
                validateOrderToUse(order);
                validateProductToUse(product);
                validateQuantityToUse(quantity);
                return Optional.of(new OrderProduct(order, product, quantity));
            } catch (IllegalArgumentException iae) {
                return Optional.empty();
            }
        }
    }
}
