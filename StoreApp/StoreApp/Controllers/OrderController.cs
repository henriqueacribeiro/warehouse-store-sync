using Microsoft.AspNetCore.Mvc;
using StoreApp.DTOs.Orders;
using StoreApp.Services.Orders;

namespace StoreApp.Controllers
{
    [Route("order")]
    [ApiController]
    public class OrderController : Controller
    {
        private readonly IOrderService orderService;

        public OrderController(IOrderService orderService)
        {
            this.orderService = orderService ?? throw new ArgumentNullException(nameof(orderService));
        }

        [HttpGet("byclient/{nif}")]
        public async Task<ActionResult<ICollection<OrderDto>>> GetByClient(string nif)
        {
            return Ok(await orderService.GetByClientDtoFormat(nif));
        }

        [HttpPost]
        public async Task<ActionResult> Register(OrderDto order)
        {
            try
            {
                await orderService.Register(order);
                return Ok(order);
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
