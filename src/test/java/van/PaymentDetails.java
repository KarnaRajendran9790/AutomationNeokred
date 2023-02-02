package van;

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

import authorization.VanAuthorization;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test
public class PaymentDetails {

	public void paymentDetails() throws Exception {

		// To Read Data From Excel Sheet(MobileNumber and Amount)
		String datapath = "C:\\Users\\Karna\\Documents\\BRIAN.xlsx";

		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 145; i++) {
			String rrn;
			
			XSSFRow row = sheet.getRow(i);
			rrn = formatter.formatCellValue(row.getCell(0));
			System.out.println("rrn = " + rrn);
		
			// Fetch Customer Hash Id Based On the MobileNumber Through FetchCustomerByMobileNumber API
			JSONObject getPaymentDetails = new JSONObject();
			getPaymentDetails.put("rrn", rrn);
			RequestSpecification getCustomerByMobileNumber1 = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON).body(getPaymentDetails.toString());
			getCustomerByMobileNumber1.header("client-Hash-Id", "ce990102-715e-4d9e-8f68-4f4ee92f3710");
			getCustomerByMobileNumber1.header("Authorization", "Bearer " + VanAuthorization.vanAuthorization());
			getCustomerByMobileNumber1.baseUri("https://va.neokred.tech:8088/va/v1/payment/details");
	
			Response response = getCustomerByMobileNumber1.post();
			System.out.println(response.asString());
			
			
			
			FileOutputStream outFile = new FileOutputStream(excelFile);
			String amount = response.asString();
			JSONObject json1 = new JSONObject(amount);
			String transferAmount = (String)(json1).getJSONObject("data").getString("amount");
			System.out.println("amount " + transferAmount);
			String status =  (String) (json1).getJSONObject("data").getString("status");
			System.out.println("status " + status);
	
			sheet.getRow(i).createCell(2, CellType.STRING).setCellValue(transferAmount);
			sheet.getRow(i).createCell(3, CellType.STRING).setCellValue(status);
			
			workbook.write(outFile);	
			
	}
	
	}

}
