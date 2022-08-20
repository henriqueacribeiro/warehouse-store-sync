using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StoreApp.DTOs.Products;
using StoreApp.Models.Products;
using StoreApp.Services.Products;

namespace StoreApp.Controllers
{
    [Route("product")]
    [ApiController]
    public class ProductController : ControllerBase
    {
        private readonly IProductService productService;

        public ProductController(IProductService productService)
        {
            this.productService = productService;
        }

        [HttpGet]
        public async Task<ActionResult<ICollection<ProductDto>>> GetProducts()
        {
            var products = await productService.GetAll();
            return Ok(products);
        }

        [HttpGet("{code}")]
        public async Task<ActionResult<Product>> GetByCode(string code)
        {
            try
            {
                var product = await productService.FindByCode(code);
                return Ok(product);
            }
            catch (KeyNotFoundException)
            {
                return NotFound("Code not found");
            }
            catch (Exception)
            {
                return StatusCode(500, "Error processing inforamtion");
            }
        }
    }
}
