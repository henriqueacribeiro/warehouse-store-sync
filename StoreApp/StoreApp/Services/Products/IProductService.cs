
using StoreApp.DTOs.Products;
using StoreApp.Models.Products;

namespace StoreApp.Services.Products
{
    public interface IProductService : IService<Product, ProductDto>
    {
        /// <summary>
        /// Fetches the product by its code
        /// </summary>
        /// <param name="code">code to fetch</param>
        /// <returns>Product DTO if found</returns>
        Task<ProductDto> FindByCode(string code);

        /// <summary>
        /// Fetches all stored products
        /// </summary>
        /// <returns>All products stored in database</returns>
         Task<IList<ProductDto>> GetAll();
    }
}
