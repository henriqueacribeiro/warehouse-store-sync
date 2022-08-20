using StoreApp.Data.Repositories.ClientRepository;
using StoreApp.DTOs.Clients;
using StoreApp.Models.Clients;
using StoreApp.Models.Products;
using System.Net.Mail;
using System.Text.RegularExpressions;

namespace StoreApp.Services.Clients
{
    public class ClientService : IClientService
    {

        private readonly IClientRepository repository;
        private readonly string nifVerification = "^(\\d){9}$";

        public ClientService(IClientRepository repository)
        {
            this.repository = repository;
        }

        public async Task<bool> CheckIfNifIsBeingUsed(string nif)
        {
            return await repository.GetByNif(nif) != null;
        }

        public async Task<IList<ClientDto>> GetAll()
        {
            var clients = await repository.GetAllAsync();

            var clientsDtos = new List<ClientDto>();
            foreach (var client in clients)
            {
                clientsDtos.Add(ToDto(client));
            };

            return clientsDtos;
        }

        public async Task<ClientDto> FindByNif(string nif)
        {
            var client = await repository.GetByNif(nif);
            if (client == null)
            {
                throw new KeyNotFoundException("Client with " + nif + " not found");
            }
            return ToDto(client);
        }

        public async Task Register(ClientDto client)
        {
            Regex verification;

            if (client.FirstName.Trim().Length < 0)
            {
                throw new ArgumentException("Invalid first name");
            }
            if (client.LastName.Trim().Length < 0)
            {
                throw new ArgumentException("Invalid last name");
            }
            if (client.Email != null && client.Email.Trim().Length > 0)
            {
                if (!MailAddress.TryCreate(client.Email, out _))
                {
                    throw new ArgumentException("Invalid email");
                }
            }

            verification = new Regex(nifVerification);
            if (!verification.Match(client.Nif).Success) { 
                throw new ArgumentException("Invalid NIF");
            }
            if (await CheckIfNifIsBeingUsed(client.Nif))
            {
                throw new ArgumentException("NIF is being used");
            }

            await repository.AddAsync(ToModelWithNewId(client));
            await repository.SaveAsync();
        }

        public Client ToModelWithNewId(ClientDto transport)
        {
            return new Client
            {
                Email = transport.Email,
                FirstName = transport.FirstName,
                LastName = transport.LastName,
                Nif = transport.Nif,
                Id = Guid.NewGuid()
            };
        }

        public ClientDto ToDto(Client entity)
        {
            return new ClientDto
            {
                Email = entity.Email,
                FirstName = entity.FirstName,
                LastName = entity.LastName,
                Nif = entity.Nif
            };
        }
    }
}
