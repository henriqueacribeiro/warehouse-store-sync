using StoreApp.DTOs.Orders;
using StoreApp.Models.Orders;

namespace StoreApp.Services.Orders
{
    public interface IOrderService : IService<Order, OrderDto>
    {
        /// <summary>
        /// Obtains all orders for a certain client, given its nif
        /// </summary>
        /// <param name="nif">nif of the user</param>
        /// <returns>list of orders for a certain client</returns>
        Task<IList<Order>> GetByClient(string nif);

        /// <summary>
        /// Obtains all orders for a certain client, given its nif, in transport format
        /// </summary>
        /// <param name="nif">nif of the user</param>
        /// <returns>list of orders for a certain client, in DTO format</returns>
        Task<IList<OrderDto>> GetByClientDtoFormat(string nif);


        /// <summary>
        /// Registers a new order in the system. Will fail if order product list is empty or products does not exist/surpasses the maximum discount
        /// </summary>
        /// <param name="order">order to register, transport format</param>
        /// <returns></returns>
        Task Register(OrderDto order);
    }
}
