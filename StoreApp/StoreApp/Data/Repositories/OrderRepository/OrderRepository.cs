using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Query;
using StoreApp.Data.Repositories.BaseRepository;
using StoreApp.Models.Orders;
using StoreApp.Models.Products;

namespace StoreApp.Data.Repositories.OrderRepository
{
    public class OrderRepository : BaseRepository<Order, Guid>, IOrderRepository
    {
        public OrderRepository(StoreContext context) : base(context)
        {
        }

        private IIncludableQueryable<Order, Product> IncludeAll()
        {
            return _context.Orders
                .Include(ord => ord.Client)
                .Include(ord => ord.Products).ThenInclude(prd => prd.Product);
        }

        public new async Task<Order> GetByIdAsync(Guid id)
        {
            return await IncludeAll().Where(ord => ord.Id.Equals(id)).FirstOrDefaultAsync();
        }

        public new async Task<IEnumerable<Order>> GetAllAsync()
        {
            return await IncludeAll().ToListAsync();
        }

        public async Task<ICollection<Order>> GetByClient(string nif)
        {
            return await IncludeAll().Where(ord => ord.Client.Nif.Equals(nif)).ToListAsync();
        }
    }
}
