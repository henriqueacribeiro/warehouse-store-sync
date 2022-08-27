package hrtech.wrhstrsync.controller;

import hrtech.wrhstrsync.model.dto.OrderDTO;
import hrtech.wrhstrsync.model.global.Response;
import hrtech.wrhstrsync.model.order.Order;
import hrtech.wrhstrsync.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

/**
 * Controller that handles requests associated with order
 */
@RestController
@RequestScope
@RequestMapping("/order")
public class OrderController implements IController<Order> {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtain order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order obtained", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = Response.class)))
    })
    public ResponseEntity<String> getOrderByID(@RequestParam(name = "id") long id) {
        Optional<Order> order = orderService.getOrderByID(id);
        return order.map(orderObtained -> new ResponseEntity<>(orderObtained.toJSON().toString(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response<>(Response.ResponseValue.CLIENT_FAIL, "Order not found", null).toJSON().toString(), HttpStatus.NOT_FOUND));
    }

    @PutMapping(produces = "application/json")
    @Operation(summary = "Updates an order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status created", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Failed while updating order status due to client error", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Failed while updating order status due to server error", content = @Content(schema = @Schema(implementation = Response.class)))
    })
    public ResponseEntity<String> updateStatus(@RequestBody String body) {
        JSONObject jsonBody;
        try {
            jsonBody = new JSONObject(body);
        } catch (JSONException jsoen) {
            return new ResponseEntity<>("Invalid request body", HttpStatus.BAD_REQUEST);
        }
        return convertResponseToResponseEntity(orderService.updateOrderStatus(jsonBody));
    }
}
