using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Product
{
    public class Product
    {
        [Key]
        public String Code { get; set; }

        [Required] 
        public String Name { get; set; }

        [Required]
        public String Description { get; set; }

        [Required]
        public String Price { get; set; }

        public Double MaximumDiscount { get; set; }
    }
}
