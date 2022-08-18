package hrtech.wrhstrsync.service;

import hrtech.wrhstrsync.model.dto.ProductDTO;
import hrtech.wrhstrsync.model.global.Response;
import hrtech.wrhstrsync.model.product.Product;
import hrtech.wrhstrsync.repository.IProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service responsible for Product operations
 */
@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private IProductRepository productRepository;

    @Autowired
    public void setOrderRepository(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Method that fetches a product by its code
     *
     * @param code code to search
     * @return product built, if found; empty Optional otherwise
     */
    public Optional<Product> getByCode(String code) {
        if (productRepository.existsById(code)) {
            return Optional.of(productRepository.getReferenceById(code));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Method that returns all products
     *
     * @return products on database
     */
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * Method that saves a new product on the database. It checks if the product code already exists
     *
     * @param productToSave product to save
     * @return Response with product info, info about error on failure
     */
    public Response<Product> save(ProductDTO productToSave) {
        try {
            if (productToSave == null) {
                throw new IllegalArgumentException("Invalid product info for creation");
            }

            String code = productToSave.getCode();
            String name = productToSave.getName();
            double price = productToSave.getPrice();

            Optional<Product> product = Product.ProductFactory.buildProduct(name, code, price);
            Optional<Product> productChecker = getByCode(code);
            if (productChecker.isPresent()) {
                throw new IllegalArgumentException("Product already exists (" + code + ")");
            }
            if (product.isPresent()) {
                Product productSaved = productRepository.save(product.get());
                return new Response<>(Response.ResponseValue.SUCCESS, "", productSaved);
            } else {
                logger.error("Error saving new product");
                return new Response<>(Response.ResponseValue.SERVER_FAIL, "Error saving product", null);
            }
        } catch (IllegalArgumentException iae) {
            logger.warn(iae.getLocalizedMessage());
            return new Response<>(Response.ResponseValue.CLIENT_FAIL, iae.getLocalizedMessage(), null);
        }
    }
}
