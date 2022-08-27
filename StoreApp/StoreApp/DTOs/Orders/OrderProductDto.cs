using StoreApp.DTOs.Products;

namespace StoreApp.DTOs.Orders
{
    public class OrderProductDto
    {
        public ProductDto Product { get; set; }

        public int Quantity { get; set; }

        public double AppliedDiscount { get; set; }

        public double FinalPrice { get; set; }
    }
}
