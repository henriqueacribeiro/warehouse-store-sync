using Microsoft.Extensions.Options;
using RabbitMQ.Client;
using StoreApp.Models.Configuration;
using StoreApp.Models.Orders;
using System.Text;
using System.Text.Json.Nodes;

namespace StoreApp.Services.Messages
{
    public class ToWarehouseCommunicationByMessageBroker : IToWarehouseCommunication
    {
        private readonly MessagingSettings _messagingSettings;

        private readonly IConnectionFactory _connectionFactory;
        private readonly IConnection _connection;
        private readonly IModel model;

        public ToWarehouseCommunicationByMessageBroker(IOptions<MessagingSettings> messagingSettings)
        {
            _messagingSettings = messagingSettings.Value;

            _connectionFactory = new ConnectionFactory() { Uri = new Uri(_messagingSettings.URL) };
            _connection = _connectionFactory.CreateConnection();
            model = _connection.CreateModel();
        }

        public void SendOrderToWarehouse(Order order)
        {
            model.BasicPublish(_messagingSettings.FromStoreDirectExchange, 
                _messagingSettings.ToWarehouse.OrderCreationQuery, null,
                Encoding.UTF8.GetBytes(ConvertOrderToWarehouseSending(order).ToJsonString()));
        }

        private JsonObject ConvertOrderToWarehouseSending(Order order)
        {
            var products = new JsonArray();
            foreach (var product in order.Products)
            {
                products.Add(new JsonObject
                {
                    { "quantity", product.Quantity },
                    { "code", product.Product.Code }
                });
            }

            return new JsonObject
            {
                { "external_code", order.Id },
                { "store_code", _messagingSettings.StoreCode },
                { "products", products }
            };
        }
    }
}
