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

    public ActionCode getActionCode() {
        if (!jsonObject.has("actionCode")) {
            return ActionCode.UNKNOWN;
        }

        return ActionCode.values()[(int) jsonObject.remove("actionCode")];
    }
}
