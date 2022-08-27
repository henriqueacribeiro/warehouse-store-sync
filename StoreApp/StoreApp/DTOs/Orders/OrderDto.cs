using StoreApp.DTOs.Clients;

namespace StoreApp.DTOs.Orders
{
    public class OrderDto
    {
        public ClientDto Client { get; set; }

        public IList<OrderProductDto> Products { get; set; } = new List<OrderProductDto>();

        public double FinalPrice;
    }
}
