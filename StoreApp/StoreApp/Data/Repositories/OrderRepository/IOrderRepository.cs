using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Orders;

namespace StoreApp.Data.Repositories.OrderRepository
{
    public interface IOrderRepository : IBaseRepository<Order, Guid>
    {
        /// <summary>
        /// Obtains all orders from a certain client, giving its id
        /// </summary>
        /// <param name="clientId">client id</param>
        /// <returns>list of clients orders</returns>
        Task<ICollection<Order>> GetByClient(Guid clientId);
    }
}
