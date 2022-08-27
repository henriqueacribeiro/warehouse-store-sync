using StoreApp.DTOs.Products;

namespace StoreApp.DTOs.Orders
{
    public class OrderProductDto
    {
        public ProductDto Product { get; set; }

        public int Quantity { get; set; } = 0;

        public double AppliedDiscount { get; set; } = 0.0;

        public double FinalPrice { get; set; } = 0.0;
    }
}
