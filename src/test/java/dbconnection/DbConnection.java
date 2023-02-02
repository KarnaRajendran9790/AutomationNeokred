package dbconnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;

public class DbConnection {

	public static  void main(String[] args) throws SQLException, ParseException, ClassNotFoundException, JSONException, IOException {

		
		BasicConfigurator.configure();
		
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String mobile = null;
		String clientid = null;
//		for(int i=1;i<2;i++) 
//		{
//		String datapath= "C:\\Users\\Karna\\Documents\\Book1.xlsx";
//		
		
//		File excelFile = new File(datapath);
//		FileInputStream file = new FileInputStream(excelFile);
//		XSSFWorkbook workbook= new XSSFWorkbook(file);
//		XSSFSheet sheet = workbook.getSheetAt(0);
//		XSSFRow row = sheet.getRow(0);
		
		connection1 = DriverManager.getConnection("jdbc:postgresql://noekred-prod-db.ceihulngzg4e.ap-south-1.rds.amazonaws.com:5432/card_service", "postgres", "Bankx1234");

//		String output = null;
		Class.forName("org.postgresql.Driver");
		String qurey = "SELECT * FROM public.transactions t where client_hash_id in('0e25e4c5-920c-49cc-a881-b1caefa7adad') and created_date between ('2022-03-01 00:00:01') and now() and service_type in('1') and status in('Success');";
		statement1 = connection1.createStatement();
		rs1 = statement1.executeQuery(qurey);
		while (rs1.next()) {
			clientid = rs1.getString(clientid);
			mobile = rs1.getString(mobile);
	
		System.out.println(clientid);
		System.out.println(mobile);
		

	
		
		}
		}
	}
//	}