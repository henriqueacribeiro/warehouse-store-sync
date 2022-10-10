using Microsoft.EntityFrameworkCore;
using StoreApp.Data;
using StoreApp.Data.Repositories.ClientRepository;
using StoreApp.Data.Repositories.OrderRepository;
using StoreApp.Data.Repositories.ProductRepository;
using StoreApp.Models.Configuration;
using StoreApp.Services;
using StoreApp.Services.Clients;
using StoreApp.Services.Messages;
using StoreApp.Services.Orders;
using StoreApp.Services.Products;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDbContext<StoreContext>(opt =>
    opt.UseInMemoryDatabase("StoreDatabase"));

var _configuration = builder.Configuration;
var messagingConfig = _configuration.GetSection("Messaging");
builder.Services.Configure<MessagingSettings>(messagingConfig);

//Add repositories bind here
builder.Services.AddScoped<IProductRepository, ProductRepository>();
builder.Services.AddScoped<IClientRepository, ClientRepository>();
builder.Services.AddScoped<IOrderRepository, OrderRepository>();

//Add services bind here
builder.Services.AddScoped<IProductService, ProductService>();
builder.Services.AddScoped<IClientService, ClientService>();
builder.Services.AddScoped<IOrderService, OrderService>();
builder.Services.AddScoped<IToWarehouseCommunication, ToWarehouseCommunicationByMessageBroker>();

builder.Services.AddHostedService<Receiver>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
