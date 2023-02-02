package learning;

import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test
public class Login {
	
	@SuppressWarnings("unused")
	public void login() throws Exception {
	JSONObject payload = new JSONObject();
	payload.put("username", "bankX@neokred.tech");
	payload.put("password", "0Wm8lz1@");
	payload.put("token", "");
	payload.put("count", "1");
	payload.put("appFlag", "true");
	RequestSpecification login = RestAssured.given().header("contentType", "application/json")
			.contentType(ContentType.JSON).accept(ContentType.JSON).body(payload.toString());
	login.header("Authorization","Bearer " + authorization.Authorization.token());
	login.header("role","Client");
	login.baseUri("https://staging.bankx.money:9091/api/v1/login");
	Response response = login.post();
	System.out.println(response.asString());
	
	for (int i = 123455; i < 123459; i++) {
		
		System.out.println(i);
				
		
		JSONObject payload1 = new JSONObject();
		payload1.put("username", "bankX@neokred.tech");
		payload1.put("otp", i);
		payload1.put("token", "");
		payload1.put("appFlag", "true");
		RequestSpecification response1 = RestAssured.given().header("contentType", "application/json")
				.contentType(ContentType.JSON).accept(ContentType.JSON).body(payload.toString());
		login.header("Authorization","Bearer " + authorization.Authorization.token());
		login.header("role","Client");
		login.baseUri("https://staging.bankx.money:9091/api/v1/verify/login");
		Response response11 = login.post();
		System.out.println(response11.asString());
	}

}
}
