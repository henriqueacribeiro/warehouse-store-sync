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
import org.springframework.beans.factory.annotation.Value;


public class Receiver {

    private static final Logger logger = LogManager.getLogger(Receiver.class);

    private OrderService orderService;

    private Sender sender;

    @Value("${storeapp.messaging.status.communicated}")
    private int storeOrderStatusCommunicated;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @RabbitListener(queues = "#{orderReceiverQueue}")
    public void orderReceiver(String in) {
        try {
            JSONObject receivedInfo = new JSONObject(in);
            Response<Order> generatedOrderInfo = orderService.saveNewOrder(receivedInfo);

            if (generatedOrderInfo.isSuccess()) {
                sender.sendOrderStatusChange(true, generatedOrderInfo.getObjectToReturn().getStoreCode(),
                        generatedOrderInfo.getObjectToReturn().getExternalID(), storeOrderStatusCommunicated);
            } else {
                sender.sendOrderCancellation(receivedInfo.optString("store_code"), receivedInfo.optString("external_code"));
            }
        } catch (JSONException jsonException) {
            throw new StoreWarehouseSynchronizationError(StoreWarehouseSynchronizationError.SyncErrorType.ERROR_ON_STORE_TO_WAREHOUSE_MESSAGE);
        } catch (StoreWarehouseSynchronizationError er) {
            logger.error(er.getMessage(), er);
        }
    }
}
