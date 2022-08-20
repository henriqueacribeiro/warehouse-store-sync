using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Clients;
using Microsoft.EntityFrameworkCore;

namespace StoreApp.Data.Repositories.ClientRepository
{
    public class ClientRepository : BaseRepository<Client, Guid>, IClientRepository
    {
        public ClientRepository(StoreContext context) : base(context)
        {
        }

        public async Task<Client> GetByNif(string nif)
        {
            return await _context.Clients.FirstOrDefaultAsync(client => client.Nif.Equals(nif));
        }
    }
}
