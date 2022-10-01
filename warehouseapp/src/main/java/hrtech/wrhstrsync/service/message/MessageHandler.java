package hrtech.wrhstrsync.service.message;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageHandler {

    @Value("${storeapp.messaging.store.de}")
    private String storeDirectExchangeName;

    @Value("${storeapp.messaging.store.fromstore}")
    private String fromStoreBindName;

    @Value("${storeapp.messaging.store.fromstore.order.receive}")
    private String storeOrderReceiverQueueName;

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Bean
    public DirectExchange storeDirectExchange() {
        return new DirectExchange(storeDirectExchangeName);
    }

    @Bean
    public Queue orderReceiverQueue() {
        return new Queue(storeOrderReceiverQueueName);
    }

    @Bean
    public Binding orderReceiverFromStoreBind(DirectExchange storeDirectExchange, Queue orderReceiverQueue) {
        return BindingBuilder.bind(orderReceiverQueue).to(storeDirectExchange).with(fromStoreBindName);
    }
}
