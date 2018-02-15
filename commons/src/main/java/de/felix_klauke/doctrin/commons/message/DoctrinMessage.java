package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessage {

    private final JSONObject jsonObject;
    private ActionCode actionCode;

    public DoctrinMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public ActionCode getActionCode() {
        if (actionCode != null) {
            return actionCode;
        }

        if (!jsonObject.has("actionCode")) {
            return ActionCode.UNKNOWN;
        }

        return (actionCode = ActionCode.values()[(int) jsonObject.remove("actionCode")]);
    }
}
