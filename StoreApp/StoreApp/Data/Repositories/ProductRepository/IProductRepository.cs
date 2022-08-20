using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Products;

namespace StoreApp.Data.Repositories.ProductRepository
{
    public interface IProductRepository : IBaseRepository<Product, string>
    {
    }
}
