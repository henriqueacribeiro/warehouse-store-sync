using StoreApp.Data.Repositories.OrderRepository;
using StoreApp.DTOs.Orders;
using StoreApp.Models.Clients;
using StoreApp.Models.Orders;
using StoreApp.Services.Clients;
using StoreApp.Services.Products;

namespace StoreApp.Services.Orders
{
    public class OrderService : IOrderService
    {

        private readonly IOrderRepository orderRepository;
        private readonly IClientService clientService;
        private readonly IProductService productService;

        public OrderService(IOrderRepository orderRepository, IClientService clientService, IProductService productService)
        {
            this.orderRepository = orderRepository ?? throw new ArgumentNullException(nameof(orderRepository));
            this.clientService = clientService ?? throw new ArgumentNullException(nameof(clientService));
            this.productService = productService ?? throw new ArgumentNullException(nameof(productService));
        }

        public async Task<IList<Order>> GetByClient(string nif)
        {
            return (await orderRepository.GetByClient(nif)).ToList();
        }

        public async Task<IList<OrderDto>> GetByClientDtoFormat(string nif)
        {
            return (await orderRepository.GetByClient(nif)).Select(ord => ord.ToDto()).ToList();
        }

        public async Task Register(OrderDto order)
        {
            if (order.Client == null)
            {
                throw new ArgumentException("Missing client information");
            }

            Client? client;
            try
            {
                client = await clientService.FindByNif(order.Client.Nif);
            }
            catch (KeyNotFoundException)
            {
                client = null;
            }
            if (client == null)
            {
                throw new ArgumentException("Client with NIF " + order.Client.Nif + " not found");
            }

            if (order.Products == null || order.Products.Count == 0)
            {
                throw new ArgumentException("No products were selected");
            }
            var products = await ExtractProductsForOrderAsync(order.Products);

            await orderRepository.AddAsync(new Order
            {
                Client = client,
                Products = products,
                Id = Guid.NewGuid(),
                Status = OrderStatus.CREATED
            });
            await orderRepository.SaveAsync();

            order.Client = client.ToDto();
        }

        /// <summary>
        /// Method that extracts product information from a new order, checks it and then returs a list of product orders (or throws an exception on failure).
        /// It checks the maximum discount of the product and if the product exists
        /// </summary>
        /// <param name="orderProducts">list of products to check</param>
        /// <returns>list of products generated for the order</returns>
        /// <exception cref="ArgumentException">if the product is invalid or any info is missing</exception>
        private async Task<ICollection<OrderProduct>> ExtractProductsForOrderAsync(ICollection<OrderProductDto> orderProducts)
        {
            var products = new List<OrderProduct>();

            foreach (OrderProductDto orderProduct in orderProducts)
            {
                try
                {
                    if (orderProduct == null || orderProduct.Product == null || orderProduct.Product.Code == null)
                    {
                        throw new ArgumentException("Invalid product detected");
                    }
                    var currentProduct = await productService.FindByCode(orderProduct.Product.Code);

                    if (currentProduct.MaximumDiscount < orderProduct.AppliedDiscount)
                    {
                        throw new ArgumentException("Invalid discount for product " + currentProduct.Code + " (" + orderProduct.AppliedDiscount + "%). The maximum discount is " + currentProduct.MaximumDiscount + "%");
                    }

                    if (orderProduct.Quantity < 1)
                    {
                        throw new ArgumentException("Invalid quantity for product " + currentProduct.Code + " (" + orderProduct.Quantity + ")");
                    }

                    products.Add(new OrderProduct
                    {
                        Product = currentProduct,
                        Quantity = orderProduct.Quantity,
                        AppliedDiscount = orderProduct.AppliedDiscount
                    });
                }
                catch (KeyNotFoundException)
                {
                    throw new ArgumentException("Invalid product (code: " + orderProduct.Product.Code + ")");
                }
            }
            return products;
        }
    }
}
