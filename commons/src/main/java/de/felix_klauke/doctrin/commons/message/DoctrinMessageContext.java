package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinMessageContext {

    void sendObject(JSONObject jsonObject);
}
