
using StoreApp.DTOs.Products;
using StoreApp.Models.Products;

namespace StoreApp.Services.Products
{
    public interface IProductService : IService<Product, ProductDto>
    {
        /// <summary>
        /// Method that checks if a product code is being used
        /// </summary>
        /// <param name="code">product code</param>
        /// <returns>true if it exists; false otherwise</returns>
        Task<bool> CheckIfCodeExists(string code);

        /// <summary>
        /// Fetches the product by its code
        /// </summary>
        /// <param name="code">code to fetch</param>
        /// <returns>Product if found</returns>
        Task<Product> FindByCode(string code);

        /// <summary>
        /// Fetches the product by its code in transport format
        /// </summary>
        /// <param name="code">code to fetch</param>
        /// <returns>Product DTO if found</returns>
        Task<ProductDto> FindByCodeDtoFormat(string code);

        /// <summary>
        /// Fetches all stored products
        /// </summary>
        /// <returns>All products stored in database</returns>
        Task<ICollection<Product>> GetAll();

        /// <summary>
        /// Fetches all stored products in transport format
        /// </summary>
        /// <returns>All products stored in database in transport (DTO) format</returns>
        Task<ICollection<ProductDto>> GetAllDtoFormat();

        /// <summary>
        /// Saves a product in the database, after checking all parameters. The code, name and price are obligatory; maximum discount and description optional
        /// </summary>
        /// <param name="product">Product in transport format. Check <c>ProudctDto</c></param>
        /// <returns></returns>
        Task Add(ProductDto product);
    }
}
