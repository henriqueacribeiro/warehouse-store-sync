package hrtech.wrhstrsync.service.message;

import hrtech.wrhstrsync.exception.StoreWarehouseSynchronizationError;
import hrtech.wrhstrsync.model.global.Response;
import hrtech.wrhstrsync.model.order.Order;
import hrtech.wrhstrsync.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


public class Receiver {

    private static final Logger logger = LogManager.getLogger(Receiver.class);

    private OrderService orderService;

    private Sender sender;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @RabbitListener(queues = "${storeapp.messaging.store.fromstore.order.receive}")
    public void orderReceiver(String in) {
        try {
            JSONObject receivedInfo = new JSONObject(in);
            Response<Order> generatedOrderInfo = orderService.saveNewOrder(receivedInfo);

            if (generatedOrderInfo.isSuccess()) {
                sender.sendOrderStatusChange(true, generatedOrderInfo.getObjectToReturn().getStoreCode(),
                        generatedOrderInfo.getObjectToReturn().getExternalID(), generatedOrderInfo.getObjectToReturn().getOrderStatus().ordinal());
            } else {
                sender.sendOrderCancellation(generatedOrderInfo.getObjectToReturn().getStoreCode(), generatedOrderInfo.getObjectToReturn().getExternalID());
            }
        } catch (JSONException jsonException) {
            throw new StoreWarehouseSynchronizationError(StoreWarehouseSynchronizationError.SyncErrorType.ERROR_ON_STORE_TO_WAREHOUSE_MESSAGE);
        } catch (StoreWarehouseSynchronizationError er) {
            logger.error(er.getMessage(), er);
        }
    }
}
