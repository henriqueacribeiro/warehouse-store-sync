namespace StoreApp.Models.Configuration
{
    public class FromWarehouseSettings
    {
        public string RoutingKey { get; set; } = string.Empty;
        public string OrderCancelArgument { get; set; } = string.Empty;
        public string OrderStatusUpdateArgument { get; set; } = string.Empty;

    }
}
