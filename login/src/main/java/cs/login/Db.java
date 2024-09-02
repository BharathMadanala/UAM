package cs.login;

import java.sql.Connection;
import java.sql.DriverManager;

public class Db {
	public static Connection connect() throws Exception {
		String driver = "com.mysql.cj.jdbc.Driver" ,url = "jdbc:mysql://localhost:3306/login" , username = "root" , password = "bharath";
//		Load the driver to perform carrying operations
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url , username , password);
		return conn;
		
	}

}
