namespace StoreApp.Data.Repositories.BaseRepository
{
    public interface IBaseRepository<TEntity, TPrimaryKeyDataType> where TEntity : class
    {
        Task SaveAsync();
        Task AddAsync(TEntity obj);
        Task<TEntity> GetByIdAsync(TPrimaryKeyDataType id);
        Task<IEnumerable<TEntity>> GetAllAsync();
        void Update(TEntity obj);
        void Remove(TEntity obj);
        void Dispose();
    }
}
