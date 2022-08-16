# Warehouse/Store Sync Example

## Description

Various stores will send orders that receives to a warehouse application, that will then validate the products existence and stores the information.

After that, the warehouse can send status changes to the store that sent the information.

## How was it made?

Both applications (warehouse and store) use Java 11 with Spring framework. The communication between them is performed using a message broker, and the data is stored in a MySQL database.

It is possible to run the application on containers using Docker, isolating it from the system.

