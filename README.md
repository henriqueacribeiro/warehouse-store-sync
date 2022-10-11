# Warehouse/Store Sync Example

## Description

Various stores will send orders that receives to a warehouse application, that will then validate the products existence and stores the information.

After that, the warehouse can send status changes to the store that sent the information.

## Basic flow description

A client, after registering itself in the system, can submit an order. That order is saved in the system and communicated to a warehouse using a message broker.

The warehouse, upon receiving the order, checks if all the products exists and the order is valid. If it is valid, the order is saved in the system and it is communicated to the store, via message broker, that the order was successfully received. Otherwise, it is canceled in the system and the cancellation is sent to the store.

The warehouse can internally update the order status, with that update being communicated to the store. When the store receives a status update, it checks if the status is valid and updates it. A sent order cannot be cancelled, for example.


## How was it made?

The warehouse application was built using Java 11 with Spring framework. 

The store application was built using C# along with .NET 6.

The communication between them is performed using a message broker, and it is possible to store the data in different databases. The interaction with the application can be performed using API.

## Message Broker

The message broker tested with both applications was RabbitMQ. Both applications create automatically the exchanges and queues to receive information.

The warehouse only has a direct exchange with a single routing key available, to receive orders.

The store application has a header exchange. Currently, the only argument used to differentiate is the `process`, used to split the received messages between status update or cancellation.

#### Configuring communication with RabbitMQ
The store application has the RabbitMQ URL, with the user and password, in the `appsettings.json` file, in the `URL` key inside the `Messaging` object.

The warehouse application can also be easily configured using a RabbitMQ URL. In the `application.properties`, use the property `spring.rabbitmq.addresses`.

## API
Both application contain a Swagger page, with information about the API functionalities.

While executing the warehouse app, the page is available in the `/doc.html` path. The store app API documentation is accessible through the `swagger/index.html` path.

Two Postman collections are stored in the project, one for each application, with request examples.

## Other configurations

The Warehouse application requires the following configurations, in the base of the `application.properties`:

```
storeapp.warehouse.code=wh1 => code of the warehouse, to be used on queues
storeapp.messaging.status.communicated=2 => status to be communicated to store when order is received successfully
storeapp.messaging.status.sent=3 => status to be communicated to store when order is sent
storeapp.messaging.status.canceled=4 => status to be communicated to store when order is canceled
storeapp.messaging.store.fromstore.de=ws.comm => => Exchange to be read by the current warehouse
storeapp.messaging.store.fromstore.rk.order.receive=s2w.order.creation => Routing key of the warehouse exchange to create orders
storeapp.messaging.store.he.base=w2s => Store header exchange prefix. Usually followed by a dot and the store name
storeapp.messaging.store.tostore.order.cancel=cancel => Argument of a queue in the store header exchange to receive cancellations
storeapp.messaging.store.tostore.order.update=statusupdate => Argument of a queue in the store header exchange to receive order status updates
```

### Store

The Store application requires the following configuration, in the root of the `appsetting.json`:

```
"Messaging": {
    "StoreCode": "StoreOne", => code of the store, for the queues
    "URL": "amqp://guest:guest@localhost:5672", => URI to access RabbitMQ
    "FromStoreDirectExchange": "ws.comm", => Exchange to be read by the warehouse
    "ToStoreExchangeName": "w2s.StoreOne", => Exchange to be read by the current store
    "ToWarehouse": {
      "OrderCreationQuery": "s2w.order.creation" => Routing key of the warehouse exchange to create orders
    },
    "FromWarehouse": {
      "OrderCancelArgument": "cancel", => Argument of a queue in the store header exchange to receive cancellations
      "OrderStatusUpdateArgument": "statusupdate" => Argument of a queue in the store header exchange to receive order status updates
    }
  }
```