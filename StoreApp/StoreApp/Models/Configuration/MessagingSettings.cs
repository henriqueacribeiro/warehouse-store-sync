namespace StoreApp.Models.Configuration
{
    public class MessagingSettings
    {
        public string URL { get; set; } = string.Empty;
        public string FromStoreDirectExchange { get; set; } = string.Empty;
        public string ToStoreExchangeName { get; set; } = string.Empty;
        public string StoreCode { get; set; } = string.Empty;
        public ToWarehouseSettings ToWarehouse { get; set; }
        public FromWarehouseSettings FromWarehouse { get; set; }
    }
}
