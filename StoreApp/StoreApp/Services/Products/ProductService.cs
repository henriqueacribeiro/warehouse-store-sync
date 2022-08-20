using StoreApp.Data.Repositories.ProductRepository;
using StoreApp.DTOs.Products;
using StoreApp.Models.Products;

namespace StoreApp.Services.Products
{
    public class ProductService : IProductService
    {

        private readonly IProductRepository repository;

        public ProductService(IProductRepository repository)
        {
            this.repository = repository;
        }

        public async Task<ProductDto> FindByCode(string code)
        {
            var product = await repository.GetByIdAsync(code);
            if (product == null)
            {
                throw new KeyNotFoundException("Product with code " + code + " not found");
            }
            return ToDto(product);
        }

        public async Task<IList<ProductDto>> GetAll()
        {
            var products = await repository.GetAllAsync();
            
            var productDtos = new List<ProductDto>();
            foreach (var product in products)
            {
                productDtos.Add(ToDto(product));
            };

            return productDtos;
        }

        public ProductDto ToDto(Product entity)
        {
            return new ProductDto
            {
                Code = entity.Code,
                Name = entity.Name,
                Description = entity.Description,
                Price = entity.Price,
                MaximumDiscount = entity.MaximumDiscount
            };
        }
    }
}
