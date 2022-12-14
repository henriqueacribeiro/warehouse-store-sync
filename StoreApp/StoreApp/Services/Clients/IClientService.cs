using StoreApp.DTOs.Clients;
using StoreApp.DTOs.Products;
using StoreApp.Models.Clients;

namespace StoreApp.Services.Clients
{
    public interface IClientService : IService<Client, ClientDto>
    {
        /// <summary>
        /// Method that checks if a client nif is being used
        /// </summary>
        /// <param name="nif">nif</param>
        /// <returns>true if it exists; false otherwise</returns>
        Task<bool> CheckIfNifIsBeingUsed(string nif);

        /// <summary>
        /// Returns a client information, given its NIF
        /// </summary>
        /// <param name="nif">nif to search</param>
        /// <returns>client, if found</returns>
        public Task<Client> FindByNif(string nif);

        /// <summary>
        /// Returns a client information, given its NIF, in transport format
        /// </summary>
        /// <param name="nif">nif to search</param>
        /// <returns>client, if found, in transport (DTO) format</returns>
        public Task<ClientDto> FindByNifDtoFormat(string nif);

        /// <summary>
        /// Create a new client, given its information. The NIF must be numeric and have 9 digits, the other fields except the email can't be empty
        /// </summary>
        /// <param name="client"></param>
        /// <returns></returns>
        public Task Register(ClientDto client);

        /// <summary>
        /// Fetches all stored clients
        /// </summary>
        /// <returns>All clients stored in database</returns>
        Task<ICollection<Client>> GetAll();

        /// <summary>
        /// Fetches all stored clients, in transport format
        /// </summary>
        /// <returns>All clients stored in database, in transport (DTO) format</returns>
        Task<ICollection<ClientDto>> GetAllDtoFormat();


    }
}
