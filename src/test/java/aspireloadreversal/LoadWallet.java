package aspireloadreversal;
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

import dbconnection.Db2ExcelExport;
import emailReport.SendEmail;
import emailReport.SendEmailReport;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@SuppressWarnings("unused")
@Test
public class LoadWallet {

		public void loadReversal() throws Exception {

			// To Read Data From Excel Sheet(MobileNumber and Amount)
			String datapath = "C:\\Users\\Karna\\Documents\\Book3.xlsx";

			File excelFile = new File(datapath);
			FileInputStream file = new FileInputStream(excelFile);
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i < 3; i++) {
				String mobileNumber;
				double amount;

				XSSFRow row = sheet.getRow(i);
				mobileNumber = formatter.formatCellValue(row.getCell(0));
				amount = sheet.getRow(i).getCell(1).getNumericCellValue();
				System.out.println("mobileNumber = " + mobileNumber);
				System.out.println("amount = " + amount);

				// Fetch Customer Hash Id Based On the MobileNumber Through FetchCustomerByMobileNumber API
				RequestSpecification getCustomerByMobileNumber = RestAssured.given();
				getCustomerByMobileNumber.header("Authorization", "Bearer " + authorization.Authorization.token());
				getCustomerByMobileNumber.header("client-Hash-Id", "16d96149-1093-45ee-a201-d447a12cbeb3");
				getCustomerByMobileNumber.header("mobile_number", mobileNumber);
				getCustomerByMobileNumber.baseUri("https://neokred.bankx.money:9092/api/v1/getCustomerByMobile");
				Response response = getCustomerByMobileNumber.get();
				System.out.println(response.asString());
				

				// Take Only the customer hash id from the FetchCustomerByMobileNumber API
				String customerHashId1 = response.asString();
				JSONObject json = new JSONObject(customerHashId1);
				String customerHashId = (json).getJSONObject("details").getString("customerHashId");
				System.out.println("CustomerHashId = " + customerHashId);

				
				JSONObject loadReversalPayload = new JSONObject();
				loadReversalPayload.put("serviceType", "Health");
				loadReversalPayload.put("amount", amount);
				loadReversalPayload.put("remark", "Load");
				loadReversalPayload.put("customerHashId", customerHashId);
				RequestSpecification loadReversal = RestAssured.given().header("contentType", "application/json")
						.contentType(ContentType.JSON).accept(ContentType.JSON).body(loadReversalPayload.toString());
				loadReversal.header("Authorization", "Bearer " + authorization.Authorization.token());
				loadReversal.header("client-Hash-Id", "16d96149-1093-45ee-a201-d447a12cbeb3");
				loadReversal.baseUri("https://neokred.bankx.money:9094/yespay/api/v1/card/load");
				Response response1 = loadReversal.post();
				System.out.println(response1.asString());
				
			}
		}

}
