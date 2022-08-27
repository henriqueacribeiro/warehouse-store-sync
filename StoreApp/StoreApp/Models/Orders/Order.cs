using StoreApp.DTOs.Orders;
using StoreApp.Models.Clients;
using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Orders
{
    public class Order : IModel<OrderDto>
    {
        [Key]
        public Guid Id { get; set; }

        public Client Client { get; set; }

        public ICollection<OrderProduct> Products { get; set; } = new List<OrderProduct>();

        public OrderStatus Status { get; set; }

        public double FinalPrice
        {
            get
            {
                return this.Products.Sum(x => x.FinalPrice);
            }
        }

        public OrderDto ToDto()
        {
            return new OrderDto
            {
                Status = Status,
                Client = Client.ToDto(),
                FinalPrice = FinalPrice,
                Products = Products.Select(product => product.ToDto()).ToList()
            };
        }
    }
}
