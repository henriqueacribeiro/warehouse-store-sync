using StoreApp.DTOs.Products;
using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Products
{
    public class Product : IModel<ProductDto>
    {
        [Key]
        public string Code { get; set; }

        [Required]
        public string Name { get; set; }

        public string Description { get; set; } = string.Empty;

        [Required]
        public double Price { get; set; }

        public Double MaximumDiscount { get; set; } = 100;

        public ProductDto ToDto()
        {
            return new ProductDto
            {
                Code = Code,
                Name = Name,
                Description = Description,
                Price = Price,
                MaximumDiscount = MaximumDiscount
            };
        }
    }
}
