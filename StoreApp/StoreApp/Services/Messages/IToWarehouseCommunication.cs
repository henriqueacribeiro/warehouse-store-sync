using StoreApp.Models.Orders;

namespace StoreApp.Services.Messages
{
    public interface IToWarehouseCommunication
    {
        /// <summary>
        /// Sends the order to a warehouse
        /// </summary>
        /// <param name="order">order to be sent</param>
        void SendOrderToWarehouse(Order order);
    }
}
