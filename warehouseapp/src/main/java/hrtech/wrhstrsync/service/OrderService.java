package hrtech.wrhstrsync.service;

import hrtech.wrhstrsync.exception.NonExistentOrder;
import hrtech.wrhstrsync.exception.NonExistentProduct;
import hrtech.wrhstrsync.model.global.Response;
import hrtech.wrhstrsync.model.order.Order;
import hrtech.wrhstrsync.model.order.OrderStatus;
import hrtech.wrhstrsync.model.product.Product;
import hrtech.wrhstrsync.repository.IOrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for Order operations
 */
@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private ProductService productService;
    private IOrderRepository orderRepository;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderRepository(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Method that creates an order from a JSON object passed by parameter
     *
     * @param orderDefinition order info, on JSON format
     * @return Response model with order created on success; empty otherwise
     */
    public Response<Order> saveNewOrder(JSONObject orderDefinition) {
        try {
            String externalCode = orderDefinition.optString("external_code");
            if (externalCode.isBlank()) {
                throw new IllegalArgumentException("Invalid or nonexistent external code");
            }

            String storeCode = orderDefinition.optString("store_code");
            if (storeCode.isBlank()) {
                throw new IllegalArgumentException("Invalid or nonexistent store code");
            }

            Order.OrderFactory orderFactory = new Order.OrderFactory(externalCode, storeCode);

            JSONArray productArray = orderDefinition.optJSONArray("products");
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject currentProductDefinition = productArray.optJSONObject(i);

                int quantity = currentProductDefinition.optInt("quantity", 0);
                String code = currentProductDefinition.optString("code", "");
                Optional<Product> currentProduct = productService.getByCode(code);
                if (currentProduct.isEmpty()) {
                    throw new NonExistentProduct(code);
                }

                if (!orderFactory.addProduct(currentProduct.get(), quantity)) {
                    throw new IllegalArgumentException("Failure while adding product " + code);
                }
            }

            Optional<Order> order = orderFactory.buildOrder();
            if (order.isPresent()) {
                Order savedOrder = orderRepository.save(order.get());
                return new Response<>(Response.ResponseValue.SUCCESS, "", savedOrder);
            }
        } catch (IllegalArgumentException nep) {
            logger.warn(nep.getLocalizedMessage());
            return new Response<>(Response.ResponseValue.CLIENT_FAIL, nep.getLocalizedMessage(), null);
        }
        return new Response<>(Response.ResponseValue.SERVER_FAIL, "Unknown error", null);
    }

    /**
     * Method that retrieves an order by its id
     *
     * @param orderID id of the order
     * @return Optional with the order, if found; empty otherwise
     */
    public Optional<Order> getOrderByID(long orderID) {
        return orderRepository.findById(orderID);
    }

    /**
     * Method that updates an order status, given the new status and the order
     *
     * @param jsonObject object with info
     * @return Response object with info about the update
     */
    public Response<Order> updateOrderStatus(JSONObject jsonObject) {
        long orderID = jsonObject.optLong("order_id", -1);
        int orderStatusID = jsonObject.optInt("status", -1);
        return updateOrderStatus(orderID, orderStatusID);
    }

    /**
     * Method that updates an order status, given the new status and the order
     *
     * @param orderID       id of the order to update
     * @param orderStatusID id of the new order status
     * @return Response object with info about the update
     */
    private Response<Order> updateOrderStatus(long orderID, int orderStatusID) {
        try {
            Optional<OrderStatus> newStatus = OrderStatus.getByID(orderStatusID);
            if (newStatus.isEmpty()) {
                throw new IllegalArgumentException("Invalid new status");
            }

            Optional<Order> order = orderRepository.findById(orderID);
            if (order.isEmpty()) {
                throw new NonExistentOrder(String.valueOf(orderID));
            }

            order.get().updateStatus(newStatus.get());
            Order updatedOrder = orderRepository.save(order.get());
            if (!updatedOrder.getOrderStatus().equals(newStatus.get())) {
                logger.error("Error while saving order status change on database");
                return new Response<>(Response.ResponseValue.SERVER_FAIL, "Unknown error", null);
            }
            return new Response<>(Response.ResponseValue.SUCCESS, "", updatedOrder);
        } catch (IllegalArgumentException iae) {
            logger.warn(iae.getLocalizedMessage());
            return new Response<>(Response.ResponseValue.CLIENT_FAIL, iae.getLocalizedMessage(), null);
        }
    }
}
