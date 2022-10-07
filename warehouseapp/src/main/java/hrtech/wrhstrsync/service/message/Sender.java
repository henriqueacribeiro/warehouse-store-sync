package hrtech.wrhstrsync.service.message;

import hrtech.wrhstrsync.model.messaging.OrderCancelCommunication;
import hrtech.wrhstrsync.model.messaging.OrderStatusCommunication;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Sender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange storeDirectExchange;

    @Value("${storeapp.messaging.store.tostore}")
    private String toStoreBindName;

    @Value("${storeapp.messaging.store.tostore.order.cancel}")
    private String orderCancellationQueueName;

    @Value("${storeapp.messaging.store.tostore.order.update}")
    private String orderUpdateQueueName;

    public void sendOrderStatusChange(boolean success, String storeCode, String externalOrderID, int statusID) {
        OrderStatusCommunication osc = new OrderStatusCommunication(success, storeCode, externalOrderID, statusID);
        template.convertAndSend(storeDirectExchange.getName(), toStoreBindName, osc.toJSON().toString(), m -> {
            m.getMessageProperties().getHeaders().put("process", orderUpdateQueueName);
            m.getMessageProperties().getHeaders().put("store", storeCode);
            return m;
        });
    }

    public void sendOrderCancellation(String storeCode, String externalOrderID) {
        OrderCancelCommunication occ = new OrderCancelCommunication(storeCode, externalOrderID);
        template.convertAndSend(storeDirectExchange.getName(), toStoreBindName, occ.toJSON().toString(), m -> {
            m.getMessageProperties().getHeaders().put("process", orderCancellationQueueName);
            m.getMessageProperties().getHeaders().put("store", storeCode);
            return m;
        });
    }
}
