package authorization;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class VanAuthorization {
	@Test

	@SuppressWarnings("unchecked")
	public static String vanAuthorization() throws Exception {
		JSONObject payload = new JSONObject();
		payload.put("username", "admin@neokred.tech");
		payload.put("password", "Neokred@12345");
		Response response = given().header("contentType", "application/json").contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(payload.toString()).when()
				.post("https://van.neokred.tech/va/v1/authenticate");
		String res = response.asString();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(res);
		String token = (String) json.get("data");
		return token;
	}
}
