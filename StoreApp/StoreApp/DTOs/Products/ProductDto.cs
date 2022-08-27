namespace StoreApp.DTOs.Products
{
    public class ProductDto
    {
        public string Code { get; set; }

        public string Name { get; set; } = "";

        public string Description { get; set; } = "";

        public double Price { get; set; } = 0.0;

        public double MaximumDiscount { get; set; } = 100.0;
    }
}
