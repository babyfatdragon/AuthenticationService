package jp.co.workscm.ec.AuthenticationService;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;

public class TestUtil {
    public static JsonObject readJsonContent(Response response) {
        JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readObject();
    }

    public static JsonReader readJsonStringFromResponse(Response response) {
        String jsonString = response.readEntity(String.class);
        StringReader stringReader = new StringReader(jsonString);
        return Json.createReader(stringReader);
    }
}
