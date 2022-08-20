namespace StoreApp.Services
{
    public interface IService<TEntity, TDTO> where TEntity : class
    {

        /// <summary>
        /// Converts the <c>TEntity</c> to its transport form (<c>TDTO</c>)
        /// </summary>
        /// <param name="entity">object to be converted</param>
        /// <returns>entity in transport format</returns>
        public TDTO ToDto(TEntity entity);
    }
}
