package aspireloadreversal;

import java.io.File;
import java.io.FileInputStream;

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
public class BlockWalletAndUnblockWallet {

	
	public void blockandunblock() throws Exception {

		// To Read Data From Excel Sheet(MobileNumber and Amount)
		String datapath = "C:\\Users\\Karna\\Documents\\Book6.xlsx";

		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 81; i++) {
			String mobileNumber;
			
			XSSFRow row = sheet.getRow(i);
			mobileNumber = formatter.formatCellValue(row.getCell(0));
			System.out.println("mobileNumber = " + mobileNumber);
		

			// Fetch Customer Hash Id Based On the MobileNumber Through FetchCustomerByMobileNumber API
			JSONObject getCustomerByMobileNumber = new JSONObject();
			getCustomerByMobileNumber.put("mobileNumber", mobileNumber);
			getCustomerByMobileNumber.put("pskSecretKey", "af9d77e5695ccae62e0423e6f57a129c");
			getCustomerByMobileNumber.put("wlpCode", "NEOKREDWLAPCODE_PROD");
			getCustomerByMobileNumber.put("wlpSecretKey", "NEOKREDWLAPKEY_PROD");
			RequestSpecification getCustomerByMobileNumber1 = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON).body(getCustomerByMobileNumber.toString());
			getCustomerByMobileNumber1.header("mobile_number", mobileNumber);
			getCustomerByMobileNumber1.baseUri("https://prod.bankx.money:9090/api/v1/blockWallet");
	
			Response response = getCustomerByMobileNumber1.post();
			System.out.println(response.asString());
			
			
			JSONObject getCustomerByMobileNumber11 = new JSONObject();
			getCustomerByMobileNumber11.put("mobileNumber", mobileNumber);
			getCustomerByMobileNumber11.put("pskSecretKey", "af9d77e5695ccae62e0423e6f57a129c");
			getCustomerByMobileNumber11.put("wlpCode", "NEOKREDWLAPCODE_PROD");
			getCustomerByMobileNumber11.put("wlpSecretKey", "NEOKREDWLAPKEY_PROD");
		
			RequestSpecification getCustomerByMobileNumber111 = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON).body(getCustomerByMobileNumber11.toString());
			getCustomerByMobileNumber111.header("mobile_number", mobileNumber);
			getCustomerByMobileNumber111.baseUri("https://prod.bankx.money:9090/api/v1/unblockWallet");
		
			Response response1 = getCustomerByMobileNumber111.post();
			System.out.println(response1.asString());
}
	}
}
