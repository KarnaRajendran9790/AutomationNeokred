package cashfree;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ChangeStatus {
	

	public void Auth() throws Exception 
	{
			String datapath = "C:\\Users\\Karna\\Documents\\AzebonCustomers.xlsx";

			File excelFile = new File(datapath);
			FileInputStream file = new FileInputStream(excelFile);
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i < 3; i++) {
				String vAccId;

				XSSFRow row = sheet.getRow(i);
				vAccId = formatter.formatCellValue(row.getCell(0));
				System.out.println("vAccId = " + vAccId);
			
		
			
			JSONObject changeStatus2 = new JSONObject();
			changeStatus2.put("vAccountId", "vAccId");
			changeStatus2.put("status", "INACTIVE");
		
			RequestSpecification changeStatus1 = RestAssured.given().header("contentType", "application/json")
					.contentType(ContentType.JSON).accept(ContentType.JSON).body(changeStatus2.toString());
			changeStatus1.header("Authorization", "Bearer ");
			changeStatus1.baseUri("https://cac-api.cashfree.com/cac/v1/changeVAStatus");
			Response response1 = changeStatus1.post();
			System.out.println(response1.asString());
			
		}
	}

}
