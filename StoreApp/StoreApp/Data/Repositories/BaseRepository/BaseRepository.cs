using Microsoft.EntityFrameworkCore;

namespace StoreApp.Data.Repositories.BaseRepository
{
    public class BaseRepository<TEntity, TPrimaryKeyDataType> : IDisposable, IBaseRepository<TEntity, TPrimaryKeyDataType> where TEntity : class
    {
        protected StoreContext _context;

        public BaseRepository(StoreContext context)
        {
            _context = context;
        }
        public async Task SaveAsync()
        {
            await _context.SaveChangesAsync();
        }

        public async Task AddAsync(TEntity obj)
        {
            await _context.Set<TEntity>().AddAsync(obj);
        }

        public async Task<TEntity> GetByIdAsync(TPrimaryKeyDataType id)
        {
            return await _context.Set<TEntity>().FindAsync(id);
        }

        public async Task<IEnumerable<TEntity>> GetAllAsync()
        {
            return await _context.Set<TEntity>().ToListAsync();
        }

        public void Update(TEntity obj)
        {
            _context.Entry(obj).State = EntityState.Modified;
        }

        public void Remove(TEntity obj)
        {
            _context.Set<TEntity>().Remove(obj);
        }

        public void Dispose()
        {
            _context.Dispose();
            GC.SuppressFinalize(this);
        }

        protected virtual void Dispose(bool disposing)
        {
        }

    }
}
