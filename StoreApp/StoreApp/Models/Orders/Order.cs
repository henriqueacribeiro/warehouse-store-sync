using StoreApp.Models.Clients;
using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Orders
{
    public class Order
    {
        [Key]
        public Guid Id { get; set; }

        public Client Client { get; set; }

        public ICollection<OrderProduct> Products { get; set; } = new List<OrderProduct>();

        public double FinalPrice
        {
            get
            {
                return this.Products.Sum(x => x.FinalPrice);
            }
        }
    }
}
