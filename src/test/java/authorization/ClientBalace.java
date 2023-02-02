package authorization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test
public class ClientBalace {

	
	public void clientBalance() throws Exception {

		// To Read Data From Excel Sheet(MobileNumber and Amount)
		String datapath = "C:\\Users\\Karna\\Documents\\ClientBalance.xlsx";

		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 121; i++) {
			String clienthashId;
			
			XSSFRow row = sheet.getRow(i);
			clienthashId = formatter.formatCellValue(row.getCell(0));
			System.out.println("clienthashId = " + clienthashId);
		
			// Fetch Customer Hash Id Based On the MobileNumber Through FetchCustomerByMobileNumber API
			
			RequestSpecification getCustomerByMobileNumber1 = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON);
			getCustomerByMobileNumber1.header("client-hash-id", clienthashId );
			getCustomerByMobileNumber1.header("Authorization", "Bearer "+ VanAuthorization.vanAuthorization());
			getCustomerByMobileNumber1.baseUri("https://va.neokred.tech:8088/va/v1/client");
	
			Response response = getCustomerByMobileNumber1.get();
			System.out.println(response.asString());
			
			
			
			FileOutputStream outFile = new FileOutputStream(excelFile);
			String amount = response.asString();
			JSONObject json1 = new JSONObject(amount);
			String transferAmount = (String)(json1).getJSONObject("data").getString("balance");
			System.out.println("amount " + transferAmount);
			
			
			sheet.getRow(i).createCell(3, CellType.STRING).setCellValue(transferAmount);

			workbook.write(outFile);		
	}
}
}
