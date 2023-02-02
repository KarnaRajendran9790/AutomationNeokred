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
public class FetchCusByMobileAndLoadReversal {

	public void loadReversal() throws Exception {

		// To Read Data From Excel Sheet(MobileNumber and Amount)
		String datapath = "C:\\Users\\Karna\\Documents\\Book3.xlsx";

		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 81; i++) {
			String mobileNumber;
			double amount;
			String clientHashId;

			XSSFRow row = sheet.getRow(i);
			mobileNumber = formatter.formatCellValue(row.getCell(0));
			amount = sheet.getRow(i).getCell(1).getNumericCellValue();
//			amount = formatter.formatCellValue(row.getCell(1));
			clientHashId = sheet.getRow(i).getCell(2).getStringCellValue();
			clientHashId = formatter.formatCellValue(row.getCell(2));
			System.out.println("mobileNumber = " + mobileNumber);
			System.out.println("amount = " + amount);
			System.out.println("clientHashId = " + clientHashId);

			// Fetch Customer Hash Id Based On the MobileNumber Through FetchCustomerByMobileNumber API
			RequestSpecification getCustomerByMobileNumber = RestAssured.given();
			getCustomerByMobileNumber.header("Authorization", "Bearer " + authorization.Authorization.token());
			getCustomerByMobileNumber.header("client-Hash-Id", clientHashId);
			getCustomerByMobileNumber.header("mobile_number", mobileNumber);
			getCustomerByMobileNumber.baseUri("https://neokred.bankx.money:9092/api/v1/getCustomerByMobile");
			Response response = getCustomerByMobileNumber.get();
			System.out.println(response.asString());
			

			// Take Only the customer hash id from the FetchCustomerByMobileNumber API
			String customerHashId1 = response.asString();
			JSONObject json = new JSONObject(customerHashId1);
			String customerHashId = (json).getJSONObject("details").getString("customerHashId");
			System.out.println("CustomerHashId = " + customerHashId);

			// From the customer wallet here it will do load reversal
			JSONObject loadReversalPayload = new JSONObject();
			loadReversalPayload.put("txnRefNo", "LW1612779268726");
			loadReversalPayload.put("amount", amount);
			loadReversalPayload.put("remark", "Refund");
			loadReversalPayload.put("customerHashId", customerHashId);
			RequestSpecification loadReversal = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON).body(loadReversalPayload.toString());
			loadReversal.header("Authorization", "Bearer " + authorization.Authorization.token());
			loadReversal.header("client-Hash-Id",clientHashId );
			loadReversal.baseUri("https://neokred.bankx.money:9094/yespay/api/v1/card/load/reversal");
			Response response1 = loadReversal.post();
			System.out.println(response1.asString());
			
			
		
						
			
//			Fetch TxnRefNo and Balance after load reversal
//			FileOutputStream outFile = new FileOutputStream(excelFile);
//			if (response.statusCode() == 200) {
//			String txnRefNo = response1.asString();
//			JSONObject json1 = new JSONObject(txnRefNo);
//			String txnRefNo1 = (String)(json).getJSONObject("details").getString("txnRefNo");
//			System.out.println("txnRefNo " + txnRefNo1);
//			String balance =  (String) (json).getJSONObject("details").getString("balance");
//			System.out.println("balance " + balance);
//		
//
//			//To write the response(txnRefNo and balance) in Excel Sheet
//				sheet.getRow(i).createCell(2, CellType.STRING).setCellValue(txnRefNo1);
//
//				sheet.getRow(i).createCell(3, CellType.STRING).setCellValue(balance);
//
//			} 
//			
//			else {
//
//				sheet.getRow(i).createCell(5, CellType.STRING).setCellValue(response.asString());
//			}
//			workbook.write(outFile);
		}
		
	}
}
