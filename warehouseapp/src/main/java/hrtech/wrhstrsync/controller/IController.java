package hrtech.wrhstrsync.controller;

import hrtech.wrhstrsync.model.ModelDefinition;
import hrtech.wrhstrsync.model.global.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Controller definition. It contains a method to convert a Response to an ResponseEntity, to be returned by an API
 *
 * @param <O> ModelDefinition object
 */
public interface IController<O extends ModelDefinition> {

    /**
     * Method that converts a Response object, with a valid Model object (or null), into a response entity.
     * <p>
     * It also sets the HTTP status code
     *
     * @param response response to convert
     * @return ResponseEntity object with response info
     */
    default ResponseEntity<String> convertResponseToResponseEntity(Response<O> response) {
        if (response.isSuccess()) {
            return new ResponseEntity<>(response.toJSON().toString(), HttpStatus.OK);
        } else if (response.failureWasClientError()) {
            return new ResponseEntity<>(response.toJSON().toString(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response.toJSON().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
