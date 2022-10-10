using Microsoft.Extensions.Options;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using StoreApp.Models.Configuration;
using StoreApp.Models.Orders;
using StoreApp.Services.Orders;
using System.Text;
using System.Text.Json.Nodes;

namespace StoreApp.Services.Messages
{
    public class Receiver : BackgroundService
    {
        private readonly IServiceProvider serviceProvider;
        private readonly MessagingSettings _messagingSettings;

        private readonly IConnectionFactory _connectionFactory;
        private readonly IConnection _connection;
        private readonly IModel model;

        public Receiver(IOptions<MessagingSettings> messagingSettings, IServiceProvider sp)
        {
            serviceProvider = sp;
            _messagingSettings = messagingSettings.Value;

            _connectionFactory = new ConnectionFactory() { Uri = new Uri(_messagingSettings.URL) };
            _connection = _connectionFactory.CreateConnection();

            model = _connection.CreateModel();
            model.ExchangeDeclare(_messagingSettings.ToStoreExchangeName, ExchangeType.Headers, true);

            var orderStatusUpdateArguments = new Dictionary<string, object>
            {
                { "process", _messagingSettings.FromWarehouse.OrderStatusUpdateArgument }
            };
            var generatedStatusUpdateQueue = model.QueueDeclare(
                queue: _messagingSettings.StoreCode + "." + _messagingSettings.FromWarehouse.OrderStatusUpdateArgument,
                durable: false,
                exclusive: false,
                autoDelete: false,
                arguments: null); ;
            model.QueueBind(generatedStatusUpdateQueue.QueueName,
                _messagingSettings.ToStoreExchangeName,
                "",
                orderStatusUpdateArguments);

            var orderCancelArguments = new Dictionary<string, object>
            {
                { "process", _messagingSettings.FromWarehouse.OrderCancelArgument }
            };
            var generatedOrderCancelQueue = model.QueueDeclare(
                queue: _messagingSettings.StoreCode + "." + _messagingSettings.FromWarehouse.OrderCancelArgument,
                durable: false,
                exclusive: false,
                autoDelete: false,
                arguments: null);
            model.QueueBind(generatedOrderCancelQueue.QueueName,
                _messagingSettings.ToStoreExchangeName,
                "",
                orderCancelArguments);
        }

        protected override Task ExecuteAsync(CancellationToken stoppingToken)
        {
            if (stoppingToken.IsCancellationRequested)
            {
                model.Dispose();
                _connection.Dispose();
                return Task.CompletedTask;
            }

            var orderStatusUpdateConsumer = new EventingBasicConsumer(model);
            orderStatusUpdateConsumer.Received += (model, ea) =>
            {
                var jsonMessage = ExtractJsonFromMessage(ea);

                var orderID = jsonMessage["order_id"].GetValue<Guid>();
                var status = jsonMessage["status"].GetValue<int>();

                Task.Run(() =>
                {
                    using var scope = serviceProvider.CreateScope();
                    var orderService = scope.ServiceProvider.GetRequiredService<IOrderService>();
                    Console.WriteLine(status);
                    orderService.UpdateOrderStatus(orderID, status);
                });
            };
            model.BasicConsume(queue: _messagingSettings.StoreCode + "." + _messagingSettings.FromWarehouse.OrderStatusUpdateArgument,
                autoAck: true,
                consumer: orderStatusUpdateConsumer);

            var orderCancelConsumer = new EventingBasicConsumer(model);
            orderCancelConsumer.Received += (model, ea) =>
            {
                var jsonMessage = ExtractJsonFromMessage(ea);
                var orderID = jsonMessage["order_id"].GetValue<Guid>();

                Task.Run(() =>
                {
                    using var scope = serviceProvider.CreateScope();
                    var orderService = scope.ServiceProvider.GetRequiredService<IOrderService>();
                    orderService.UpdateOrderStatus(orderID, (int)OrderStatus.CANCELED);
                });
            };
            model.BasicConsume(queue: _messagingSettings.StoreCode + "." + _messagingSettings.FromWarehouse.OrderCancelArgument,
                autoAck: true,
                consumer: orderCancelConsumer);

            return Task.CompletedTask;
        }

        private static JsonNode ExtractJsonFromMessage(BasicDeliverEventArgs eventInfo)
        {
            var body = eventInfo.Body.ToArray();
            var message = Encoding.UTF8.GetString(body);
            return JsonNode.Parse(message);
        }
    }
}
