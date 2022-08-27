using StoreApp.DTOs.Clients;
using StoreApp.Models.Orders;
using System.ComponentModel.DataAnnotations;

namespace StoreApp.Models.Clients
{
    public class Client : IModel<ClientDto>
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        public string FirstName { get; set; }

        [Required]
        public string LastName { get; set; }

        [Required]
        public string Nif { get; set; }

        [Required]
        public string Email { get; set; }

        public virtual ICollection<Order> Orders { get; set; }

        public ClientDto ToDto()
        {
            return new ClientDto
            {
                Email = Email,
                FirstName = FirstName,
                LastName = LastName,
                Nif = Nif
            };
        }
    }
}
