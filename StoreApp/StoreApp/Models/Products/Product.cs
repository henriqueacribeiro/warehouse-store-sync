using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Products
{
    public class Product
    {
        [Key]
        public string Code { get; set; }

        [Required]
        public string Name { get; set; }

        public string Description { get; set; } = string.Empty;

        [Required]
        public double Price { get; set; }

        public Double MaximumDiscount { get; set; } = 100;
    }
}
