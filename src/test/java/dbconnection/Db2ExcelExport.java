package dbconnection;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.annotations.Test;
@Test
public class Db2ExcelExport {

	public static void main(String[] args) {
		new Db2ExcelExport().export();
	}

	@SuppressWarnings("unused")
	public void export() {
		String jdbcURL = "jdbc:postgresql://noekred-prod-db.ceihulngzg4e.ap-south-1.rds.amazonaws.com:5432/card_service";
		String username = "postgres";
		String password = "Bankx1234";

		String timeStamp = new SimpleDateFormat("yyyy.MMM.dd HH.MM").format(new Date());// time stamp
		String Name = "Aspire "+ timeStamp+".xlsx";
		
		String excelFilePath = "C:\\Users\\Karna\\Documents\\Book4.xlsx";

		try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
			String sql = "SELECT * FROM public.transactions t where client_hash_id in('0e25e4c5-920c-49cc-a881-b1caefa7adad') and created_date between  ('2022-03-01 00:00:01') and now() and service_type in('1') and status in('Success');";

			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);
			
			@SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
			OutputStream fileOut = new FileOutputStream(excelFilePath);   
			System.out.println("Excel File has been created successfully.");   
			wb.write(fileOut);
			

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Transaction");

			writeHeaderLine(sheet);

			writeDataLines(result, workbook, sheet);

			FileOutputStream outputStream = new FileOutputStream(excelFilePath);
			workbook.write(outputStream);
			workbook.close();

			statement.close();

		} catch (SQLException e) {
			System.out.println("Datababse error:");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File IO error:");
			e.printStackTrace();
		}
	}

	private void writeHeaderLine(XSSFSheet sheet) {

		Row headerRow = sheet.createRow(0);

		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("id");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("created_date");

		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("amount");

		headerCell = headerRow.createCell(3);
		headerCell.setCellValue("client_hash_id");

		headerCell = headerRow.createCell(4);
		headerCell.setCellValue("customer_hash_id");


		headerCell = headerRow.createCell(5);
		headerCell.setCellValue("description");
		headerCell = headerRow.createCell(6);
		headerCell.setCellValue("transaction_ref");


		headerCell = headerRow.createCell(7);
		headerCell.setCellValue("status");
		headerCell = headerRow.createCell(8);
		headerCell.setCellValue("service_type");

		headerCell = headerRow.createCell(9);
		headerCell.setCellValue("customer_mobile");
		headerCell = headerRow.createCell(10);
		headerCell.setCellValue("first_name");
		headerCell = headerRow.createCell(11);
		headerCell.setCellValue("last_name");

		headerCell = headerRow.createCell(12);
		headerCell.setCellValue("rrn");
	}

	private void writeDataLines(ResultSet result, XSSFWorkbook workbook,
			XSSFSheet sheet) throws SQLException {
		int rowCount = 1;

		while (result.next()) {
			String id = result.getString("id");
			float amount = result.getFloat("amount");
			Timestamp created_date = result.getTimestamp("created_date");
			String client_hash_id= result.getString("client_hash_id");
			String description= result.getString("description");
			String customer_hash_id= result.getString("customer_hash_id");
			String transaction_ref= result.getString("transaction_ref");
			String status= result.getString("status");
			@SuppressWarnings("unused")
			String service_type= result.getString("service_type");
			String customer_mobile= result.getString("customer_mobile");
			String first_name= result.getString("first_name");
			String last_name= result.getString("last_name");
			String rrn= result.getString("rrn");



			Row row = sheet.createRow(rowCount++);

			int columnCount = 0;
			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(id);

			cell = row.createCell(columnCount++);
			CellStyle cellStyle = workbook.createCellStyle();
			CreationHelper creationHelper = workbook.getCreationHelper();
			cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
			cell.setCellStyle(cellStyle);
			cell.setCellValue(created_date);
			
			cell = row.createCell(columnCount++);
			cell.setCellValue(amount);

			cell = row.createCell(columnCount++);
			cell.setCellValue(client_hash_id);

			cell = row.createCell(columnCount++);
			cell.setCellValue(customer_hash_id);

			cell = row.createCell(columnCount++);
			cell.setCellValue(description);

			cell = row.createCell(columnCount++);
			cell.setCellValue(transaction_ref);
			
			cell = row.createCell(columnCount++);
			cell.setCellValue(status);
			cell = row.createCell(columnCount++);
			cell.setCellValue("Load Reversal");

			cell = row.createCell(columnCount++);
			cell.setCellValue(customer_mobile);
			cell = row.createCell(columnCount++);
			cell.setCellValue(first_name);

			cell = row.createCell(columnCount++);
			cell.setCellValue(last_name);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rrn);


			System.out.println("success");

		}
	}

}
