package cs.login;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import cs.login.Entity.Request;

public class Requests {
	public static String addRequest(String userName,String reqName) throws Exception {
		try {
			Connection c = Db.connect();
			String query = "insert into requests(username,requestName,status,requestDate)values(?,?,?,?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, userName);
			ps.setString(2, reqName);
			ps.setString(3, "pending");
		    LocalDate today = LocalDate.now();
		    Date sqlDate = Date.valueOf(today);
		    ps.setDate(4, sqlDate);
			ps.executeUpdate();
		    return "Resource added successfully";
		}
		catch(Exception ex) {
			 ex.printStackTrace();
		        return "Error adding resource";
		}		
    }
	public static List<Request> getRequests(String username) {
	    List<Request> requests = new ArrayList<>();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Db.connect();
	        String query = "SELECT distinct requestName, status, requestDate FROM requests WHERE userName=?";
	        ps = c.prepareStatement(query);
	        ps.setString(1, username);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            Request req = new Request();
	            req.setResourceName(rs.getString(1));
	            req.setStatus(rs.getString(2));
	            
	            java.sql.Date sqlDate = rs.getDate(3);
	            if (sqlDate != null) {
	                req.setRequestDate(sqlDate.toLocalDate()); // Convert SQL Date to LocalDate
	            } else {
	                req.setRequestDate(null);
	            }

	            requests.add(req);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace(); // Log the exception for debugging
	    } finally {
	        // Ensure resources are closed
	        try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
	        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
	        try { if (c != null) c.close(); } catch (Exception e) { e.printStackTrace(); }
	    }

	    return requests;
	}

}
