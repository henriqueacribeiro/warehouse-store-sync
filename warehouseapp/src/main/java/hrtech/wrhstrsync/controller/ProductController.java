package hrtech.wrhstrsync.controller;

import hrtech.wrhstrsync.model.dto.ProductDTO;
import hrtech.wrhstrsync.model.global.Response;
import hrtech.wrhstrsync.model.product.Product;
import hrtech.wrhstrsync.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller that handles requests associated with products
 */
@RestController
@RequestScope
@RequestMapping("/product")
public class ProductController implements IController<Product> {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "byCode", produces = "application/json")
    @Operation(summary = "Obtain product by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product obtained", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = Response.class)))})
    public ResponseEntity<String> getByCode(@RequestParam(name = "code") String productCode) {
        Optional<Product> product = productService.getByCode(productCode);
        return product.map(value -> new ResponseEntity<>(value.toJSON().toString(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response<>(Response.ResponseValue.CLIENT_FAIL, "Product not found", null).toJSON().toString(), HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtain all products")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All products", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),})
    public ResponseEntity<List<ProductDTO>> getAll() {
        return new ResponseEntity<>(productService.getAll().stream().map(Product::toDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Creates a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Failed while creating due to client error", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Failed while creating due to server error", content = @Content(schema = @Schema(implementation = Response.class)))
    })
    public ResponseEntity<String> save(@RequestBody ProductDTO productInfo) {
        Response<Product> product = productService.save(productInfo);
        return convertResponseToResponseEntity(product);
    }
}
