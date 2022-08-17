package hrtech.wrhstrsync.model;

import org.json.JSONObject;

/**
 * Interface to be used by the DTOs
 */
public interface ModelDTODefinition {

    /**
     * Converts the object into its JSON representation
     *
     * @return JSON object with info about the object
     */
    JSONObject toJSON();

}
