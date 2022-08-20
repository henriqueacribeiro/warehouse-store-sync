using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Products;

namespace StoreApp.Data.Repositories.ProductRepository
{
    public class ProductRepository : BaseRepository<Product, string>, IProductRepository
    {
        public ProductRepository(StoreContext context) : base(context)
        {
        }
    }
}
