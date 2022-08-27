using StoreApp.DTOs.Clients;
using StoreApp.Models.Orders;

namespace StoreApp.DTOs.Orders
{
    public class OrderDto
    {
        public ClientDto Client { get; set; }

        public OrderStatus Status { get; set; }

        public IList<OrderProductDto> Products { get; set; } = new List<OrderProductDto>();

        public double FinalPrice;
    }
}
