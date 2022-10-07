namespace StoreApp.Models.Configuration
{
    public class ToWarehouseSettings
    {
        public string RoutingKey { get; set; } = string.Empty;
        public string OrderCreationQuery { get; set; } = string.Empty;
    }
}
