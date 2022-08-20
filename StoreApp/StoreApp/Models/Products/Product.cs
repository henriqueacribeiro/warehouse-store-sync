using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Products
{
    public class Product
    {
        [Key]
        public string Code { get; set; }

        [Required]
        public string Name { get; set; }

        [Required]
        public string Description { get; set; }

        [Required]
        public string Price { get; set; }

        public Double MaximumDiscount { get; set; }
    }
}
