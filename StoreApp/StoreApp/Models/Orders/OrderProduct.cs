using StoreApp.Models.Products;

namespace StoreApp.Models.Orders
{
    public class OrderProduct
    {
        public Product Product { get; set; }

        public int Quantity { get; set; }

        public double AppliedDiscount { get; set; }

        public double FinalPrice { get
            {
                return Quantity * Product.Price * ((100 - AppliedDiscount) / 100);
            }
        }

        public virtual Order Order { get; set; }
    }
}
