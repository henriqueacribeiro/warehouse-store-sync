using Microsoft.EntityFrameworkCore;
using StoreApp.Models.Products;

namespace StoreApp.Data
{
    public class StoreContext : DbContext
    {
        public StoreContext(DbContextOptions<StoreContext> options) : base(options) { }

        public DbSet<Product> Products { get; set; }
    }
}
