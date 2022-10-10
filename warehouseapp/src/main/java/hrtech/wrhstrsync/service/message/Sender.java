package hrtech.wrhstrsync.service.message;

import hrtech.wrhstrsync.model.messaging.OrderCancelCommunication;
import hrtech.wrhstrsync.model.messaging.OrderStatusCommunication;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
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
        Message message = MessageBuilder.withBody(SerializationUtils.serialize(osc.toJSON().toString()))
                .setHeader("process", orderUpdateQueueName)
                .build();
        template.convertAndSend(toStoreHeaderExchangeName, message);
    }

    public void sendOrderCancellation(String storeCode, String externalOrderID) {
        OrderCancelCommunication occ = new OrderCancelCommunication(storeCode, externalOrderID);
        Message message = MessageBuilder.withBody(SerializationUtils.serialize(occ.toJSON().toString()))
                .setHeader("process", orderCancellationQueueName)
                .build();
        template.convertAndSend(toStoreHeaderExchangeName + "storeCode", message);
    }
}
