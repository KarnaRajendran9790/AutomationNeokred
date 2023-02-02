package trapezaCustBeneficiaryCount;


	
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.text.ParseException;
	import org.apache.log4j.BasicConfigurator;
	import org.apache.poi.ss.usermodel.CellType;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	import org.json.JSONException;

	public class TrapezaBeneCount {

		@SuppressWarnings({ "unused", "resource" })
		public static void main(String[] args)
				throws SQLException, ParseException, ClassNotFoundException, JSONException, IOException {

			BasicConfigurator.configure();

			Connection connection1 = null;
			Statement statement1 = null;
			ResultSet rs1 = null;
			String wlp = null;
			String psk = null;
			String wlpSk = null;
			String mobileNo = null;
			String transactionFlag = "E";

			for (int i = 1; i < 2; i++) {
				String datapath = "C:\\Users\\Karna\\Documents\\Book1.xlsx";

				File excelFile = new File(datapath);
				FileInputStream file = new FileInputStream(excelFile);
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheetAt(0);
				XSSFRow row = sheet.getRow(0);

				connection1 = DriverManager.getConnection(
						"jdbc:postgresql://noekred-prod-db.ceihulngzg4e.ap-south-1.rds.amazonaws.com:5432/card_service",
						"postgres", "Bankx1234");

				Connection connection2 = null;
				Statement statement2 = null;
				ResultSet rs2 = null;
				connection2 = DriverManager.getConnection(
						"jdbc:postgresql://noekred-prod-db.ceihulngzg4e.ap-south-1.rds.amazonaws.com:5432/admin_service",
						"postgres", "Bankx1234");
				Connection connection3 = null;
				Statement statement3 = null;
				ResultSet rs3 = null;
				connection3 = DriverManager.getConnection(
						"jdbc:postgresql://noekred-prod-db.ceihulngzg4e.ap-south-1.rds.amazonaws.com:5432/customer_service",
						"postgres", "Bankx1234");

//			String output = null;
				Class.forName("org.postgresql.Driver");
				String qurey = "SELECT count(*) FROM public.beneficiary_details where customer_hash_id in('')";
				statement1 = connection3.createStatement();
				rs1 = statement1.executeQuery(qurey);
//			output = null;
				while (rs1.next()) {
					String dq1 = "\"";
					mobileNo = dq1 + rs1.getString(2) + dq1;
					System.out.println(rs1.getString(1));
					System.out.println(rs1.getString(2));
					System.out.println(rs1.getString(3));
					String qurey2 = "select wlp_code, psk_secret_key, wlp_secret_key from public.client_details where user_hash_id = '"
							+ rs1.getString(3) + "'";
					statement2 = connection2.createStatement();
					rs2 = statement2.executeQuery(qurey2);
					if (rs2.next()) {
						String dq = "\"";
						wlp = dq + rs2.getString(1) + dq;
						psk = dq + rs2.getString(2) + dq;
						wlpSk = dq + rs2.getString(3) + dq;
						System.out.println("WLP Code :" + wlp);
						System.out.println("PSK Secret Key : " + psk);
						System.out.println("WLP Secret Key : " + wlpSk);

						FileOutputStream outFile = new FileOutputStream(excelFile);

						sheet.getRow(i).createCell(5, CellType.STRING).setCellValue(wlp);

//					sheet.getRow(0).createCell(1, CellType.STRING).setCellValue("karna");
//					sheet.getRow(0).createCell(2, CellType.STRING).setCellValue("manoj");
//					

						workbook.write(outFile);

//					JSONObject requestBody = new JSONObject();
//					requestBody.put("mobileNumber",mobileNo);
//					requestBody.put("transactionFlag",transactionFlag);
//					requestBody.put("pskSecretKey",psk);
//					requestBody.put("wlpCode",wlp);
//					requestBody.put("wlpSecretKey",wlpSk);
//					Response response = given().header("contentType","application/json").
//							contentType(ContentType.JSON).
//							accept(ContentType.JSON).
//							body(requestBody.toJSONString()).
//							when().
//							post("https://prod.bankx.money:9090/api/v1/fetchVirtualCard").
//							then().extract().response();
//					System.out.println(response.asString());
//					System.out.println(response.statusCode());

					}

				}

			}

		}
	}


