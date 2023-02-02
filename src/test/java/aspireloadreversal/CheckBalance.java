package aspireloadreversal;

import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CheckBalance {

	@SuppressWarnings({ "unchecked", "resource" })
	@Test
	public void checkBalance() throws IOException, ParseException {

		String datapath = "C:\\Users\\Karna\\Documents\\Book2.xlsx";

		// To Read Data From Excel Sheet(MobileNumber)
		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		String mobileNumber;
		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 31011 ; i++) {
			XSSFRow row = sheet.getRow(i);
			mobileNumber = formatter.formatCellValue(row.getCell(0));
			System.out.println(mobileNumber);
			
			//Fetch Balance of the customer from checkBalance API
			JSONObject payload = new JSONObject();
			payload.put("mobileNumber", mobileNumber);
			payload.put("pskSecretKey", "af9d77e5695ccae62e0423e6f57a129c");
			payload.put("wlpCode", "NEOKREDWLAPCODE_PROD");
			payload.put("wlpSecretKey", "NEOKREDWLAPKEY_PROD");
			Response response = given().header("contentType", "application/json").contentType(ContentType.JSON)
					.accept(ContentType.JSON).body(payload).when()
					.post("https://prod.bankx.money:9090/api/v1/checkWalletBalance").then().extract().response();
			
			System.out.println(response.asString());
			System.out.println(response.statusCode());

			String res = response.asString();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(res);
			String balance = (String) json.get("balance");
			System.out.println(balance);

			//To write balance of the customer in Excel sheet
			FileOutputStream outFile = new FileOutputStream(excelFile);
			if (response.statusCode() == 200) {
				sheet.getRow(i).createCell(2, CellType.STRING).setCellValue(balance);
			} else {

				sheet.getRow(i).createCell(3, CellType.STRING).setCellValue(response.asString());
			}
			workbook.write(outFile);
			
			
			
		}
	}
}
