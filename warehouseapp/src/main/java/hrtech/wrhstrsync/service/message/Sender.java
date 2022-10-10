package hrtech.wrhstrsync.service.message;

import hrtech.wrhstrsync.model.messaging.OrderCancelCommunication;
import hrtech.wrhstrsync.model.messaging.OrderStatusCommunication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Sender {

    @Autowired
    private RabbitTemplate template;

    @Value("${storeapp.messaging.store.he.base}")
    private String toStoreHeaderExchangeName;

    @Value("${storeapp.messaging.store.tostore.order.cancel}")
    private String orderCancellationQueueName;

    @Value("${storeapp.messaging.store.tostore.order.update}")
    private String orderUpdateQueueName;

    public void sendOrderStatusChange(boolean success, String storeCode, String externalOrderID, int statusID) {
        OrderStatusCommunication osc = new OrderStatusCommunication(success, storeCode, externalOrderID, statusID);
        template.convertAndSend(toStoreHeaderExchangeName + "." + storeCode, "", osc.toJSON().toString(), m -> {
            m.getMessageProperties().getHeaders().put("process", orderUpdateQueueName);
            return m;
        });
    }

    public void sendOrderCancellation(String storeCode, String externalOrderID) {
        OrderCancelCommunication occ = new OrderCancelCommunication(storeCode, externalOrderID);
        template.convertAndSend(toStoreHeaderExchangeName + "." + storeCode, "", occ.toJSON().toString(), m -> {
            m.getMessageProperties().getHeaders().put("process", orderCancellationQueueName);
            return m;
        });
    }
}
