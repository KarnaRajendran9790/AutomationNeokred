package authorization;


import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
@Test
public class Authorization {
	@SuppressWarnings("unchecked")
	public static String token() throws Exception {
		JSONObject payload = new JSONObject();
		payload.put("email", "admin@neokred.tech");
		payload.put("password", "0Wm8lz1@");
		Response response = given().header("contentType", "application/json").contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(payload.toString()).when()
				.post("https://neokred.bankx.money:9091/api/v1/authenticate");
		String res = response.asString();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(res);
		String token = (String) json.get("token");
		return token;
	}


}





































