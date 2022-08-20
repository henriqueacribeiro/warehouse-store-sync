using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Clients;

namespace StoreApp.Data.Repositories.ClientRepository
{
    public interface IClientRepository : IBaseRepository<Client, Guid>
    {
        /// <summary>
        /// Fetches client info by NIF
        /// </summary>
        /// <param name="nif">nif to search</param>
        /// <returns>client, if found</returns>
        Task<Client> GetByNif(string nif);
    }
}
