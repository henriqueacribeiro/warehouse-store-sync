package hrtech.wrhstrsync.model;


import org.json.JSONObject;

/**
 * Interface to be used by the models in the project.
 */
public interface ModelDefinition<D extends ModelDTODefinition> {

    /**
     * Converts the object into its DTO representation
     *
     * @return DTO representation of the object
     */
    D toDTO();

    /**
     * Converts the object into its JSON representation
     *
     * @return JSON object with info about the object
     */
    default JSONObject toJSON() {
        return toDTO().toJSON();
    }
}
