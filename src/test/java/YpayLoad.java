import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class YpayLoad {
	@SuppressWarnings("unchecked")
	@Test
	public void checkBalance() throws IOException, ParseException {

		// To Read Data From Excel Sheet(MobileNumber and Amount)
		String datapath = "C:\\Users\\Karna\\Documents\\fu.xlsx";

		File excelFile = new File(datapath);
		FileInputStream file = new FileInputStream(excelFile);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		DataFormatter formatter = new DataFormatter();

		for (int i = 1; i < 36; i++) {
			String mobileNumber;
			double amount;
			String merchantTransactionNumber;

			XSSFRow row = sheet.getRow(i);
			mobileNumber = formatter.formatCellValue(row.getCell(0));
			merchantTransactionNumber = formatter.formatCellValue(row.getCell(2));

			amount = sheet.getRow(i).getCell(1).getNumericCellValue();

			System.out.println("mobileNumber = " + mobileNumber);
			System.out.println("merchantTransactionNumber = " + merchantTransactionNumber);
			System.out.println("amount = " + amount);

			// Fetch Balance of the customer from checkBalance API
			JSONObject payload = new JSONObject();
			payload.put("mobileNumber", mobileNumber);
			payload.put("amount", amount);
			payload.put("merchantTransactionNumber", merchantTransactionNumber);
			payload.put("remark", "Ecollect Money");
			payload.put("tagName", "YPAY Customer");
			payload.put("pskSecretKey", "5ebc4e74ad7099f813e199fa50e5f30c");
			payload.put("wlpCode", "YPAY_WLAPCODE_PROD");
			payload.put("wlpSecretKey", "YPAY_WLAPKEY_PROD");

			Response response = given().header("contentType", "application/json").contentType(ContentType.JSON)
					.accept(ContentType.JSON).body(payload).when()
					.post("https://prod.bankx.money:9090/api/v1/loadMoneyToWallet");

			System.out.println(response.asString());
			System.out.println(response.statusCode());

		}
	}
}
