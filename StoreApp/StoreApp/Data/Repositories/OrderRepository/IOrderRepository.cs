using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Orders;

namespace StoreApp.Data.Repositories.OrderRepository
{
    public interface IOrderRepository : IBaseRepository<Order, Guid>
    {
        /// <summary>
        /// Obtains all orders from a certain client, giving its nif
        /// </summary>
        /// <param name="clientNif">client nif</param>
        /// <returns>list of client's orders</returns>
        Task<ICollection<Order>> GetByClient(string nif);
    }
}
