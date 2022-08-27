package hrtech.wrhstrsync.model.order;

import hrtech.wrhstrsync.exception.IllegalOrderStatusChange;
import hrtech.wrhstrsync.model.ModelDefinition;
import hrtech.wrhstrsync.model.dto.OrderDTO;
import hrtech.wrhstrsync.model.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order
 */
@Entity
@Table(name = "store_order")
public class Order implements Serializable, ModelDefinition<OrderDTO> {

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final List<OrderProduct> products;
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "status")
    private OrderStatus orderStatus;

    protected Order() {
        products = new ArrayList<>();
        orderStatus = OrderStatus.CREATED;
    }

    public long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    /**
     * Method that updates the order
     *
     * @param statusToChange status to change
     * @throws IllegalOrderStatusChange if the new status can't be set because it violates a business rule
     */
    public void updateStatus(OrderStatus statusToChange) throws IllegalOrderStatusChange {
        if (!orderStatus.getPossibleNextStatus().contains(statusToChange)) {
            throw new IllegalOrderStatusChange(orderStatus.getName(), statusToChange.getName());
        }
        orderStatus = statusToChange;
    }

    private boolean addProduct(OrderProduct product) {
        return this.products.add(product);
    }

    /**
     * Method that converts the model into DTO
     *
     * @return representation of current object in DTO format
     */
    @Override
    public OrderDTO toDTO() {
        return new OrderDTO(orderStatus.getName(), products.stream().map(OrderProduct::toDTO).collect(Collectors.toList()));
    }

    /**
     * Factory to build an Order instance
     */
    public static class OrderFactory {

        private final Order order;

        public OrderFactory() {
            order = new Order();
        }

        /**
         * Add a product to the order
         *
         * @param product  product to add to order
         * @param quantity product quantity
         * @return true on success, false otherwise
         */
        public boolean addProduct(Product product, int quantity) {
            Optional<OrderProduct> productLine = OrderProduct.OrderProductFactory.buildOrderProduct(order, product, quantity);
            if (productLine.isEmpty()) {
                return false;
            }
            return order.addProduct(productLine.get());
        }

        /**
         * Order builder, accepts the client id
         *
         * @return Order built, with products; empty on error
         */
        public Optional<Order> buildOrder() {
            if (order.getProducts().isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(order);
        }
    }
}
