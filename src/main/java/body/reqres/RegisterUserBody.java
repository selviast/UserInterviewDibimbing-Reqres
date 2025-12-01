package body.reqres;

import org.json.JSONObject;
import utils.ConfigReader;

public class RegisterUserBody {
    public JSONObject getBody() {
        org.json.JSONObject body = new JSONObject();
        body.put("email", ConfigReader.getProperty("email"));
        body.put("password", ConfigReader.getProperty("password"));
        return body;
    }
}
