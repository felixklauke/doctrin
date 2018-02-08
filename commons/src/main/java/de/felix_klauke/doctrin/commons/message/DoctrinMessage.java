package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessage {

    private final JSONObject jsonObject;

    public DoctrinMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
