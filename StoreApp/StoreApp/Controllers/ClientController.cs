using Microsoft.AspNetCore.Mvc;
using StoreApp.DTOs.Clients;
using StoreApp.Services.Clients;

namespace StoreApp.Controllers
{
    [Route("client")]
    [ApiController]
    public class ClientController : ControllerBase
    {
        private readonly IClientService clientService;

        public ClientController(IClientService clientService)
        {
            this.clientService = clientService;
        }

        [HttpGet]
        public async Task<ActionResult<ICollection<ClientDto>>> GetAll()
        {
            var clients = await clientService.GetAll();
            return Ok(clients);
        }

        [HttpGet("{nif}")]
        public async Task<ActionResult<ClientDto>> GetByNif(string nif)
        {
            try
            {
                var client = await clientService.FindByNif(nif);
                return Ok(client);
            }
            catch (KeyNotFoundException)
            {
                return NotFound("Code not found");
            }
            catch (Exception)
            {
                return StatusCode(500, "Error processing information");
            }
        }

        [HttpPost]
        public async Task<ActionResult<ClientDto>> Add(ClientDto client)
        {
            try
            {
                await clientService.Register(client);
                return Ok(client);
            }
            catch (ArgumentException ae)
            {
                return BadRequest(ae.Message);
            }
            catch (Exception e)
            {
                return StatusCode(500, "Error processing inforamtion");
            }
        }
    }
}
