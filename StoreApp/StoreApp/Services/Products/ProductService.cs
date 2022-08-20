using StoreApp.Data.Repositories.ProductRepository;
using StoreApp.DTOs.Products;
using StoreApp.Models.Products;
using System.Text.RegularExpressions;

namespace StoreApp.Services.Products
{
    public class ProductService : IProductService
    {

        private readonly IProductRepository repository;
        private readonly string codeVerification = "^([AB])(\\d){5}$";

        public ProductService(IProductRepository repository)
        {
            this.repository = repository;
        }

        public async Task<bool> CheckIfCodeExists(string code)
        {
            return await repository.GetByIdAsync(code) != null;
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

        public async Task Add(ProductDto product)
        {

            if (product.Name.Trim().Length < 0)
            {
                throw new ArgumentException("Invalid product name");
            }

            var verification = new Regex(codeVerification);
            if (!verification.Match(product.Code).Success)
            {
                throw new ArgumentException("Invalid product code. The format must started with A or B, followed by 5 numbers");
            }

            if (product.Name.Trim().Length < 0)
            {
                throw new ArgumentException("Invalid product name");
            }
            if (product.MaximumDiscount < 0.0 || product.MaximumDiscount > 100.0)
            {
                throw new ArgumentException("Invalid maximum discount for product (" + product.MaximumDiscount + ")");
            }
            if (product.Price < 0.0)
            {
                throw new ArgumentException("Invalid maximum discount for product (" + product.Price + ")");
            }

            await repository.AddAsync(ToModel(product));
            await repository.SaveAsync();
        }

        public Product ToModel(ProductDto transport)
        {
            return new Product
            {
                Code = transport.Code,
                Name = transport.Name,
                Description = transport.Description,
                Price = transport.Price,
                MaximumDiscount = transport.MaximumDiscount
            };
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
