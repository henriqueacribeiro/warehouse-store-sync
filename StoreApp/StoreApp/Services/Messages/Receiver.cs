using Microsoft.Extensions.Options;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using StoreApp.Models.Configuration;
using StoreApp.Services.Orders;
using System.Text;

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
            model.ExchangeDeclare(_messagingSettings.StoreDirectExchange, ExchangeType.Direct, true);

            IDictionary<string, object> orderStatusUpdateArguments = new Dictionary<string, object>();
            orderStatusUpdateArguments.Add("process", _messagingSettings.FromWarehouse.OrderStatusUpdateArgument);
            orderStatusUpdateArguments.Add("store", _messagingSettings.StoreCode);
            model.QueueDeclare(
                queue: _messagingSettings.FromWarehouse.OrderStatusUpdateArgument,
                durable: false,
                exclusive: false,
                autoDelete: false,
                arguments: null);
            model.QueueBind(_messagingSettings.FromWarehouse.OrderStatusUpdateArgument,
                _messagingSettings.StoreDirectExchange,
                _messagingSettings.FromWarehouse.RoutingKey,
                orderStatusUpdateArguments);

            IDictionary<string, object> orderCancelArguments = new Dictionary<string, object>();
            orderCancelArguments.Add("process", _messagingSettings.FromWarehouse.OrderCancelArgument);
            orderCancelArguments.Add("store", _messagingSettings.StoreCode);
            model.QueueDeclare(
                queue: _messagingSettings.FromWarehouse.OrderCancelArgument,
                durable: false,
                exclusive: false,
                autoDelete: false,
                arguments: null);
            model.QueueBind(_messagingSettings.FromWarehouse.OrderCancelArgument,
                _messagingSettings.StoreDirectExchange,
                _messagingSettings.FromWarehouse.RoutingKey,
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
                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);

                Task.Run(() =>
                {
                    using var scope = serviceProvider.CreateScope();
                    var orderService = scope.ServiceProvider.GetRequiredService<IOrderService>();
                });
            };
            model.BasicConsume(queue: _messagingSettings.FromWarehouse.OrderStatusUpdateArgument, 
                autoAck: true, 
                consumer: orderStatusUpdateConsumer);

            var orderCancelConsumer = new EventingBasicConsumer(model);
            orderCancelConsumer.Received += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);

                Task.Run(() =>
                {
                    using var scope = serviceProvider.CreateScope();
                    var orderService = scope.ServiceProvider.GetRequiredService<IOrderService>();
                });
            };
            model.BasicConsume(queue: _messagingSettings.FromWarehouse.OrderCancelArgument,
                autoAck: true,
                consumer: orderCancelConsumer);

            return Task.CompletedTask;
        }
    }
}
