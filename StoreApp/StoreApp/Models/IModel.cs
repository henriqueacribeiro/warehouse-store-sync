namespace StoreApp.Models
{
    public interface IModel<TDTO> where TDTO : class
    {
        /// <summary>
        /// Converts the model into its transport representation
        /// </summary>
        /// <returns>model info in transport format</returns>
        TDTO ToDto();
    }
}
