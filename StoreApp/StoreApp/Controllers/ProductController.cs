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
            var products = await productService.GetAllDtoFormat();
            return Ok(products);
        }

        [HttpGet("{code}")]
        public async Task<ActionResult<Product>> GetByCode(string code)
        {
            try
            {
                var product = await productService.FindByCodeDtoFormat(code);
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

        [HttpPost]
        public async Task<ActionResult<Product>> Add(ProductDto product)
        {
            try
            {
                await productService.Add(product);
                return Ok(product);
            }
            catch (ArgumentException ae)
            {
                return BadRequest(ae.Message);
            }
            catch (Exception)
            {
                return StatusCode(500, "Error processing inforamtion");
            }
        }
    }
}
