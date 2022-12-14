using StoreApp.DTOs.Orders;
using StoreApp.Models.Products;
using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Orders
{
    public class OrderProduct : IModel<OrderProductDto>
    {
        [Key]
        public Guid Id { get; set; }

        public Product Product { get; set; }

        public int Quantity { get; set; }

        public double AppliedDiscount { get; set; }

        public double FinalPrice
        {
            get
            {
                return Quantity * Product.Price * ((100 - AppliedDiscount) / 100);
            }
        }

        public virtual Order Order { get; set; }

        public OrderProductDto ToDto()
        {
            return new OrderProductDto
            {
                AppliedDiscount = AppliedDiscount,
                FinalPrice = FinalPrice,
                Product = Product.ToDto(),
                Quantity = Quantity
            };
        }
    }
}
